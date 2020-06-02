package com.github.johan.backstrom.common.corev2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DataCow<T> {

    private T theDairy;
    private Class<T> clazz;
    private Map<String, Method> generators = new HashMap<>();
    private Map<String, Field> allFields = new HashMap<>();
    private Set<Field> handledFields = new HashSet<>();

    public static <T> DataCow<T> generateDairyFor(Class<T> clazz) {

        DataCow<T> theCow = new DataCow<>();
        theCow.clazz = clazz;
        try {
            theCow.theDairy = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        theCow.collectGenerators();

        return theCow;
    }

    private void collectGenerators() {

        this.generators = Arrays.stream(clazz.getAnnotation(WithGenerators.class).value())
                .map(scannedClass -> Arrays.asList(scannedClass.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .filter(method -> method.isAnnotationPresent(Generator.class))
                .collect(
                        Collectors.toMap(
                                method -> method.getAnnotation(Generator.class).value(),
                                method -> method
                        )
                );
    }

    public DataCow<T> with(With<T> with) {
        with.with(theDairy);
        return this;
    }

    public T milkCow() {

        allFields = Arrays
                .stream(theDairy.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Attribute.class))
                .collect(
                        Collectors.toMap(
                                field -> {
                                    Attribute[] attributeAnnotations = field.getAnnotationsByType(Attribute.class);
                                    if (attributeAnnotations.length > 1){
                                        throw new RuntimeException("The can only be one Attribute annotation.");
                                    }
                                    return attributeAnnotations[0].value();
                                },
                                field -> field
                        )
                );

        allFields.values().forEach(this::milkField);
        return theDairy;
    }

    private void milkField(Field field) {

        if (handledFields.contains(field)) {
            // We've already been here. Don't do anything!
            return;
        }

        handledFields.add(field);

        try {
            if (field.get(theDairy) != null ){
                return;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Field is not accessable");
        }

        // Get the method that generates the value
        Method generator = Optional.ofNullable(
                generators.get(field.getAnnotation(Attribute.class).value())
        ).orElseThrow(RuntimeException::new);

        // Get its dependencies (TODO: Check by Annotation, type and name, type)
        List<Field> dependantFields = Arrays.stream(generator.getParameters())
                .map(
                        parameter -> {
                            if (parameter.getAnnotation(References.class) != null) {
                                return Optional.ofNullable(
                                        allFields.get(parameter.getAnnotation(References.class).value())
                                ).orElseThrow(RuntimeException::new);
                            } else {
                                throw new RuntimeException("Parameter has no reference");
                            }
                        }
                ).collect(Collectors.toList());

        dependantFields.forEach(
                dependantField -> milkField(dependantField)
        );

        try {
            Object[] parameterValues = dependantFields.stream().map(field1 -> {
                        try {
                            return field1.get(theDairy);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Can't access method");
                        }
                    }
            ).toArray();

            // Call method and set value
            Object generatedValue = generator.invoke(generator.getDeclaringClass().newInstance(), parameterValues);
            field.set(theDairy, generatedValue);

        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Can't access field");
        }
    }
}

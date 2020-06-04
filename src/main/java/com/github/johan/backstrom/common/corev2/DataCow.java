package com.github.johan.backstrom.common.corev2;

import com.github.johan.backstrom.common.corev2.exception.DuplicateAttributeException;
import com.github.johan.backstrom.common.corev2.exception.GeneratorNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class DataCow<T> {

    private T theDairy;
    private Class<T> clazz;
    private Map<String, DataGenerator> generators = new HashMap<>();
    private Map<String, DataField> allFields = new HashMap<>();
    private Set<String> handledFieldAttributeIds = new HashSet<>();

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

    public DataCow<T> with(With<T> with) {
        with.with(theDairy);
        return this;
    }

    public T milkCow() {

        allFields = collectFields();
        checkThatAllFieldsHaveGenerators();
        allFields.values().forEach(field -> milkFieldAndParentsRecursively(field));
        return theDairy;
    }

    private void collectGenerators() {

        if (clazz.getAnnotation(WithGenerators.class) == null) {
            throw new GeneratorNotFoundException("@WithGenerators was not specified in class " + clazz.getName());
        }

        Map<Class, Object> generatorInstanceObjects = Arrays.stream(clazz.getAnnotation(WithGenerators.class).value())
                .collect(
                        Collectors.toMap(
                                aClass -> aClass,
                                aClass -> {
                                    try {
                                        return aClass.newInstance();
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        throw new RuntimeException("Generator class could not be instantiated", e);
                                    }
                                }
                        )
                );

        this.generators = generatorInstanceObjects.keySet().stream()
                .map(classToScan -> Arrays.asList(classToScan.getDeclaredMethods()))
                .flatMap(Collection::stream)
                .filter(method -> method.isAnnotationPresent(Generator.class))
                .map(method -> DataGenerator.builder()
                        .setAttributeId(method.getDeclaredAnnotation(Generator.class).value())
                        .setMethod(method)
                        .setObject(generatorInstanceObjects.get(method.getDeclaringClass()))
                        .build()
                )
                .collect(
                        Collectors.toMap(
                                generator -> generator.getAttributeId(),
                                generator -> generator,
                                (generator1, generator2) -> {
                                    throw new DuplicateAttributeException(
                                            String.format(
                                                    "Duplicate attribute ids found in generators %s and %s",
                                                    generator1.getQualifiedMethodName(),
                                                    generator2.getQualifiedMethodName()
                                            )
                                    );
                                }
                        )
                );
    }

    private Map<String, DataField> collectFields() {
        return Arrays.stream(theDairy.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Attribute.class))
                .filter(field -> field.getAnnotationsByType(Attribute.class).length > 0)
                .map(field -> new DataField(
                                field.getAnnotationsByType(Attribute.class)[0].value(),
                                theDairy,
                                field
                        )
                )
                .collect(
                        Collectors.toMap(
                                field -> field.getAttributeId(),
                                field -> field,
                                (field1, field2) -> {
                                    throw new DuplicateAttributeException(String.format("Duplicate attribute ids found in attributes %s and %s", field1.getQualifiedFieldName(), field2.getQualifiedFieldName()));
                                }
                        )
                );
    }

    private void checkThatAllFieldsHaveGenerators() {

        if (!generators.keySet().containsAll(allFields.keySet())) {

            Set<String> attributeIds = allFields.keySet();
            attributeIds.removeAll(generators.keySet());
            throw new GeneratorNotFoundException(
                    String.format(
                            "No generator was found for the following fields: %s",
                            attributeIds
                    )
            );
        }

    }

    private Object milkFieldAndParentsRecursively(DataField field) {

        if (handledFieldAttributeIds.contains(field.getAttributeId())) {
            // We've already been here. Don't do anything but return the value (which can be null)!
            return field.getFieldValue();
        }

        handledFieldAttributeIds.add(field.getAttributeId());

        if (field.getFieldValue() != null) {
            // If the field has a value already, don't create a new one
            return field.getFieldValue();
        }

        DataGenerator generator = generators.get(field.getAttributeId());

        Object[] parameterValues = generator.getParentFieldAttributeIds().stream()
                .map(attributeId -> allFields.get(attributeId))
                .map(parentField -> milkFieldAndParentsRecursively(parentField))
                .collect(Collectors.toList())
                .toArray();

        field.setFieldValue(generator.invokeGenerator(parameterValues));

        return field.getFieldValue();
    }
}

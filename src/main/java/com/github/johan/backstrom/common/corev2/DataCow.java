package com.github.johan.backstrom.common.corev2;

import com.github.johan.backstrom.common.corev2.exception.DuplicateAttributeException;
import com.github.johan.backstrom.common.corev2.exception.GeneratorNotFoundException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.johan.backstrom.common.corev2.FieldType.*;

public class DataCow<T> {

    private T theDairy;
    private Class<T> clazz;
    private Map<String, DataGenerator> generators = new HashMap<>();
    private Map<String, DataField> allFields = new HashMap<>();
    private Set<String> handledFieldAttributeIds = new HashSet<>();
    private Set<Class> withGenerators = new HashSet<>();
    private boolean useFieldByName;

    public static <T> DataCow<T> generateDairyFor(Class<T> clazz) {

        DataCow<T> theCow = new DataCow<>();
        theCow.clazz = clazz;
        try {
            theCow.theDairy = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return theCow;
    }

    public DataCow<T> with(With<T> with) {
        with.with(theDairy);
        return this;
    }

    public DataCow<T> withGenerator(Class clazz) {
        this.withGenerators.add(clazz);
        return this;
    }

    public DataCow<T> useVariableNamesAsAttributeId(){
        this.useFieldByName = true;
        return this;
    }

    public T milkCow() {

        generators = collectGenerators();
        allFields = collectFields();
        checkThatAllFieldsHaveGenerators(generators, allFields);
        allFields.values().forEach(field -> milkFieldAndParentsRecursively(field));
        return theDairy;
    }

    private Map<String, DataGenerator> collectGenerators() {

        if (clazz.getAnnotation(WithGenerators.class) != null) {
            withGenerators.addAll(Arrays.asList(clazz.getAnnotation(WithGenerators.class).value()));
        }

        if (withGenerators.isEmpty()) {
            throw new GeneratorNotFoundException("@WithGenerators was not specified in class " + clazz.getName());
        }

        Map<Class, Object> generatorInstanceObjects = withGenerators.stream()
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

        return generatorInstanceObjects.keySet().stream()
                .map(classToScan -> Arrays.asList(classToScan.getDeclaredMethods()))
                .flatMap(methods -> methods.stream())
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
                .filter(field -> field.isAnnotationPresent(Attribute.class) || useFieldByName)
                .map(field -> new DataField(
                                field.isAnnotationPresent(Attribute.class)
                                        ? field.getAnnotationsByType(Attribute.class)[0].value()
                                        : field.getName(),
                                theDairy,
                                field,
                                field.isAnnotationPresent(Attribute.class) ? annotatedField : namedField
                        )
                )
                .collect(
                        Collectors.toMap(
                                field -> field.getAttributeId(),
                                field -> field,
                                (field1, field2) -> getFieldWithHighestPrio(field1, field2)
                        )
                );
    }

    private DataField getFieldWithHighestPrio(DataField field1, DataField field2){
        if (field1.getFieldType().equals(field2.getFieldType())){
            throw new DuplicateAttributeException(String.format("Duplicate attribute ids found in attributes %s and %s", field1.getQualifiedFieldName(), field2.getQualifiedFieldName()));
        } else if (annotatedField.equals(field1.getFieldType())){
            return field1;
        } else {
            return field2;
        }
    }

    private void checkThatAllFieldsHaveGenerators(Map<String, DataGenerator> generators, Map<String, DataField> fields) {

        if (!generators.keySet().containsAll(fields.keySet())) {

            Set<String> attributeIds = fields.keySet();
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

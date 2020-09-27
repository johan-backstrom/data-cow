package com.github.johan.backstrom.corev2;

import com.github.johan.backstrom.corev2.exception.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.johan.backstrom.corev2.FieldCategory.annotatedField;
import static com.github.johan.backstrom.corev2.FieldCategory.namedField;

public class DataCow<T> {

    private T theDairy;
    private static Configuration staticConfig = new Configuration();
    private final Configuration config = staticConfig.getCopyOfConfiguration();
    private final Class<T> clazz;
    private Map<AttributeId, DataGenerator> generators = new HashMap<>();
    private Map<AttributeId, DataField> dataFields = new HashMap<>();

    private final Set<Class> withGenerators = new HashSet<>();
    private final Set<Generators> withGeneratorInstances = new HashSet<>();
    private final Set<AttributeId> handledFieldAttributeIds = new HashSet<>();

    private DataCow(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> DataCow<T> generateDairyFor(Class<T> clazz) {

        DataCow<T> theCow = new DataCow<>(clazz);
        try {
            theCow.theDairy = clazz.newInstance();
        } catch (Exception e) {
            throw new DairyInstantiationException(String.format("Class %s can not be instantiated, does it have a no args constructor?", clazz.getName()), e);
        }

        return theCow;
    }

    public static <T> DataCow<T> generateDairyForInstance(T object) {
        DataCow<T> theCow = new DataCow(object.getClass());
        theCow.theDairy = object;
        return theCow;
    }

    public static void withStaticConfiguration(DataCowConfiguration config) {
        config.getConfiguration(staticConfig);
    }

    public static void useDefaultConfiguration() {
        staticConfig = new Configuration();
    }

    public DataCow<T> with(With<T> with) {
        with.mutateObject(theDairy);
        return this;
    }

    public DataCow<T> withGenerator(Class clazz) {
        this.withGenerators.add(clazz);
        return this;
    }

    public DataCow<T> withGenerator(Generators generator) {
        this.withGeneratorInstances.add(generator);
        return this;
    }

    public DataCow<T> withConfiguration(DataCowConfiguration config) {
        config.getConfiguration(this.config);
        return this;
    }

    public T milkCow() {

        generators = collectGenerators();
        dataFields = collectFields();
        checkThatAllFieldsHaveGenerators(generators, dataFields);
        dataFields.values().stream().sorted().forEach(field -> milkFieldAndParentsRecursively(field));
        return theDairy;
    }

    private Map<AttributeId, DataGenerator> collectGenerators() {

        if (clazz.getAnnotation(WithGenerators.class) != null) {
            withGenerators.addAll(Arrays.asList(clazz.getAnnotation(WithGenerators.class).value()));
        }

        if (withGenerators.isEmpty() && withGeneratorInstances.isEmpty()) {
            throw new GeneratorNotFoundException("@WithGenerators was not specified in class " + clazz.getName());
        }

        Map<Class, Object> generatorInstanceObjects = collectGeneratorInstanceObjects();
        return collectDataGeneratorsFromGeneratorInstances(generatorInstanceObjects);
    }

    private Map<Class, Object> collectGeneratorInstanceObjects() {
        Map<Class, Object> generatorInstanceObjects = new HashMap<>();

        generatorInstanceObjects.putAll(withGeneratorInstances.stream()
                .collect(
                        Collectors.toMap(
                                generatorInstance -> generatorInstance.getClass(),
                                generatorInstance -> generatorInstance
                        )
                )
        );

        // Only instantiate generator classes that are not already passed as instances
        generatorInstanceObjects.putAll(withGenerators.stream()
                .filter(aClass -> !generatorInstanceObjects.containsKey(aClass))
                .collect(
                        Collectors.toMap(
                                aClass -> aClass,
                                aClass -> {
                                    try {
                                        return aClass.newInstance();
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        throw new GeneratorCouldNotBeInstantiatedException(
                                                String.format(
                                                        "Generator class %s could not be instantiated, does it have a public no-args constructor?",
                                                        aClass.getName()
                                                ),
                                                e
                                        );
                                    }
                                }
                        )
                )
        );

        return generatorInstanceObjects;
    }

    private Map<AttributeId, DataGenerator> collectDataGeneratorsFromGeneratorInstances(Map<Class, Object> generatorInstanceObjects) {
        Map<AttributeId, DataGenerator> generators = new HashMap<>();

        for (Class generatorClass : generatorInstanceObjects.keySet()) {
            Arrays.stream(generatorClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(Generator.class))
                    .map(method -> DataGenerator.builder()
                            .setAttributeId(new AttributeId(method.getDeclaredAnnotation(Generator.class).value()))
                            .setMethod(method)
                            .setObject(generatorInstanceObjects.get(generatorClass))
                            .build()
                    ).forEach(dataGenerator -> {
                        if (generators.containsKey(dataGenerator.getAttributeId())) {
                            throw new DuplicateAttributeException(
                                    String.format(
                                            "Duplicate attribute ids found in generators %s and %s",
                                            generators.get(dataGenerator.getAttributeId()).getQualifiedMethodName(),
                                            dataGenerator.getQualifiedMethodName()
                                    )
                            );
                        }
                        generators.put(dataGenerator.getAttributeId(), dataGenerator);
                    }
            );
        }

        return generators;
    }

    private Map<AttributeId, DataField> collectFields() {
        return getAllFields(theDairy.getClass()).stream()
                .filter(field -> field.isAnnotationPresent(Attribute.class) || config.useFieldByName())
                .map(field -> new DataField(
                                new AttributeId(
                                        field.isAnnotationPresent(Attribute.class)
                                                ? field.getAnnotationsByType(Attribute.class)[0].value()
                                                : field.getName()
                                ),
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

    private List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            fields.addAll(getAllFields(superClass));
        }
        return fields;
    }

    private DataField getFieldWithHighestPrio(DataField field1, DataField field2) {
        if (field1.getFieldCategory().equals(field2.getFieldCategory())) {
            throw new DuplicateAttributeException(String.format("Duplicate attribute ids found in attributes %s and %s", field1.getQualifiedFieldName(), field2.getQualifiedFieldName()));
        } else if (annotatedField.equals(field1.getFieldCategory())) {
            return field1;
        } else {
            return field2;
        }
    }

    private void checkThatAllFieldsHaveGenerators(Map<AttributeId, DataGenerator> generators, Map<AttributeId, DataField> fields) {

        fields.forEach(
                (attributeId, dataField) -> {
                    if (!generators.containsKey(attributeId)) {
                        if (!config.failOnBoundaryValuesWithMissingGenerators() && dataField.getFieldValue() != null) {
                            return;
                        } else if (!config.failOnMissingGenerators()) {
                            return;
                        } else {
                            throw new GeneratorNotFoundException(
                                    String.format(
                                            "No generator was found for the following field: %s",
                                            dataField.getAttributeId()
                                    )
                            );
                        }
                    }
                }
        );
    }

    private Object milkFieldAndParentsRecursively(DataField field) {

        if (handledFieldAttributeIds.contains(field.getAttributeId())) {
            // We've already been here. Don't do anything but return the value (which can be null)!
            return field.getFieldValue();
        }

        handledFieldAttributeIds.add(field.getAttributeId());

        if (
                field.getFieldValue() != null &&
                        !config.overwriteAttributesWithValues() &&
                        !config.shouldAttributeBeOverwritten(field.getAttributeId())) {
            // If the field has a value already, don't create a new one
            return field.getFieldValue();
        }

        DataGenerator generator = generators.get(field.getAttributeId());

        if (generator == null) {
            return null;
        } else {
            Object[] parameterValues = generator.getParentFieldAttributeIds().stream()
                    .map(attributeId -> Optional.ofNullable(dataFields.get(attributeId))
                            .orElseThrow(
                                    () -> new UnknownGeneratorReferenceException(String.format("Attribute %s referenced from a generator could not be found", attributeId))
                            )
                    )
                    .map(parentField -> milkFieldAndParentsRecursively(parentField))
                    .collect(Collectors.toList())
                    .toArray();

            field.setFieldValue(generator.invokeGenerator(parameterValues));

            return field.getFieldValue();
        }
    }
}

package com.github.johan.backstrom.common.corev2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DataCow<T> {

    T theDairy;
    Map<String, Method> generators = new HashMap<>();


    public static <T> DataCow<T> generateDairyFor(Class<T> clazz) {
        try {
            DataCow<T> theCow = new DataCow<>();
            theCow.theDairy = clazz.newInstance();
            Arrays.stream(clazz.getAnnotation(WithGenerators.class).value())
                    .map(scannedClass -> Arrays.asList(scannedClass.getDeclaredMethods()))
                    .reduce((methods, methods2) -> {
                                List<Method> aggregatedMethods = new ArrayList<>();
                                aggregatedMethods.addAll(methods);
                                aggregatedMethods.addAll(methods2);
                                return aggregatedMethods;
                            }
                    ).orElseThrow(RuntimeException::new).forEach(method -> {
                        String annotationValue = method.getAnnotation(Generator.class).value();
                        theCow.generators.put(
                                "".equals(annotationValue) ? method.getName() : annotationValue,
                                method
                        );
                    }
            );

            return theCow;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataCow<T> with(With<T> with) {
        with.with(theDairy);
        return this;
    }

    public T milkCow() {

        Set<Field> handlesFields = new HashSet<>();
        Map<Field, Method> fieldGenerator = new HashMap<>();

        Arrays.stream(theDairy.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Attribute.class))
                .forEach(field -> {
                    milkField(handlesFields, field);
                });

        return theDairy;
    }

    public void milkField(Set<Field> handledFields, Field field) {
        Optional<Method> generator = Optional.ofNullable(
                generators.get(field.getAnnotation(Attribute.class).value())
        );

        Arrays.stream(generator
                .orElseThrow(RuntimeException::new)
                .getParameters()
        ).forEach(
                parameter -> {
                    if (parameter.getAnnotation(References.class) != null) {
                        Optional.ofNullable(
                                generators.get(parameter.getAnnotation(References.class).value())
                        ).orElseThrow(RuntimeException::new)

                    }
                }
        );
    }
}

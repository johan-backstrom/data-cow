package com.github.johan.backstrom.common.corev2;

import com.github.johan.backstrom.common.corev2.exception.MethodNotExecutableException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataGenerator {

    private final Method method;
    private final Object object;
    private final String attributeId;

    private DataGenerator(String attributeId, Object object, Method method){
        this.attributeId = attributeId;
        this.object = object;
        this.method = method;
    }

    public Object invokeGenerator(Object[] parameters){
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodNotExecutableException(String.format("The method %s cannot be executed", getQualifiedMethodName()));
        }
    }

    public String getAttributeId(){
        return attributeId;
    }

    public String getQualifiedMethodName(){
        return String.format("%s.%s", method.getDeclaringClass(), method.getName());
    }

    public List<String> getParentFieldAttributeIds(){
        // Get its dependencies (TODO: Check by Annotation, type and name, type)
        return Arrays.stream(method.getParameters())
                .map(
                        parameter -> {
                            if (parameter.getAnnotation(References.class) != null) {
                                return Optional.ofNullable(parameter.getAnnotation(References.class).value())
                                        .orElseThrow(RuntimeException::new);
                            } else {
                                throw new RuntimeException("Parameter has no reference");
                            }
                        }
                ).collect(Collectors.toList());
    }

    public static MethodBuilder builder(){
        return new MethodBuilder();
    }

    public static class MethodBuilder {

        private Method method;
        private Object object;
        private String attributeId;

        public MethodBuilder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public MethodBuilder setObject(Object object) {
            this.object = object;
            return this;
        }

        public MethodBuilder setAttributeId(String attributeId) {
            this.attributeId = attributeId;
            return this;
        }

        public DataGenerator build(){
            return new DataGenerator(attributeId, object, method);
        }
    }

}

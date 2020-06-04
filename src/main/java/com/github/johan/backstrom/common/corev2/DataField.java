package com.github.johan.backstrom.common.corev2;

import com.github.johan.backstrom.common.corev2.exception.FieldNotAccessibleException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class DataField {

    private final Field field;
    private final Object object;
    private final String attributeId;
    private final Optional<Method> setterMethod;
    private final Optional<Method> getterMethod;

    public DataField(String attributeId, Object object, Field field){
        this.attributeId = attributeId;
        this.object = object;
        this.field = field;

        this.setterMethod = getSetterMethod(object, field);
        this.getterMethod = getGetterMethod(object, field);
    }

    public void setFieldValue(Object value){
        try {

            if (setterMethod.isPresent()){
                setterMethod.get().invoke(object, value);
            } else if (Modifier.isPublic(field.getModifiers())){
                field.set(object, value);
            } else {
                throw new Exception("There is no setter and the field is not accessible");
            }
        } catch (Exception e) {
            throw new FieldNotAccessibleException(String.format("This field %s cannot be modified", field.getName()));
        }
    }

    public Object getFieldValue(){
        try {
            if (getterMethod.isPresent()){
                return getterMethod.get().invoke(object);
            } else if (Modifier.isPublic(field.getModifiers())){
                return field.get(object);
            } else {
                throw new Exception("There is no getter and the field is not accessible");
            }
        } catch (Exception e) {
            throw new FieldNotAccessibleException(String.format("This field %s cannot be accessed, either directly or through a public getter", field.getName()));
        }
    }

    private Optional<Method> getSetterMethod(Object object, Field field){
        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.getName().matches(getRegexForSetter(field)))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
    }

    private String getRegexForSetter(Field field){
        String startingLetter = field.getName().substring(0,1);
        String ending = field.getName().substring(1);
        return "^set(" + startingLetter.toLowerCase() + "|" + startingLetter.toUpperCase() + ")" + ending + "$";
    }

    private Optional<Method> getGetterMethod(Object object, Field field){
        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.getName().matches(getRegexForGetter(field)))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
    }

    private String getRegexForGetter(Field field){
        String startingLetter = field.getName().substring(0,1);
        String ending = field.getName().substring(1);
        return "^get(" + startingLetter.toLowerCase() + "|" + startingLetter.toUpperCase() + ")" + ending + "$";
    }


    public String getAttributeId(){
        return attributeId;
    }

    public String getQualifiedFieldName(){
        return String.format("%s.%s", field.getDeclaringClass(), field.getName());
    }
}
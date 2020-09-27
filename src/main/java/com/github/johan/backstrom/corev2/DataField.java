package com.github.johan.backstrom.corev2;

import com.github.johan.backstrom.corev2.exception.FieldNotAccessableException;
import com.github.johan.backstrom.corev2.exception.FieldNotModifiableException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class DataField implements Comparable<DataField>{

    private final Field field;
    private final Object object;
    private final AttributeId attributeId;
    private final FieldCategory fieldCategory;
    private final Optional<Method> setterMethod;
    private final Optional<Method> getterMethod;

    public DataField(AttributeId attributeId, Object object, Field field, FieldCategory fieldCategory){
        this.attributeId = attributeId;
        this.object = object;
        this.field = field;
        this.fieldCategory = fieldCategory;

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
            throw new FieldNotModifiableException(String.format("This field %s cannot be modified", field.getName()), e);
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
            throw new FieldNotAccessableException(String.format("This field %s cannot be accessed, either directly or through a public getter", field.getName()), e);
        }
    }

    public AttributeId getAttributeId(){
        return attributeId;
    }

    public FieldCategory getFieldCategory(){
        return fieldCategory;
    }

    protected Class getFieldType(){
        return field.getType();
    }

    public String getQualifiedFieldName(){
        return String.format("%s.%s", field.getDeclaringClass(), field.getName());
    }

    private Optional<Method> getSetterMethod(Object object, Field field){
        return Arrays.stream(object.getClass().getMethods())
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
        return Arrays.stream(object.getClass().getMethods())
                .filter(method -> method.getName().matches(getRegexForGetter(field)))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
    }

    private String getRegexForGetter(Field field){
        String startingLetter = field.getName().substring(0,1);
        String ending = field.getName().substring(1);
        return "^get(" + startingLetter.toLowerCase() + "|" + startingLetter.toUpperCase() + ")" + ending + "$";
    }

    @Override
    public int compareTo(DataField dataField) {
        String attributeIdToCompareTo = dataField.getAttributeId().getAttributeId();
        return attributeId.getAttributeId().compareTo(attributeIdToCompareTo);
    }
}
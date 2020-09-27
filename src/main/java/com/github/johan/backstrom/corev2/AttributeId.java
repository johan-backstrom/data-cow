package com.github.johan.backstrom.corev2;

import java.util.Objects;

public class AttributeId {
    private String attributeId;

    protected String getAttributeId(){
        return attributeId;
    };

    protected AttributeId(String attributeId){
        this.attributeId = attributeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeId that = (AttributeId) o;
        return Objects.equals(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeId);
    }
}

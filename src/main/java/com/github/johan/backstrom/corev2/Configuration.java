package com.github.johan.backstrom.corev2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Configuration{

    private boolean useFieldByName = false;
    private boolean overwriteAllAttributes = false;
    private boolean failOnMissingGenerators = true;
    private boolean failOnBoundaryValuesWithMissingGenerators = true;
    private Set<AttributeId> attributeIdsToOverwrite = new HashSet<>();

    public Configuration useVariableNamesAsAttributeId() {
        this.useFieldByName = true;
        return this;
    }

    public Configuration overwriteAllAttributes() {
        this.overwriteAllAttributes = true;
        return this;
    }

    public Configuration overwriteAttribute(String... attributeId){
        attributeIdsToOverwrite.addAll(
                Arrays.stream(attributeId).map(s -> new AttributeId(s)).collect(Collectors.toList())
        );
        return this;
    }

    public Configuration doNotFailOnMissingGenerators() {
        this.failOnMissingGenerators = false;
        return this;
    }

    public Configuration doNotFailOnMissingGeneratorsForBoundaryValue() {
        this.failOnBoundaryValuesWithMissingGenerators = false;
        return this;
    }

    protected boolean useFieldByName() {
        return useFieldByName;
    }

    protected boolean overwriteAttributesWithValues() {
        return overwriteAllAttributes;
    }

    protected boolean failOnMissingGenerators(){
        return failOnMissingGenerators;
    }

    protected boolean failOnBoundaryValuesWithMissingGenerators(){
        return failOnBoundaryValuesWithMissingGenerators;
    }

    protected boolean shouldAttributeBeOverwritten(AttributeId attributeId){
        return attributeIdsToOverwrite.contains(attributeId);
    }

    protected Configuration getCopyOfConfiguration(){
        return new Configuration(useFieldByName, overwriteAllAttributes, failOnMissingGenerators, failOnBoundaryValuesWithMissingGenerators, attributeIdsToOverwrite);
    }

    protected Configuration(){
    }

    private Configuration(boolean useFieldByName, boolean overwriteAllAttributes, boolean failOnMissingGenerators, boolean failOnBoundaryValuesWithMissingGenerators, Set<AttributeId> attributeIdsToOverwrite) {
        this.useFieldByName = useFieldByName;
        this.overwriteAllAttributes = overwriteAllAttributes;
        this.failOnMissingGenerators = failOnMissingGenerators;
        this.failOnBoundaryValuesWithMissingGenerators = failOnBoundaryValuesWithMissingGenerators;

        this.attributeIdsToOverwrite = attributeIdsToOverwrite.stream()
                .map(a -> new AttributeId(a.getAttributeId()))
                .collect(Collectors.toSet());
    }
}

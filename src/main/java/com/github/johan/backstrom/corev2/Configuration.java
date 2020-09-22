package com.github.johan.backstrom.corev2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Configuration{

    private boolean useFieldByName = false;
    private boolean overwriteAllAttributes = false;
    private boolean failOnMissingGenerators = true;
    private boolean failOnBoundaryValuesWithMissingGenerators = true;
    private Set<String> attributeIdsToOverwrite = new HashSet<>();

    public Configuration useVariableNamesAsAttributeId() {
        this.useFieldByName = true;
        return this;
    }

    public Configuration overwriteAllAttributes() {
        this.overwriteAllAttributes = true;
        return this;
    }

    public Configuration overwriteAttribute(String... attributeId){
        attributeIdsToOverwrite.addAll(Arrays.asList(attributeId));
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

    protected boolean shouldAttributeBeOverwritten(String attributeId){
        return attributeIdsToOverwrite.contains(attributeId);
    }

    protected Configuration getCopyOfConfiguration(){
        return new Configuration(useFieldByName, overwriteAllAttributes, failOnMissingGenerators, failOnBoundaryValuesWithMissingGenerators);
    }

    protected Configuration(){
    }

    private Configuration(boolean useFieldByName, boolean overwriteAllAttributes, boolean failOnMissingGenerators, boolean failOnBoundaryValuesWithMissingGenerators) {
        this.useFieldByName = useFieldByName;
        this.overwriteAllAttributes = overwriteAllAttributes;
        this.failOnMissingGenerators = failOnMissingGenerators;
        this.failOnBoundaryValuesWithMissingGenerators = failOnBoundaryValuesWithMissingGenerators;
    }
}

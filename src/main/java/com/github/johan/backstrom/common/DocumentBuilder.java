package com.github.johan.backstrom.common;

import com.github.johan.backstrom.common.core.Attribute;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentBuilder {

    private DirectedGraph<Attribute, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

    public DocumentBuilder addAttribute(Attribute attribute) {
        graph.addVertex(attribute);
        return this;
    }

    public DocumentBuilder addDependency(Attribute attribute, Attribute parentAttribute) {
        graph.addEdge(parentAttribute, attribute);
        return this;
    }

    public DocumentBuilder buildDataForEmptyAttributes() {
        Set<Attribute> attributes = new HashSet<>();
        graph.vertexSet().forEach(
                attribute -> setParamsForAttribute(attribute, attributes)
        );
        return this;
    }

    public String toString() {
        Optional<String> theString = graph
                .vertexSet()
                .stream()
                .map(vertex -> vertex.getName().concat(": ").concat(vertex.getValue().toString()))
                .reduce((string1, string2) -> string1.concat("\n").concat(string2));

        return theString.isPresent() ? theString.get() : "";
    }

    public Map<String, Object> toMap() {
        return graph
                .vertexSet()
                .stream()
                .collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
    }

    // Takes a set of attributes that should not be traversed
    private void setParamsForAttribute(Attribute attribute, Set<Attribute> handledAttributes) {

        // Parameters to pass to current attribute when generating the data
        Map<String, Object> params = new HashMap<>();

        if (!handledAttributes.contains(attribute)) {

            // Add attribute to collection to not iterate over it again
            handledAttributes.add(attribute);

            // Make recursive generateData to populate all verticies that affect the current one
            graph.incomingEdgesOf(attribute).forEach(
                    dependency -> {
                        Attribute sourceAttribute = graph.getEdgeSource(dependency);
                        if (sourceAttribute != null) {
                            setParamsForAttribute(sourceAttribute, handledAttributes);
                        }
                        params.put(sourceAttribute.getName(), sourceAttribute);
                    }
            );

            if (attribute.getValue() == null) {
                attribute.generateAttributeData(params);
            }
        }

        if (!attribute.validateAttributeData(params)) {
            Optional<String> dependants = params.keySet().stream().reduce((string1, string2) -> string1.concat(", " + string2));
            throw new RuntimeException("Inconsistency in data validation for attribute: "
                    + attribute.getName()
                    + " which depends on: "
                    + (dependants.isPresent() ? dependants.get() : ""));
        }
    }
}

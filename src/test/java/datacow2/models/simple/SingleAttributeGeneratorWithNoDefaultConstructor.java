package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.GeneratorWithoutDefaultConstructor;
import datacow2.generators.Generators;

@WithGenerators(GeneratorWithoutDefaultConstructor.class)
public class SingleAttributeGeneratorWithNoDefaultConstructor {

    @Attribute("aSingleValue")
    public String aSingleValue;

}

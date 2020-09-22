package datacow2.models.annotation;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class GeneratorWithWrongType {

    @Attribute("aSingleValue")
    public Integer aSingleValue;

}

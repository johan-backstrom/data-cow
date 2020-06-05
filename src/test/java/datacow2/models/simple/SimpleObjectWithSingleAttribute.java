package datacow2.models.simple;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class SimpleObjectWithSingleAttribute {

    @Attribute("aSingleValue")
    public String aSingleValue;

}

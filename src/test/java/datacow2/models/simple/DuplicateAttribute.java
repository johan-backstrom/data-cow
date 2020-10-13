package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class DuplicateAttribute {

    @Attribute("aSingleValue")
    public String aSingleValue;

    @Attribute("aSingleValue")
    public String aSingleValue2;

}

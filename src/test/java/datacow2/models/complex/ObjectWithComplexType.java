package datacow2.models.complex;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;
import datacow2.models.simple.MultipleAttributes;

@WithGenerators(Generators.class)
public class ObjectWithComplexType {

    @Attribute("complexType")
    public MultipleAttributes compleObject;

    @Attribute("aSingleValue")
    public String aSingleValue;
}

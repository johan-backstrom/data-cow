package datacow2.models.complex;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.Generators;
import datacow2.models.simple.SimpleObjectWithMultipleAttributes;

@WithGenerators(Generators.class)
public class ObjectWithComplexType {

    @Attribute("complexType")
    public SimpleObjectWithMultipleAttributes compleObject;

    @Attribute("aSingleValue")
    public String aSingleValue;
}

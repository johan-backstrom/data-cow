package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class ObjectWithNullValue {

    @Attribute("aSingleValue")
    public String aSingleValue;

    @Attribute("nullValue")
    public String nullValue;

    @Attribute("primitiveNull")
    public int nullPrimitive;

}

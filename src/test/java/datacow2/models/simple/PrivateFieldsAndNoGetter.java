package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class PrivateFieldsAndNoGetter {

    @Attribute("aSingleValue")
    private String aSingleValue;

    @Attribute("aString")
    private String aString;

}

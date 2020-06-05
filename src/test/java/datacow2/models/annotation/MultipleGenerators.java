package datacow2.models.annotation;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.generators.Generators2;
import datacow2.generators.Generators;

@WithGenerators({Generators.class, Generators2.class})
public class MultipleGenerators {

    @Attribute("aSingleValue")
    public String singleValue;

    @Attribute("aSingleValue2")
    public String singleValue2;

}

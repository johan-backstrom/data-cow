package datacow2.models.annotation;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.generators.GeneratorWithDuplicate;
import datacow2.generators.Generators;

@WithGenerators({Generators.class, GeneratorWithDuplicate.class})
public class SimpleObjectWithDupliacteGenerators {

    @Attribute("aSingleValue")
    public String aSingleValue;

}

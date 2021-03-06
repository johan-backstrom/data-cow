package datacow2.models.annotation;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class GeneratorNotFound {

    @Attribute("notFound")
    public String notFound;

    @Attribute("referencesNotFound")
    public String aValue;

}

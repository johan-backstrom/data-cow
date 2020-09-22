package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class ObjectWithTransitiveDependencies {

    @Attribute("parentAttribute")
    public String parent;

    @Attribute("childAttribute")
    public String parent2;

    @Attribute("childWithTransitiveDependency")
    public String child;

}

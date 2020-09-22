package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class ObjectWithMultipleDependencies {

    @Attribute("parentAttribute")
    public String parent;

    @Attribute("secondParentAttribute")
    public String parent2;

    @Attribute("childWithTwoParents")
    public String child;

}

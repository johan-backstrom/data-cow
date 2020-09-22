package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class ObjectWithDependency {

    @Attribute("parentAttribute")
    public String parent;

    @Attribute("childAttribute")
    public String child;

}

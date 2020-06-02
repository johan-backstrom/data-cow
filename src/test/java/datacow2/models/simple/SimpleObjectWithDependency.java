package datacow2.models.simple;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.Generators;

@WithGenerators(Generators.class)
public class SimpleObjectWithDependency {

    @Attribute("parentAttribute")
    public String parent;

    @Attribute("childAttribute")
    public String child;

}

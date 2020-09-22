package datacow2.models.simple;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class CirkularDependency {

    @Attribute("cirkular1")
    public String cirkular1;

    @Attribute("cirkular2")
    public String cirkular2;

    @Attribute("cirkular3")
    public String cirkular3;

}

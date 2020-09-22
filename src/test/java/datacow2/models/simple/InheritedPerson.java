package datacow2.models.simple;

import com.github.johan.backstrom.corev2.WithGenerators;
import com.github.johan.backstrom.corev2.entities.person.Person;
import datacow2.generators.InheritedPersonGenerators;

@WithGenerators(InheritedPersonGenerators.class)
public class InheritedPerson extends Person {
}

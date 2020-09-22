package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.References;
import com.github.johan.backstrom.corev2.entities.person.PersonGenerators;
import com.github.johan.backstrom.corev2.entities.person.Sex;

public class InheritedPersonGenerators extends PersonGenerators {

    @Override
    @Generator("givenName")
    public String getGivenName(@References("sex") Sex sex) {
        return "Chris";
    }
}
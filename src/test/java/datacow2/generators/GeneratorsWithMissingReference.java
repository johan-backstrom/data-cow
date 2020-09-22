package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.References;

public class GeneratorsWithMissingReference implements com.github.johan.backstrom.corev2.Generators {

    @Generator("aSingleValue")
    public String aSingleValue(@References("somethingNonExistent") String anArgument){
        return "SimpleGeneratedValue: " + anArgument;
    }
}

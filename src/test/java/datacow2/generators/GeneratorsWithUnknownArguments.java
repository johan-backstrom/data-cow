package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;

public class GeneratorsWithUnknownArguments implements com.github.johan.backstrom.corev2.Generators {

    @Generator("aSingleValue")
    public String aSingleValue(String anArgument){
        return "SimpleGeneratedValue: " + anArgument;
    }
}

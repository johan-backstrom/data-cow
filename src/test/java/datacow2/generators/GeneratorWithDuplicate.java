package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;

public class GeneratorWithDuplicate {

    @Generator("aSingleValue")
    public String aSingleValue(){
        return "SimpleGeneratedValue";
    }

}

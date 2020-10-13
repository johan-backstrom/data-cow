package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;

public class GeneratorWithoutDefaultConstructor {

    public GeneratorWithoutDefaultConstructor(String something){
    }

    @Generator("aSingleValue")
    public String aString(){
        return "aString";
    }

}

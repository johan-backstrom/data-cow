package datacow2.generators;

import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.Generators;

public class GeneratorWithoutDefaultConstructor implements Generators {

    public GeneratorWithoutDefaultConstructor(String something){
    }

    @Generator("aSingleValue")
    public String aString(){
        return "aString";
    }

}

package datacow2;

import com.github.johan.backstrom.common.corev2.Generator;
import com.github.johan.backstrom.common.corev2.References;

public class TheBuilder {

    @Generator
    public static String apa(){
        return "bepa";
    }

    @Generator("apa2")
    public static String depa(int i){
        return "bepa";
    }

    @Generator
    public static String somethingElse(
             @References("apa") String apa
    ){
        return apa + "depa";
    }
}
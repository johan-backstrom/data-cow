package datacow2.models.annotation;

import com.github.johan.backstrom.common.corev2.Attribute;

public class NoGeneratorSpecifiedWithMultipleGenerators {

    @Attribute("aString")
    public String aString;

    @Attribute("aSingleValue2")
    public String anotherValue;
}

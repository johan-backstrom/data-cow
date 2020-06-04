package datacow2.models.simple;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.generators.GeneratorWithDuplicate;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class SimpleObjectWithGetterAndSetter {

    @Attribute("aSingleValue")
    private String aSingleValue;

    @Attribute("aString")
    private String aString;

    public String getaSingleValue() {
        return aSingleValue;
    }

    public String getASingleValue() {
        return aSingleValue;
    }

    public void setaSingleValue(String aSingleValue) {
        this.aSingleValue = aSingleValue;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }
}

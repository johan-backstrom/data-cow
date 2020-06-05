package datacow2.models.annotation;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;
import datacow2.generators.Generators;

@WithGenerators(Generators.class)
public class SimpleObjectWithDuplicateAttributeOneIsAnnotated {

    @Attribute("aString")
    private String aSingleValue;

    public String aString;

    public String getaSingleValue() {
        return aSingleValue;
    }

    public void setaSingleValue(String aSingleValue) {
        this.aSingleValue = aSingleValue;
    }
}

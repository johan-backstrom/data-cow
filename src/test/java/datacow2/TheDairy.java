package datacow2;

import com.github.johan.backstrom.common.corev2.Attribute;
import com.github.johan.backstrom.common.corev2.WithGenerators;

@WithGenerators(TheBuilder.class)
public class TheDairy {

    @Attribute("apa")
    private String apa;

    @Attribute("apa2")
    private String depa;

    @Attribute
    private String bepa;

    public String getApa() {
        return apa;
    }

    public void setApa(String apa) {
        this.apa = apa;
    }

    public String getDepa() {
        return depa;
    }

    public void setDepa(String depa) {
        this.depa = depa;
    }

    public String getBepa() {
        return bepa;
    }

    public void setBepa(String bepa) {
        this.bepa = bepa;
    }
}
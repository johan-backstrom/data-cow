package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import org.junit.Test;

public class DataCow2Test {

    @Test
    public void something(){

        TheDairy myMilk = DataCow.generateDairyFor(TheDairy.class)
                .with(o -> o.setApa("Something"))
                .milkCow();

    }
}

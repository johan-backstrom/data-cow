package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import datacow2.models.complex.ObjectWithComplexType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexType {

    @Test
    public void objectWithComplexTypeMember(){
        ObjectWithComplexType o = DataCow.generateDairyFor(ObjectWithComplexType.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
        assertEquals("aString", o.compleObject.aString);
    }

}

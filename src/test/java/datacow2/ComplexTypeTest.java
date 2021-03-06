package datacow2;

import com.github.johan.backstrom.corev2.DataCow;
import datacow2.models.complex.ObjectWithComplexType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexTypeTest {

    @Test
    public void objectWithComplexTypeMember(){
        ObjectWithComplexType o = DataCow.generateDairyFor(ObjectWithComplexType.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
        assertEquals("aString", o.compleObject.aString);
    }

}

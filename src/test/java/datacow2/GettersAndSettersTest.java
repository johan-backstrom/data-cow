package datacow2;

import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.exception.FieldNotAccessableException;
import datacow2.models.simple.GetterAndSetter;
import datacow2.models.simple.PrivateFieldsAndNoGetter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GettersAndSettersTest {

    @Test(expected = FieldNotAccessableException.class)
    public void privateFieldWithNoGetterAndSetter(){

        PrivateFieldsAndNoGetter o = DataCow.generateDairyFor(PrivateFieldsAndNoGetter.class)
                .milkCow();

    }

    @Test
    public void getterAndSetter(){
        GetterAndSetter o = DataCow.generateDairyFor(GetterAndSetter.class)
            .milkCow();

        assertEquals("aString", o.getaString());
    }
}

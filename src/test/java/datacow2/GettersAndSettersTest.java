package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import com.github.johan.backstrom.common.corev2.exception.FieldNotAccessibleException;
import datacow2.models.simple.SimpleObjectWithGetterAndSetter;
import datacow2.models.simple.SimpleObjectWithPrivateFieldsAndNoGetter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GettersAndSettersTest {

    @Test(expected = FieldNotAccessibleException.class)
    public void privateFieldWithNoGetterAndSetter(){

        SimpleObjectWithPrivateFieldsAndNoGetter o = DataCow.generateDairyFor(SimpleObjectWithPrivateFieldsAndNoGetter.class)
                .milkCow();

    }

    @Test
    public void getterAndSetter(){
        SimpleObjectWithGetterAndSetter o = DataCow.generateDairyFor(SimpleObjectWithGetterAndSetter.class)
            .milkCow();

        assertEquals("aString", o.getaString());
    }
}

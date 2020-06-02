package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import datacow2.models.simple.SimpleObjectWithMultipleAttributes;
import datacow2.models.simple.SimpleObjectWithSingleAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleValueGeneration {

    @Test
    public void createSingleValueInSimpleObject(){

        SimpleObjectWithSingleAttribute o = DataCow.generateDairyFor(SimpleObjectWithSingleAttribute.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
    }

    @Test
    public void createMultipleValuesInSimpleObject(){
        SimpleObjectWithMultipleAttributes o = DataCow.generateDairyFor(SimpleObjectWithMultipleAttributes.class)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(false, o.aBoolean);
        assertEquals(new Integer(666), o.anInteger);
    }

    @Test
    public void valueAlreadySet(){
        SimpleObjectWithMultipleAttributes o = DataCow.generateDairyFor(SimpleObjectWithMultipleAttributes.class)
                .with(o1 -> o1.anInteger = 777)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(new Integer(777), o.anInteger);
    }

    @Test
    public void integerValueAlreadySet(){
        SimpleObjectWithMultipleAttributes o = DataCow.generateDairyFor(SimpleObjectWithMultipleAttributes.class)
                .with(o1 -> o1.anInteger = 777)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(new Integer(777), o.anInteger);
    }

    @Test
    public void primitiveTypeNotSet(){
        SimpleObjectWithMultipleAttributes o = DataCow.generateDairyFor(SimpleObjectWithMultipleAttributes.class)
                .milkCow();

        assertEquals(123, o.aPrimitiveType);
    }
}

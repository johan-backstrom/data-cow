package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import com.github.johan.backstrom.common.corev2.exception.GeneratorNotFoundException;
import datacow2.models.annotation.GeneratorNotFound;
import datacow2.models.annotation.NoGeneratorSpecified;
import datacow2.models.simple.SimpleObjectWithMultipleAttributes;
import datacow2.models.simple.SimpleObjectWithNullValue;
import datacow2.models.simple.SimpleObjectWithSingleAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleValueGenerationTest {

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

    @Test
    public void generateNullValue(){
        SimpleObjectWithNullValue o = DataCow.generateDairyFor(SimpleObjectWithNullValue.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
        assertEquals(null, o.nullValue);
    }

    @Test(expected = GeneratorNotFoundException.class)
    public void generatorNotFound(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class).milkCow();
    }

    @Test(expected = GeneratorNotFoundException.class)
    public void noGeneratorClassDefined(){
        NoGeneratorSpecified o = DataCow.generateDairyFor(NoGeneratorSpecified.class).milkCow();
    }
}

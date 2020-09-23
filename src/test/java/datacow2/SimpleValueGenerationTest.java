package datacow2;

import com.github.johan.backstrom.corev2.Configuration;
import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.exception.GeneratorNotFoundException;
import datacow2.models.annotation.GeneratorNotFound;
import datacow2.models.annotation.NoGeneratorSpecified;
import datacow2.models.simple.MultipleAttributes;
import datacow2.models.simple.ObjectWithNullValue;
import datacow2.models.simple.SingleAttribute;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleValueGenerationTest {

    @Test
    public void createSingleValueInSimpleObject(){

        SingleAttribute o = DataCow.generateDairyFor(SingleAttribute.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
    }

    @Test
    public void createMultipleValuesInSimpleObject(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(false, o.aBoolean);
        assertEquals(new Integer(666), o.anInteger);
    }

    @Test
    public void valueAlreadySet(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .with(o1 -> o1.anInteger = 777)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(new Integer(777), o.anInteger);
    }

    @Test
    public void integerValueAlreadySet(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .with(o1 -> o1.anInteger = 777)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals(new Integer(777), o.anInteger);
    }

    @Test
    public void generateNullValue(){
        ObjectWithNullValue o = DataCow.generateDairyFor(ObjectWithNullValue.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
        assertEquals(null, o.nullValue);
    }


    @Test @Ignore
    public void generateNullValueForPrimitive(){
        ObjectWithNullValue o = DataCow.generateDairyFor(ObjectWithNullValue.class)
                .milkCow();

        assertEquals(null, o.nullPrimitive);
    }

    @Test(expected = GeneratorNotFoundException.class)
    public void generatorNotFound(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class).milkCow();
    }

    @Test
    public void generatorNotFoundIgnored(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class)
                .withConfiguration(Configuration::doNotFailOnMissingGenerators)
                .milkCow();

        assertNull(o.notFound);
    }

    @Test
    public void generatorNotFoundButBoundaryValueSet(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class)
                .withConfiguration(Configuration::doNotFailOnMissingGeneratorsForBoundaryValue)
                .with(o1 -> o1.notFound = "boundaryValue")
                .milkCow();

        assertEquals("boundaryValue", o.notFound);
    }

    @Test(expected = GeneratorNotFoundException.class)
    public void noGeneratorClassDefined(){
        NoGeneratorSpecified o = DataCow.generateDairyFor(NoGeneratorSpecified.class).milkCow();
    }
}

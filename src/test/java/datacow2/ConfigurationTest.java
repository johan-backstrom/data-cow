package datacow2;

import com.github.johan.backstrom.corev2.Configuration;
import com.github.johan.backstrom.corev2.DataCow;
import datacow2.generators.Generators;
import datacow2.models.annotation.SimpleObjectWithDuplicateAttributeOneIsAnnotated;
import datacow2.models.annotation.SimpleObjectWithoutAnnotations;
import datacow2.models.simple.MultipleAttributes;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConfigurationTest {

    @Test
    public void getFieldsAlsoByName(){
        SimpleObjectWithoutAnnotations o = DataCow.generateDairyFor(SimpleObjectWithoutAnnotations.class)
                .withGenerator(Generators.class)
                .withConfiguration(config -> config.useVariableNamesAsAttributeId())
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
    }

    @Test
    public void getFieldsAlsoByNameWithDuplicateNameShouldPrioritizeAnnotations(){
        SimpleObjectWithDuplicateAttributeOneIsAnnotated o = DataCow.generateDairyFor(SimpleObjectWithDuplicateAttributeOneIsAnnotated.class)
                .withConfiguration(Configuration::useVariableNamesAsAttributeId)
                .milkCow();

        assertEquals("aString", o.getaSingleValue());
        assertNull(o.aString);
    }

    @Test
    public void overWriteAllAttributesWithPrimitiveType(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .withConfiguration(Configuration::overwriteAllAttributes)
                .milkCow();

        assertEquals(123, o.aPrimitiveType);
    }

    @Test
    public void staticConfiguration(){

        DataCow.withStaticConfiguration(Configuration::overwriteAllAttributes);

        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .milkCow();

        assertEquals(123, o.aPrimitiveType);
        DataCow.useDefaultConfiguration();
    }

    @Test
    public void resetStaticConfiguration(){

        DataCow.withStaticConfiguration(Configuration::overwriteAllAttributes);
        DataCow.useDefaultConfiguration();

        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .milkCow();

        // Since int is initialized to 0 it won't be overwritten
        assertEquals(0, o.aPrimitiveType);
    }

    @Test
    public void overwriteSpecificAttribute(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .withConfiguration(configuration -> configuration.overwriteAttribute("aPrimitiveType"))
                .milkCow();

        // Since int is initialized to 0 it won't be overwritten
        assertEquals(123, o.aPrimitiveType);
        assertEquals(0, o.aPrimitiveType2);
    }

    @Test
    public void overwriteArrayOfAttributes(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .withConfiguration(configuration -> configuration.overwriteAttribute(new String[]{"aPrimitiveType", "aPrimitiveType2"}))
                .milkCow();

        // Since int is initialized to 0 it won't be overwritten
        assertEquals(123, o.aPrimitiveType);
        assertEquals(123, o.aPrimitiveType2);
    }

    @Test
    public void overwriteMultipleOfAttributes(){
        MultipleAttributes o = DataCow.generateDairyFor(MultipleAttributes.class)
                .withConfiguration(configuration -> configuration.overwriteAttribute("aPrimitiveType"))
                .withConfiguration(configuration -> configuration.overwriteAttribute("aPrimitiveType2"))
                .milkCow();

        // Since int is initialized to 0 it won't be overwritten
        assertEquals(123, o.aPrimitiveType);
        assertEquals(123, o.aPrimitiveType2);
    }

    @Test
    public void overwritePrimitivesSetToDefaultValues(){
        Assert.fail();
    }

    @Test
    public void overwriteAllPrimitivesWithoutBoundaryValues(){
        Assert.fail();
    }

}

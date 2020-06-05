package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import com.github.johan.backstrom.common.corev2.exception.DuplicateAttributeException;
import datacow2.generators.Generators2;
import datacow2.generators.Generators;
import datacow2.models.annotation.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnnotationTest {

    @Test
    public void multipleGenerators(){

        MultipleGenerators o = DataCow.generateDairyFor(MultipleGenerators.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.singleValue);
        assertEquals("SimpleGeneratedValue", o.singleValue2);
    }

    @Test(expected = DuplicateAttributeException.class)
    public void duplicateAttributeId(){
        SimpleObjectWithDupliacteGenerators o = DataCow.generateDairyFor(SimpleObjectWithDupliacteGenerators.class)
                .milkCow();
    }

    @Test
    public void specifyingGeneratorDirectlyInDataCow(){
        NoGeneratorSpecified o = DataCow.generateDairyFor(NoGeneratorSpecified.class)
                .withGenerator(Generators.class)
                .milkCow();

        assertEquals("aString", o.aString);
    }

    @Test
    public void specifyingMultipleGeneratorsDirectlyInDataCow(){
        NoGeneratorSpecifiedWithMultipleGenerators o = DataCow.generateDairyFor(NoGeneratorSpecifiedWithMultipleGenerators.class)
                .withGenerator(Generators.class)
                .withGenerator(Generators2.class)
                .milkCow();

        assertEquals("aString", o.aString);
        assertEquals("SimpleGeneratedValue", o.anotherValue);
    }

    @Test
    public void getFieldsAlsoByName(){
        SimpleObjectWithoutAnnotations o = DataCow.generateDairyFor(SimpleObjectWithoutAnnotations.class)
                .withGenerator(Generators.class)
                .useVariableNamesAsAttributeId()
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
    }

    @Test
    public void getFieldsAlsoByNameWithDuplicateNameShouldPrioritizeAnnotations(){
        SimpleObjectWithDuplicateAttributeOneIsAnnotated o = DataCow.generateDairyFor(SimpleObjectWithDuplicateAttributeOneIsAnnotated.class)
                .useVariableNamesAsAttributeId()
                .milkCow();

        assertEquals("aString", o.getaSingleValue());
        assertNull(o.aString);
    }

    @Test
    public void multipleAttributeIdsInGeneratorAnnotation(){

    }
}

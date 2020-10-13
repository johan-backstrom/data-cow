package datacow2;

import com.github.johan.backstrom.corev2.Configuration;
import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.exception.DairyInstantiationException;
import com.github.johan.backstrom.corev2.exception.DuplicateAttributeException;
import com.github.johan.backstrom.corev2.exception.FieldNotModifiableException;
import com.github.johan.backstrom.corev2.exception.GeneratorCouldNotBeInstantiatedException;
import datacow2.generators.GeneratorWithoutDefaultConstructor;
import datacow2.generators.Generators;
import datacow2.generators.Generators2;
import datacow2.models.annotation.*;
import datacow2.models.simple.ConstructorWithArgument;
import datacow2.models.simple.SingleAttributeGeneratorWithNoDefaultConstructor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnnotationTest {

    @Test
    public void multipleGenerators(){

        MultipleGenerators o = DataCow.generateDairyFor(MultipleGenerators.class)
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.singleValue);
        assertEquals("SimpleGeneratedValue", o.singleValue2);
    }

    @Test(expected = DuplicateAttributeException.class)
    public void duplicateGeneratorIds(){
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
    public void passGeneratorInstanceToDataCow(){
        NoGeneratorSpecified o = DataCow.generateDairyFor(NoGeneratorSpecified.class)
                .withGenerator(new Generators())
                .milkCow();

        assertEquals("aString", o.aString);
    }

    @Test(expected = FieldNotModifiableException.class)
    public void wrongReturnTypeOfGeneratorGivesError(){
        GeneratorWithWrongType o = DataCow.generateDairyFor(GeneratorWithWrongType.class)
                .milkCow();
        }

    @Test(expected = DairyInstantiationException.class)
    public void instantiateObjectWithSpecialConstructorAndExpectException(){
        ConstructorWithArgument o = DataCow.generateDairyFor(ConstructorWithArgument.class)
                .milkCow();
    }

    @Test
    public void instantiateObjectWithSpecialConstructor(){
        ConstructorWithArgument o = DataCow.generateDairyForInstance(new ConstructorWithArgument("test"))
                .milkCow();

        assertEquals("SimpleGeneratedValue", o.aSingleValue);
    }

    @Test(expected = GeneratorCouldNotBeInstantiatedException.class)
    public void generatorDoesNotHaveNoArgsConstructor(){
        SingleAttributeGeneratorWithNoDefaultConstructor o = DataCow.generateDairyFor(SingleAttributeGeneratorWithNoDefaultConstructor.class)
                .milkCow();
    }

    @Test
    public void generatorDoesNotHaveNoArgsConstructorInstantiateManually(){
        SingleAttributeGeneratorWithNoDefaultConstructor o = DataCow.generateDairyFor(SingleAttributeGeneratorWithNoDefaultConstructor.class)
                .withGenerator(new GeneratorWithoutDefaultConstructor("test"))
                .milkCow();

        assertEquals("aString", o.aSingleValue);
    }

    @Test
    public void generatorReferencesAttributeWithoutGenerator(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class)
                .withConfiguration(Configuration::doNotFailOnMissingGenerators)
                .with(o1 -> o1.notFound = "hello")
                .milkCow();

        assertEquals("hello", o.notFound);
        assertEquals("This one referenced notFound: hello", o.aValue);
    }

    @Test
    public void attributeWithoutGeneratorWithBoundaryValue(){
        GeneratorNotFound o = DataCow.generateDairyFor(GeneratorNotFound.class)
                .withConfiguration(Configuration::doNotFailOnMissingGeneratorsForBoundaryValue)
                .with(o1 -> o1.notFound = "hello")
                .milkCow();

        assertEquals("hello", o.notFound);
    }

}

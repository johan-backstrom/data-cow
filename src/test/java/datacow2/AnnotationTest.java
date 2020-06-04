package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import com.github.johan.backstrom.common.corev2.exception.DuplicateAttributeException;
import datacow2.models.annotation.MultipleGenerators;
import datacow2.models.annotation.SimpleObjectWithDupliacteGenerators;
import datacow2.models.simple.SimpleObjectWithSingleAttribute;
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
    public void duplicateAttributeId(){
        SimpleObjectWithDupliacteGenerators o = DataCow.generateDairyFor(SimpleObjectWithDupliacteGenerators.class)
                .milkCow();
    }
}

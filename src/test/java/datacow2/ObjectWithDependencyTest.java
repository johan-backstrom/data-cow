package datacow2;

import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.exception.UnknownGeneratorArgumentException;
import com.github.johan.backstrom.corev2.exception.UnknownGeneratorReferenceException;
import datacow2.generators.GeneratorsWithMissingReference;
import datacow2.generators.GeneratorsWithUnknownArguments;
import datacow2.models.simple.*;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ObjectWithDependencyTest {

    @Test
    public void createDependantValue() {
        ObjectWithDependency o = DataCow.generateDairyFor(ObjectWithDependency.class)
                .milkCow();

        assertEquals("Parent : child", o.child);
        assertEquals("Parent : ", o.parent);
    }

    @Test
    public void multipleDependencies() {

        ObjectWithMultipleDependencies o = DataCow.generateDairyFor(ObjectWithMultipleDependencies.class)
                .milkCow();

        assertEquals("parent 2 Parent : child", o.child);

    }

    @Test
    public void transitiveDependency() {
        ObjectWithTransitiveDependencies o = DataCow.generateDairyFor(ObjectWithTransitiveDependencies.class)
                .milkCow();

        assertEquals("Parent : child with transitive dependency", o.child);
    }

    @Test
    public void circularDependency() {
        CirkularDependency o = DataCow.generateDairyFor(CirkularDependency.class)
                .milkCow();

        assertEquals("Cirkular 1, Cirkular 2, Cirkular 3, null", o.cirkular1);
        assertEquals("Cirkular 2, Cirkular 3, null", o.cirkular2);
        assertEquals("Cirkular 3, null", o.cirkular3);
    }

    @Test(expected = UnknownGeneratorArgumentException.class)
    public void generatorsWithUnknownArguments() {

        ObjectWithoutGenerator o = DataCow.generateDairyFor(ObjectWithoutGenerator.class)
                .withGenerator(GeneratorsWithUnknownArguments.class)
                .milkCow();
    }

    @Test(expected = UnknownGeneratorReferenceException.class)
    public void generatorsWithNonExistingReference() {
        ObjectWithoutGenerator o = DataCow.generateDairyFor(ObjectWithoutGenerator.class)
                .withGenerator(GeneratorsWithMissingReference.class)
                .milkCow();
    }
}

package datacow2;

import com.github.johan.backstrom.common.corev2.DataCow;
import datacow2.models.simple.SimpleObjectWithCirkularDependency;
import datacow2.models.simple.SimpleObjectWithDependency;
import datacow2.models.simple.SimpleObjectWithMultipleDependencies;
import datacow2.models.simple.SimpleObjectWithTransitiveDependencies;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectWithDependency {

    @Test
    public void createDependantValue(){
        SimpleObjectWithDependency o = DataCow.generateDairyFor(SimpleObjectWithDependency.class)
                .milkCow();

        assertEquals("Parent : child", o.child);
        assertEquals("Parent : ", o.parent);
    }

    @Test
    public void multipleDependencies(){

        SimpleObjectWithMultipleDependencies o = DataCow.generateDairyFor(SimpleObjectWithMultipleDependencies.class)
                .milkCow();

        assertEquals("parent 2 Parent : child", o.child);

    }

    @Test
    public void transitiveDependency(){
        SimpleObjectWithTransitiveDependencies o = DataCow.generateDairyFor(SimpleObjectWithTransitiveDependencies.class)
                .milkCow();

        assertEquals("Parent : child with transitive dependency", o.child);
    }

    @Test
    public void circularDependency(){
        SimpleObjectWithCirkularDependency o = DataCow.generateDairyFor(SimpleObjectWithCirkularDependency.class)
                .milkCow();

        assertEquals("Cirkular 1, Cirkular 2, Cirkular 3, null", o.cirkular1);
        assertEquals("Cirkular 2, Cirkular 3, null", o.cirkular2);
        assertEquals("Cirkular 3, null", o.cirkular3);
    }
}

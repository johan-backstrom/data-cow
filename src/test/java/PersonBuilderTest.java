import com.github.johan.backstrom.entities.finance.CreditCardBuilder;
import com.github.johan.backstrom.entities.person.Gender;
import com.github.johan.backstrom.entities.person.PersonBuilder;
import org.junit.Test;

public class PersonBuilderTest {

    @Test
    public void createPerson(){
        System.out.println(new PersonBuilder().setGender(Gender.Female).build());
    }

    @Test
    public void createCreditCard(){
        System.out.println(new CreditCardBuilder().build());
    }
}

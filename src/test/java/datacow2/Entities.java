package datacow2;

import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.entities.address.Address;
import com.github.johan.backstrom.corev2.entities.finance.CreditCard;
import com.github.johan.backstrom.corev2.entities.person.Person;
import com.github.johan.backstrom.corev2.entities.person.Sex;
import datacow2.models.simple.InheritedPerson;
import org.junit.Assert;
import org.junit.Test;

import static com.github.johan.backstrom.corev2.entities.person.Sex.female;
import static com.github.johan.backstrom.corev2.entities.util.DataHelper.getRandomZeroPaddedNumber;
import static org.junit.Assert.assertEquals;

public class Entities {

    @Test
    public void createAddress(){
        Address a = DataCow.generateDairyFor(Address.class)
                .with(address -> address.setPostalCode("12345"))
                .milkCow();

        assertEquals("12345", a.getPostalCode());
    }

    @Test
    public void createCreditCard(){
        CreditCard c = DataCow.generateDairyFor(CreditCard.class)
                .with(cc -> {
                    cc.setExpireMonth("11");
                    cc.setExpireYear("2022");
                })
                .milkCow();

        assertEquals("11/22", c.getExpire());
    }

    @Test
    public void createPerson(){
        Person p = DataCow.generateDairyFor(Person.class)
                .with(person -> person.setSex(female))
                .milkCow();

        Assert.assertNotNull(p.getGivenName());
    }

    @Test
    public void zeroPaddedNumber(){
        Assert.assertEquals("00059", getRandomZeroPaddedNumber(59, 59, 5));
    }

    @Test
    public void inheritedGenerators(){
        InheritedPerson p = DataCow.generateDairyFor(InheritedPerson.class).milkCow();
        assertEquals("Chris", p.getGivenName());
    }
}

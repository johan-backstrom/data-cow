import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.entities.Country;
import com.github.johan.backstrom.entities.person.DataHelper;
import com.github.johan.backstrom.entities.person.Gender;
import org.junit.Test;

public class FirstTest {

    Attribute<Gender> gender = new StandardAttribute<>(
            "gender",
            dependencyAttributes -> Gender.Female
    );

    Attribute<String> lastName = new StandardAttribute<>(
            "lastName",
            dependencyAttributes -> DataHelper.getRandomLastName()
    );

    Attribute<String> firstName = new StandardAttribute<>(
            "firstName",
            dependencyAttributes -> DataHelper.getRandomFirstName(dependencyAttributes.get("gender"))
    );

    Attribute<String> phoneNumber = new StandardAttribute<>(
            "mobilePhone",
            dependencyAttributes -> DataHelper.getRandomMobilePhoneNumber(Country.SWEDEN)
    );


    @Test
    public void simpleTest() {

        DocumentBuilder documentBuilder = new DocumentBuilder()
                .addAttribute(gender)
                .addAttribute(firstName)
                .addAttribute(lastName)
                .addAttribute(phoneNumber)
                .addDependency(firstName, gender)
                .buildDataForEmptyAttributes();

        System.out.println(
                documentBuilder.toString()
        );


        try {
            System.out.println(new ObjectMapper().writeValueAsString(documentBuilder.toMap()));
        } catch (Exception e) {

        }
    }
}

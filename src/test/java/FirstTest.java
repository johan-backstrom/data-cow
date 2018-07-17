import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.entities.Countries;
import com.github.johan.backstrom.entities.person.NameHelper;
import com.github.johan.backstrom.entities.util.DataHelper;
import com.github.johan.backstrom.entities.person.Gender;
import org.junit.Test;

public class FirstTest {

    Attribute<Gender> gender = new StandardAttribute<>(
            "gender",
            dependencyAttributes -> Gender.Female
    );

    Attribute<String> lastName = new StandardAttribute<>(
            "lastName",
            dependencyAttributes -> new NameHelper().getRandomLastName(Countries.SWEDEN)
    );

    Attribute<String> firstName = new StandardAttribute<>(
            "firstName",
            dependencyAttributes -> new NameHelper().getRandomFirstName((Gender)dependencyAttributes.get("gender").getValue(), Countries.SWEDEN)
    );

    Attribute<String> phoneNumber = new StandardAttribute<>(
            "mobilePhone",
            dependencyAttributes -> DataHelper.getRandomMobilePhoneNumber(Countries.SWEDEN)
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

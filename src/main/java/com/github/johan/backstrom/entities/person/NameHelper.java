package com.github.johan.backstrom.entities.person;

import com.github.johan.backstrom.common.util.DefaultRandomnessImplementation;
import com.github.johan.backstrom.common.util.Randomness;
import com.github.johan.backstrom.entities.Country;

import static com.github.johan.backstrom.entities.person.Gender.Male;

public class NameHelper {

    private Randomness random = new DefaultRandomnessImplementation();
    public void setRandomness(Randomness randomness){
        random = randomness;
    }

    public String getRandomFirstName(Gender gender, Country country) {
        if ("SWE".equals(country.getIso3countryCode())){
            if(Male.equals(gender)){
                return getRandomValue(maleSwedishNames);
            } else {
                return getRandomValue(femaleSwedishNames);
            }
        } else {
            if(Male.equals(gender)){
                return getRandomValue(maleInternationalNames);
            } else {
                return getRandomValue(femaleInternationalNames);
            }
        }
    }

    public String getRandomLastName(Country country) {
        if ("SWE".equals(country.getIso3countryCode())){
            return getRandomValue(maleSwedishNames).concat("sson");
        } else {
            return getRandomValue(internationalLastNames);
        }
    }

    private String getRandomValue(String[] values) {
        return values[random.getRandomInteger(0, values.length - 1)];
    }

    private static final String[] maleSwedishNames = {
            "Adam",
            "Bertil",
            "Cesar",
            "David",
            "Erik",
            "Filip",
            "Gustav",
            "Helge",
            "Ivar",
            "Johan",
            "Karl",
            "Ludvig",
            "Martin",
            "Nils",
            "Olof",
            "Petter",
            "Qvintus",
            "Rudolf",
            "Sigurd",
            "Urban",
            "Viktor",
            "Wilhelm",
            "Xerxes",
            "Yngve",
            "Zäta",
            "Åke",
            "Ärlig",
            "Östen"
    };

    private static final String[] femaleSwedishNames = {
            "Mikaela",
            "Carolina",
            "Anna",
            "Erika",
            "Filippa",
            "Helena",
            "Ivana",
            "Johanna",
            "Camilla",
            "Ulrika"
    };

    private static final String[] maleInternationalNames = {
            "Michael",
            "Tim",
            "Max",
            "Donald",
            "Daniel",
            "Christhopher",
            "Robert",
            "Roger",
            "John"
    };

    private static final String[] femaleInternationalNames = {
            "Mariah",
            "Marie",
            "Stephanie",
            "Helena",
            "Christina",
            "Magdalena",
            "Jenny",
            "Anette",
            "Linda"
    };

    private static final String[] internationalLastNames = {
            "Anderson",
            "Jackson",
            "Schroder",
            "Fermi",
            "Curie",
            "Wilson",
            "Richardson"
    };
}

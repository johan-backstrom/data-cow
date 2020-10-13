package datacow2.generators;

import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.References;
import datacow2.models.simple.MultipleAttributes;

public class Generators {

    @Generator("aSingleValue")
    public String aSingleValue(){
        return "SimpleGeneratedValue";
    }

    @Generator("aString")
    public String aString(){
        return "aString";
    }

    @Generator("anInteger")
    public int anInteger(){
        return 666;
    }

    @Generator("aPrimitiveType")
    public int aPrimitiveType(){
        return 123;
    }

    @Generator("aPrimitiveType2")
    public int aPrimitiveType2(){
        return 123;
    }

    @Generator("aBoolean")
    public boolean aBoolean(){
        return false;
    }

    @Generator("parentAttribute")
    public String parent(){
        return "Parent : ";
    }

    @Generator("childAttribute")
    public String child(
            @References("parentAttribute") String parent
    ){
        return parent + "child";
    }

    @Generator("secondParentAttribute")
    public String parent2(){
        return "parent 2";
    }

    @Generator("childWithTwoParents")
    public String childWithTwoParents(
            @References("parentAttribute") String parent,
            @References("secondParentAttribute") String parent2
    ){
        return parent2 + " " + parent + "child";
    }

    @Generator("childWithTransitiveDependency")
    public String childWithTransitiveDependency(
            @References("childAttribute") String parent
    ){
        return parent + " with transitive dependency";
    }

    @Generator("complexType")
    public MultipleAttributes complexObject(){
        return DataCow.generateDairyFor(MultipleAttributes.class).milkCow();
    }

    @Generator("cirkular1")
    public String cirkular1(
            @References("cirkular2") String parent
    ){
        return "Cirkular 1, " + parent;
    }

    @Generator("cirkular2")
    public String cirkular2(
            @References("cirkular3") String parent
    ){
        return "Cirkular 2, " + parent;
    }

    @Generator("cirkular3")
    public String cirkular3(
            @References("cirkular1") String parent
    ){
        return "Cirkular 3, " + parent;
    }

    @Generator("nullValue")
    public String nullValue(){
        return null;
    }

    @Generator("primitiveNull")
    public Integer primitiveNull(){
        return null;
    }

    @Generator("referencesNotFound")
    public String referencesNotFound(
            @References("notFound") String notFound
    ){
        return "This one referenced notFound: " + notFound;
    }

}
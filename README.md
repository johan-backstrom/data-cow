## Introduction

The idea behind Data Cow is to be able to create datasets for testing with a minimum amount of maintenance applying the
principles of "Test Data as Code".

## Concepts

The main concept in Data Cow is to treat test data as code. Just like "infrastructure as code" and "configuration as code"
which has gained popularity in the DevOps community, test data can and *should* be managed by a declarative approach. 

The central idea is that the "Cattle vs. Pets" metaphore is applied to test data. Instead of managing sets of actual
data in databases or script (pets), a recipe for the needed data is defined, and the data is then created when and where
is is needed (cattle). Changes in the information model are handled by fefactoring code rather than transforming data.

In Data Cow, the information model is declared as a graph where the nodes are called attributes and the edges are called 
dependencies.

### Attribute

An attribute is a value in the information model. When defining an attribute in Data Cow, you need to provide a function
for setting the value of that attribute. This function can generate a random value, read it from some external resource or
hard code something.

To make a data set internally consistent, there might be dependencies between attributes. For example "birth date" <--> "age".
When a data generation function is called in an attribute, the values of its dependencies are passed to that function.

To define data generation for an attribute, a name and a lamda expression is passed to the attributes constructor. The lambda
will receive a map with the attributes it depends on and the name of those attributes as keys. The absolutely simplest case is 
to return a hard coded value from the lambda.

```
Attribute<String> firstName = new StandardAttribute<>(
    "firstName",
    dependencyAttributes -> "Michael"
);
```

### Dependency

Dependencies are simple pointers between two attributes that allows data to be passed to the function generating the data 
for the attribute.

### Data Generation

When the data is built, Data Cow will start at an attribute and then scan the dependencies, the attributes that
it dependes on. When an attribute with no dependencies is found, Data Cow will call the 
data generation function on that attribute and then pass the results to all attributes depending on that attribute recursively
until all attributes have values.

The data generation funciton which is a lambda expression will receive a map with the attributes it depends on and the 
attribute names as keys as its input parameter.

#### Circular dependencies

Circular dependencies could lead to an infinite recursion if no values were set. To prevent that, an attributes' data 
generation function will be called when all its dependencies point to nodes that have already been traversed by the 
data generation.

## Examples

### A random person

One of the simplest cases is a model with two attributes and one dependency. Data for attributes with no dependencies will
be generated first which means that the value for gender will be set by calling the method getRandomGender().

The gender attribute is then passed in the map of attributes to the firstName attribute.

```java
public class SimplePersonCreator{

    Attribute<Gender> gender = new StandardAttribute<>(
        "gender",
        dependencyAttributes -> SomeHelperClass.getRandomGender()
    );

    Attribute<String> firstName = new StandardAttribute<>(
        "firstName",
        dependencyAttributes -> dependencyAttributes.get("gender").equals(Gender.Female) ? "Mariah" : "Michael"
    );
    
    public Map<String, Object> createPerson(){
        DocumentBuilder documentBuilder = new DocumentBuilder()
            .addAttribute(gender)
            .addAttribute(firstName)
            .addDependency(firstName, gender)
            .buildDataForEmptyAttributes();
    
        return documentBuilder.toMap();
    }
}
```


### Controlling boundary values

To build a generic data generation capability you start by creating random values (although internally consistent) 
for all you attributes.

When you use your data for a specific purpose like writing a test, completely random data probably isn't what you want. This
is where boundary values will come in to play. As you may have noticed the top level method that actually creates the data is called
"buildDataForEmptyAttributes". This means that you can set values for some attributes and let the data generation create data
consistent with those attributes for the rest of the model.

If we extend the example from above with a setter we can use a boundary value to create an object with the desired properties
while letting the framework auto generate all the attributes that are not relevant to our test.

```java
public class SimplePersonCreator{

    Attribute<Gender> gender = new StandardAttribute<>(
        "gender",
        dependencyAttributes -> SomeHelperClass.getRandomGender()
    );

    Attribute<String> firstName = new StandardAttribute<>(
        "firstName",
        dependencyAttributes -> dependencyAttributes.get("gender").equals(Gender.Female) ? "Mariah" : "Michael"
    );
    
    public SimplePersonCreator setGender(Gender gender){
        this.gender.setValue(gender);
        return this;
    }
    
    public Map<String, Object> createPerson(){
        DocumentBuilder documentBuilder = new DocumentBuilder()
            .addAttribute(gender)
            .addAttribute(firstName)
            .addDependency(firstName, gender)
            .buildDataForEmptyAttributes();

        return documentBuilder.toMap();
    }
}
```

This allows the user of our SimplePersonCreator to create an object that suits his/her needs. In the following example
we create a random object with the attributes relevant to the test specified. If we change the information model in a way
that is not relevant to this test, it is is completely unaffected. 

Compare this to keeping test data for the entire model in something like an xml file or sql-script where every change
  to the model will affect every script.

```java
public class PersonTest{

    @Test
    public void processFemalePerson(){
        
        Map<String, Object> person = new SimplePersonCreator()
            .setGender(Gender.Female)
            .createPerson;
            
        // Use the generated data to test your system...
    }
}
```

### Converting to proper POJOs

The document builder will return a map with attribute names and values. The simplest way to convert this to a proper POJO
is by using Jackson in the following way:

```
MyPerson person = new ObjectMapper().convertValue(documentBuilder.toMap(), MyPerson.class);
```

In this example I have choosen to create a model object called MyPerson. 

## Download

Download using gradle or maven. As of 2017-06-25 synching to Maven Central is in progress and artifacts will soon be availiable!

Gradle:

```
compile group: 'com.github.johan-backstrom', name: 'data-cow', version: '1.0'
```

## Build

![alt text](https://travis-ci.org/johan-backstrom/data-cow.svg?branch=master "Curent build status")

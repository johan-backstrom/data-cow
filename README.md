# Data Cow 2

## Download

Download from Maven central using gradle or maven.

Gradle:

```
compile 'com.github.johan-backstrom:data-cow:2.0.0'
```

Maven:

```
<dependency>
    <groupId>com.github.johan-backstrom</groupId>
    <artifactId>data-cow</artifactId>
    <version>2.0.0</version>
</dependency>
```

DataCow has no third party dependencies and is pure java-8. It will not 
introduce any security or compatibility concerns. 

## Introduction

The idea behind Data Cow is to be able to create datasets for testing with a minimum amount of maintenance applying the
principles of "Test Data as Code".

## Test Data as Code

The main concept in Data Cow is to treat test data as code. Just like "infrastructure as code" and "configuration as code"
that has gained popularity in the DevOps community, test data can and *should* be managed by a declarative approach. 

The central idea is that the "Cattle vs. Pets" metaphore is applied to test data. Instead of managing sets of actual
data in databases or scripts such as SQL statements (pets), a recipe for the needed data is defined, and the data is 
then created when and where it is needed (cattle). Changes in the information model are handled by refactoring code 
rather than transforming data.

One of the main benefits of using data cow is that you can build data where you specify only the attributes that are of
importance to you and then let data-cow generate random data, although consistent with the specified values, for all other 
attributes. This will radically reduce maintenance of test data compared to maintaining a complete data set in for example
text files or databases.

## Concepts

In Data Cow, the information model is declared as POJOs, commonly referred to as DTOs or models. In Data Cow terminology,
it is called a **dairy class**. The member fields of these classes are called **attributes**.

Each of the fields that should have data generated for it has a **generator** connected to it. A generator is a method that
generates a value, possibly with input from other fields. These inputs are the relations that make the generated data set
internally consistent. For example, if you are generating credit card data, then there will be a relation between
which credit card network the card belongs to (e.g. Visa, Amex) and what starting digit the credit card number has.

The attributes, generators and their relations form a directed cyclic graph. Data Cow topologically sorts that graph and
generates the values accordingly to maintain internal consistency.

So, should your fields always contain generated random values? For most use cases, completely random values are not what you want. 
Take the example of a credit card payment. You may for example want to test different credit card networks (visa, MC etc.) but 
for the particular test, the rest of the card information is not relevant. To handle this case, Data Cow has a 
concept called **boundary values**. By specifying one or more values in the information model before generating the data
(boundary values), Data Cow will generate values consistent with those values.

This is what makes Data Cow powerful. In most scenarios, your test code that generates the data only needs know about
the part of the information model that is relevant to that specific test. Data Cow of course needs to know the full model.
If the information model changes, that change will only impact Data Cow and tests that are directly related to that specific 
change. Tests that do not interact with the modified area of the information model will remain unchanged.

Compare this to the common approach that each tests has a complete data set in a file or script that is injected into
the system before the test is executed. Any change to the data model would in this case affect each and every script,
even those that are not related to the change. 

#### Attributes

An attribute is a value in the information model. The @Attribute annotation is used to mark it as a candidate for data
generation and also give it a globally unique attribute id.

```java
@WithGenerators(PersonGenerators.class)
public class Person{
    
    @Attribute("firstName")
    private String name;

    @Attribute("lastName")
    public String lastName;

    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
}
```

Attribute fields need to be either public or have public getters and setters for Data Cow to set the values. The attribute
id is specified as an argument to the annotation. It must not correspond to the variable name, but must be unique in the
class. Making it globally unique is probably a good idea in most cases.

#### Dairy classes

In the above example, Person is the dairy class. @WithGenerators is a class level annotation that tells Data Cow what
classes to scan for generators.

The default behaviour of Data Cow is to expect a no args constructor in the dairy class.

#### Generators

A generator is a method that returns the generated value. The @Generator annotation together with the attribute id is used
to define the method as a generator and connect it to the attribute.

```java
public class PersonGenerators{
    
    @Generator("firstName")
    public String getFirstName(){
        return "John";
    }
}
```

To make a data set internally consistent, there might be dependencies between attributes. For example "birth date" <--> "age".
In Data Cow, this is handled by specifying arguments to the generator method and annotating those arguments with the
@References annotation. @References takes an attribute id as its argument.

```java
@Generator("birthDate")
public LocalDate getBirthDate(){
    // return a random date...
}

@Generator("age")
public int getAge(@References("birthDate") LocalDate birthDate){
    // Code for calculating age from birth date
}
```
The reference in this example will cause the birthDate attribute to be generated first and then passed as the argument
to the age generator.

It is up to the programmer to make sure that the return type and argument types of the generator match the type of the 
referenced attribute. Typing errors will of course be quite obvious at runtime if not caught earlier.

The default behaviour of Data Cow is to expect a no args constructor in the generator class.

#### Boundary values

To build a generic data generation capability you start by creating random values (although internally consistent) for 
all your attributes. When you use your data for a specific purpose like writing a test, completely random data probably 
isn't what you want. This is where boundary values will come in to play. 

Boundary values are the requirements you have on your test data. the part of your test data that is relevant to your
test. See "Generating data" below for usage.

#### Data Generation

When the data is built, Data Cow will start at an attribute and then scan its dependencies, the attributes that
its data generation dependes on. When an attribute with no dependencies is found, Data Cow will call the generator function on 
that attribute and then pass the results to all attributes depending on that attribute recursively until all 
attributes have values. If an attribute already has a pre defined boundary value, the generator function will not be 
called and the pre defined value is used instead.

#### Circular dependencies

Circular dependencies could lead to an infinite recursion if no values were set. To prevent that, a generator will be
called when all its dependencies point to nodes that have already been traversed by the data generation. This
means that the arguments passed to a generator potentially could be null.

It is up to the programmer to handle null values in the generator arguments if there are circular dependencies.

#### Primitive types and variables with pre-set values

Since Data Cow checks for null-values to determine whether it should generate data for a particular field, we run into
problems when dealing with primitive types and classes where declarations are coupled with instantiation like in the 
following example.

```java
public class ProblematicExample {

    public int anInteger;
    public List<Object> aList = new ArrayList<>();
}
```

What happens when Data Cow instantiates an object from this class, is that both these fields will be non null, and thus
data Cow will not generate data for them. The recommended way to handle these problems is to change the primitive types 
to reference types such as Integer and to declare values without assignments.

 ```java
 public class NoLongerProblematicExample {
 
     public Integer anInteger;
     public List<Object> aList;
 }
 ```

If this solution is not possible, it is also possible to use the "overwrite" configurations described in the Configuration
 section below.

## Generating data

This section contains practical guidance on how to use DataCow. _**This is the recommended way of working with Data Cow,**_
but as you will see in the configuration section, there are a few tweaks you can apply, especially if you do not have
full control over your dairy classes.

#### A random person

One of the simplest cases is a model with two attributes and one dependency. Data for attributes with no dependencies will
be generated first which means that the value for sex will be set by data Cow by calling the method getRandomSex().

The sex attribute is then passed to the "givenName" generator by Data Cow.

```java
@WithGenerators(PersonGenerators.class)
public class Person {

    @Attribute("sex")
    private Sex sex;

    @Attribute("givenName")
    private String givenName;

}
```

```java
public class PersonGenerators {

    @Generator("sex")
    public Sex getRandomSex() {
        // return a random sex (male/female)
    }

    @Generator("givenName")
    public String getGivenName(
            @References("sex") Sex sex
    ) {
        if (male.equals(sex)) {
            return "Michael";
        } else {
            return "Mariah";
        }
    }
}
```

Now that we have both our attributes and our generators set up, we call Data Cow using JUnit.

```java
@Test
public void generateRandomPerson(){

    Person p = DataCow.generateDairyFor(Person.class)
            .milkCow();

    Assert.assertNotNull(p.getGivenName());

}
```

Many more examples can be found in the unit tests.

#### Controlling boundary values

The boundary values are set to define the characteristics of the generated data.

```java
public void generateRandomPerson(){

    Person p = DataCow.generateDairyFor(Person.class)
            .with(person -> person.setSex(female))
            .milkCow();

    Assert.assertEquals("Mariah", p.getGivenName());

}
```

This is very useful if you, for example, want to test logic that uses the sex of the person. Again, the power of Data Cow is that
this test will be completely unaware of the rest of the data model, thus resulting in much less re-work when making changes 
to that data model.

It is important to understand that the ```with``` method will allow you to modify the instace of your DairyClass created by DataCow.
This means that you can mutate the object in any way you please, includes calling methods and setting public variables.

#### Nesting objects

It is pretty common with nested objects. For example: A person object may have a credit card object (or even a list of
credit cards) containing card data for the person. Generating this type of data is generally achieved by putting
another call to DataCow in the generator like so:

```java
@Generator("creditCard")
public CreditCard getCreditCard(){
    return DataCow.generateDairyFor(CreditCard.class)
        .milkCow();
} 
```

## Configuration

There are a few different ways in which Data Cow can be configured to suit your needs. Configuration options are passed to 
DataCow using the withConfiguration() method or set in a static context. With javas lambda functions there are two
different ways to specify the config, either with a lambda or with a function reference.

Of course, the ```withConfiguration``` method returns the data cow object to allow multiple configurations options to
be chained together.

Examples of instance config:

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.useVariableNamesAsAttributeId())
    .milkCow();
```

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(Configuration::useVariableNamesAsAttributeId)    
    .milkCow();
```
Example of static config:

```java
DataCow.withStaticConfiguration(Configuration::useVariableNamesAsAttributeId);

Person p = DataCow.generateDairyFor(Person.class)    
    .milkCow();
```

The static method ```DataCow.useDefaultConfiguration();``` will reset the static config to DataCow defaults.

The following configuration options currently exist.

#### Use variable names as attribute ids

This option will do what it is called. It is useful if you for some reason can't or don't want to annotate the fields
in your dairy class. 

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.useVariableNamesAsAttributeId())
    .milkCow();
```

#### Do not fail on missing generators

By default, DataCow will fail if no generator matching the attribute id can be found. With this option an attribute with
no generator will be ignored.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.doNotFailOnMissingGenerators())
    .milkCow();
``` 

#### Do not fail on missing generators if a boundary value is set

By default, DataCow will fail if no generator matching the attribute id can be found. With this option an attribute with
no generator will be ignored if it has a boundary value.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.doNotFailOnMissingGeneratorsForBoundaryValue())
    .milkCow();
``` 

#### Overwrite all attributes

This will generate values even when there is a value set already, ignoring all boundary values or values
defined in the dairy class.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.overwriteAllAttributes())
    .milkCow();
```

#### Overwrite specific attribute

In some special cases you may have a dairy classes that can not be modified that contains initialized
values. By default DataCow will treat these initialized values as boundary values.

By using the overwriteAttribute method of the Configuration class, you can make DataCow treat these values
as normal attributes that will have data generated for it. The method takes the attribute id of the field to overwrite.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.overwriteAttribute("anAtttributeId"))
    .milkCow();
```
Multiple attribute ids can be passed.
```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.overwriteAttribute("attribute1")
    .withConfiguration(config -> config.overwriteAttribute("attribute2")
    .milkCow();
``` 
Alternatively, an array of attributes can be passed to the config method
```java
Person p = DataCow.generateDairyFor(Person.class)
    .withConfiguration(config -> config.overwriteAttribute(new String[]{"attribute1", "attribute2"}))
    .milkCow();
``` 

## Dairy classes and generators that can not be changed

If you are generating data using production code or third party classes where you do not want to, or can't, annotate 
the fields of your dairy classes, you cannot use DataCow's default behaviour.

A word of caution: _These options are just here to save your ass when it's already too late to turn back. Don't 
use this as your default approach_.

#### Dairy classes without no args constructor

By default, DataCow will instantiate the object for you, but this requires a no argument constructor. If your class
doesn't have one, you can pass an object instance instead.

```java
ClassWithConstructorArgument o = DataCow.generateDairyForInstance(new ClassWithConstructorArgument("test"))
                .milkCow();
```

#### Dairy classes without annotations

There are usually two different annotations in the dairy class: @Attribute and @WithGenerators. As documented under
the configuration section, there is an option for using the field names as attribute ids. If it's not possible to
annotate the dairy class with the @WithGenerators annotation, the generator classes can be passed to DataCow with
a method call instead.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withGenerator(PersonGenerators.class)
    .milkCow();
``` 

#### Generators without default constructor

If for some reason you are unable to add a no arg constructor to your generator class, you can
pass an instance of a generator class to DataCow. If an instance is passed, data cow will not instantiate
objects of that type, even if present in the @WithGenerators annotation.

```java
Person p = DataCow.generateDairyFor(Person.class)
    .withGeneratorInstance(new PersonGenerators("Some arg"))
    .milkCow();
``` 

This can be extra useful for parameterizing your generators. 

## Pre built generators

Included in DataCow is a small set of ready made generators and dairy classes. These include:

- CreditCard
- Address
- Person

Where the person object will contain both an address and a credit card. This is both a good example to look at when building
your own data and a good starting point that you can extend if you wish to build upon it. The following is an example
for creating a credit card with expiration 11/22.

```java
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
```

There is also a class called DataHelper which has methods to help you get pseudo random numbers. There is a static method
for setting the seed ```DataHelper.setRandomSeed(aNumber)```. DataHelper is used by the pre built generators.

Using seeded pseudo random numbers for generating test data is often very useful. This means that it is possible to 
re-generate a data set identical to the one used in a nightly test run, assuming you are using the same
code and the same seed, of course.

## Extending generators and dairy classes

Extending the ready made dairy classes and generators is done with inheritance. ```@Attribute``` and ```@Generator``` annotations
are inherited while the ```@WithGenerators``` annotation isn't. The following example shows how to extend the person generator.

```java
@WithGenerators(ExtendedPersonGenerators.class)
public class ExtendedPerson extends Person {

}

public class ExtendedPersonGenerators extends PersonGenerators {

    @Override
    public String getGivenName(Sex sex) {
        return "Chris";
    }
}
```

## Serializing data

DataCow creates the data as POJOs. This is quite often not the desired end result. Most of the time, you need data in
a json format, SQL format, flat text file etc.

Most of these things can easily be achieved with a few lines of code and there are plenty of frameworks out there to
give you what you need. For example, the jackson library for json-serialization turns your java objects into json.

A very common (but in my opinion bad) practice is to keep data in sql-scripts or json-files and then insert that data into 
the system under test as a first step in an automated test suite. A much more effective way of achieving the same thing 
is to generate the data and write the files "on the fly" with DataCow. When the data model changes, you only need to
 change a few lines of your DataCow code and not go through hundreds or thousands of json-files or lines of SQL-DDLs. 

## Build

![alt text](https://travis-ci.org/johan-backstrom/data-cow.svg?branch=master "Curent build status")

https://travis-ci.org/johan-backstrom/data-cow
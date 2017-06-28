# Java.Binary
Binary auto-parsing for Java.

##### Examples
For using the ObjectEncoder and ObjectDecoder classes you just need to declare your java classes with all properties being supported, just notice the DataType enum.

###### Writing example:
Example of the implementation for converting objects to data (DataReader is a byte[] wrapped).
```java
public class Employee {
    private String name;
    private Integer age;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

public class Boss extends Employee {
    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return this.employees;
    }
}

Employee employee1 = new Employee();
employee1.setName("John Apple Juice");
employee1.setAge(35);

Boss boss = new Boss();
boss.setName("Steve James Apple Orange Juice");
boss.setAge(65);
boss.getEmployees().add(employee1);

ObjectEncoder encoder = new ObjectEncoder();
DataReader data = encoder.encode(boss);
```

###### Parsing example:
Example of the implementation for converting data (DataReader is a byte[] wrapped) to objects.
Obs: Using the same classes from above.
```java
DataReader data = // binary byte[]

ObjectDecoder decoder = new ObjectDecoder();
Boss boss = decoder.decode(data, Boss.class);

assert(boss.getName() == "Steve James Apple Orange Juice")
assert(boss.getAge() == 65)
assert(boss.getEmployees().get(0).getName() == "John Apple Juice")
assert(boss.getEmployees().get(0).getAge() == 35)
```

Any doubts, post an issue or create a pull request. Pull requests are welcome.
Thanks.
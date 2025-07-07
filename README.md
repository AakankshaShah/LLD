# LLD
Link : https://gitlab.com/shrayansh8/interviewcodingpractise/-/tree/main/src/LowLevelDesign
![image](https://github.com/user-attachments/assets/7ef6e3e6-5c7d-4ec1-bc06-02b3e7792e26)

<img width="860" alt="image" src="https://github.com/user-attachments/assets/122af20f-a05c-4127-b89d-5d56c6a99234" />

| Principle | Name                                | Summary                                                                                                         |
| --------- | ----------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| **S**     | **Single Responsibility Principle** | A class should have one and only one reason to change. *(Do one thing well.)*                                   |
| **O**     | **Open/Closed Principle**           | Software entities should be open for extension but closed for modification. *(You can add, but don’t rewrite.)* |
| **L**     | **Liskov Substitution Principle**   | Subtypes must be substitutable for their base types. *(A subclass should behave like its parent class.)*        |
| **I**     | **Interface Segregation Principle** | Clients shouldn’t be forced to depend on methods they don’t use. *(Use small, specific interfaces.)*            |
| **D**     | **Dependency Inversion Principle**  | Depend on abstractions, not concretions. *(High-level modules shouldn’t depend on low-level details.)*          |


Strategy Design Pattern
   ```java
public interface DriveStrategy {
    void drive();
}

public class NormalDrive implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving in normal mode: balanced performance and comfort.");
    }
}

public class SportyDrive implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving in sporty mode: fast acceleration and sharp handling.");
    }
}

public interface Vehicle {
    void drive();
}

public abstract class BaseCar implements Vehicle {
    protected DriveStrategy driveStrategy;

    public BaseCar(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
    }

    public void setDriveStrategy(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
    }

    @Override
    public void drive() {
        driveStrategy.drive();
    }
}

public class NormalCar extends BaseCar {
    public NormalCar() {
        super(new NormalDrive());
    }
}

public class OffRoadCar extends BaseCar {
    public OffRoadCar() {
        super(new SportyDrive()); // Off-road could use sporty or custom drive strategy
    }
}
```

Observer Design Pattern
```java

public interface Observer {
    void update(String productName);
}
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
import java.util.ArrayList;
import java.util.List;

public class Product implements Subject {
    private String name;
    private boolean inStock = false;
    private List<Observer> observers = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
        if (inStock) {
            notifyObservers();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this.name);
        }
    }
}

public class User implements Observer {
    private String email;

    public User(String email) {
        this.email = email;
    }

    @Override
    public void update(String productName) {
        System.out.println("Hey " + email + ", the product '" + productName + "' is now back in stock!");
    }
}
public class Main {
    public static void main(String[] args) {
        Product iphone = new Product("iPhone 15 Pro");

        // Users click "Notify Me"
        User alice = new User("alice@example.com");
        User bob = new User("bob@example.com");

        iphone.registerObserver(alice);
        iphone.registerObserver(bob);

        // Product is restocked
        System.out.println("Restocking iPhone...");
        iphone.setInStock(true);
    }
}



```
```java
    import java.util.Observable;

public class Product extends Observable {
    private String name;
    private boolean inStock = false;

    public Product(String name) {
        this.name = name;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
        if (inStock) {
            setChanged();
            notifyObservers(); // No argument this time
        }
    }

    public String getName() {
        return name;
    }
}
import java.util.Observer;
import java.util.Observable;

public class User implements Observer {
    private String email;
    private Product product; // injected observable

    public User(String email, Product product) {
        this.email = email;
        this.product = product;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Hey " + email + ", the product '" + product.getName() + "' is now back in stock!");
    }
}
public class Main {
    public static void main(String[] args) {
        Product iphone = new Product("iPhone 15 Pro");

        User alice = new User("alice@example.com", iphone);
        User bob = new User("bob@example.com", iphone);

        iphone.addObserver(alice);
        iphone.addObserver(bob);

        System.out.println("Restocking iPhone...");
        iphone.setInStock(true);
    }
}

```

Decorator Design Pattern
```java
public interface Pizza {
    String getDescription();
    double getCost();
}
public class MargheritaPizza implements Pizza {
    @Override
    public String getDescription() {
        return "Margherita";
    }

    @Override
    public double getCost() {
        return 5.00;
    }
}

public class FarmhousePizza implements Pizza {
    @Override
    public String getDescription() {
        return "Farmhouse";
    }

    @Override
    public double getCost() {
        return 6.50;
    }
}
public abstract class ToppingDecorator implements Pizza {
    protected Pizza pizza;

    public ToppingDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}
public class Cheese extends ToppingDecorator {
    public Cheese(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Cheese";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 1.00;
    }
}

public class Olives extends ToppingDecorator {
    public Olives(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Olives";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 0.75;
    }
}

public class Jalapenos extends ToppingDecorator {
    public Jalapenos(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Jalapenos";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 0.90;
    }
}
public class Main {
    public static void main(String[] args) {
        Pizza pizza = new MargheritaPizza();
        pizza = new Cheese(pizza);
        pizza = new Olives(pizza);
        pizza = new Jalapenos(pizza);

        System.out.println("Pizza: " + pizza.getDescription());
        System.out.println("Total Cost: $" + pizza.getCost());
    }
}
```

```
public class ToppingDecorator implements Pizza {
    protected Pizza pizza; // No constructor defined here
}
public class Cheese extends ToppingDecorator {

    public Cheese(Pizza pizza) {
        this.pizza = pizza; // ✅ Now valid, since superclass has a no-arg constructor
    }

    // override methods...
}

```
Factory Design Pattern
  ```java
     public interface Vehicle {
    void start();
    }
    public class TwoWheeler implements Vehicle {
    @Override
    public void start() {
        System.out.println("Two-wheeler is starting. Ready to ride!");
    }
   }

   public class FourWheeler implements Vehicle {
    @Override
    public void start() {
        System.out.println("Four-wheeler is starting. Buckle up!");
    }
    }
     public class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        switch (type.toLowerCase()) {
            case "2":
            case "twowheeler":
                return new TwoWheeler();
            case "4":
            case "fourwheeler":
                return new FourWheeler();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
    }


  ```

Abstract Factory Design pattern
```
public interface Vehicle {
    void start();
}
public class Bike implements Vehicle {
    @Override
    public void start() {
        System.out.println("Bike started. Zoom Zoom!");
    }
}

public class Scooter implements Vehicle {
    @Override
    public void start() {
        System.out.println("Scooter started. Whirr!");
    }
}

public class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car started. Vroom Vroom!");
    }
}

public class Truck implements Vehicle {
    @Override
    public void start() {
        System.out.println("Truck started. Heavy load moving!");
    }
}
public interface VehicleFactory {
    Vehicle createVehicle(String type);
}
public class TwoWheelerFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle(String type) {
        switch (type.toLowerCase()) {
            case "bike":
                return new Bike();
            case "scooter":
                return new Scooter();
            default:
                throw new IllegalArgumentException("Unknown two-wheeler type: " + type);
        }
    }
}
public class FourWheelerFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle(String type) {
        switch (type.toLowerCase()) {
            case "car":
                return new Car();
            case "truck":
                return new Truck();
            default:
                throw new IllegalArgumentException("Unknown four-wheeler type: " + type);
        }
    }
}
public class FactoryProvider {
    public static VehicleFactory getFactory(String category) {
        switch (category.toLowerCase()) {
            case "2":
            case "twowheeler":
                return new TwoWheelerFactory();
            case "4":
            case "fourwheeler":
                return new FourWheelerFactory();
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
}
public class Main {
    public static void main(String[] args) {
        VehicleFactory twoWheelerFactory = FactoryProvider.getFactory("2");
        Vehicle bike = twoWheelerFactory.createVehicle("bike");
        bike.start();

        VehicleFactory fourWheelerFactory = FactoryProvider.getFactory("4");
        Vehicle car = fourWheelerFactory.createVehicle("car");
        car.start();
    }
} 
```

| Feature                            | `abstract class`                                  | `interface`                                                 |
| ---------------------------------- | ------------------------------------------------- | ----------------------------------------------------------- |
| **Purpose**                        | Partial implementation & shared state             | Pure abstraction (contract)                                 |
| **Keyword**                        | `abstract class`                                  | `interface`                                                 |
| **Method implementation allowed?** | ✅ Yes (can have both abstract & concrete methods) | ✅ Since Java 8 (default/static methods)                     |
| **Can have fields?**               | ✅ Yes (instance variables, static fields)         | ⚠ Only constants (`public static final`)                    |
| **Constructors allowed?**          | ✅ Yes                                             | ❌ No                                                        |
| **Multiple inheritance?**          | ❌ No (single abstract class only)                 | ✅ Yes (a class can implement multiple interfaces)           |
| **Access modifiers?**              | ✅ Yes (public, protected, etc.)                   | ⚠ Only public (methods in interfaces are public by default) |
| **Suitable for**                   | Closely related classes with shared code          | Unrelated classes needing to follow the same contract       |



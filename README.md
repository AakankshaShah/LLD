# LLD
Link : https://gitlab.com/shrayansh8/interviewcodingpractise/-/tree/main/src/LowLevelDesign
![image](https://github.com/user-attachments/assets/7ef6e3e6-5c7d-4ec1-bc06-02b3e7792e26)

<img width="860" alt="image" src="https://github.com/user-attachments/assets/122af20f-a05c-4127-b89d-5d56c6a99234" />


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

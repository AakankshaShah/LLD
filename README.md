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


Chain of responsibility
```java

package com.conceptandcoding.LowLevelDesign.DesignPatterns.LLDChainResponsibilityDesignPattern;
public class Main {
    public static void main(String args[]) {
        LogProcessor logObject = new InfoLogProcessor(new DebugLogProcessor(new ErrorLogProcessor(null)));
        logObject.log(LogProcessor.ERROR, "exception happens");
        logObject.log(LogProcessor.DEBUG, "need to debug this ");
        logObject.log(LogProcessor.INFO, "just for info ");
    }
}
ackage com.conceptandcoding.LowLevelDesign.DesignPatterns.LLDChainResponsibilityDesignPattern;

public abstract class LogProcessor {

    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    LogProcessor nextLoggerProcessor;

    LogProcessor(LogProcessor loggerProcessor) {

        this.nextLoggerProcessor = loggerProcessor;

    }

    public void log(int logLevel, String message) {

        if (nextLoggerProcessor != null) {
            nextLoggerProcessor.log(logLevel, message);
        }
    }
}


package com.conceptandcoding.LowLevelDesign.DesignPatterns.LLDChainResponsibilityDesignPattern;

public class InfoLogProcessor extends LogProcessor{

    InfoLogProcessor(LogProcessor nexLogProcessor){
        super(nexLogProcessor);
    }

    public void log(int logLevel,String message){

        if(logLevel == INFO) {
            System.out.println("INFO: " + message);
        } else{

            super.log(logLevel, message);
        }

    }
}

```
Proxy Design pattern
```java
public interface InternetAccess {
    void connectTo(String serverHost) throws Exception;
}
public class RealInternetAccess implements InternetAccess {

    private String employeeName;

    public RealInternetAccess(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public void connectTo(String serverHost) throws Exception {
        System.out.println(employeeName + " is connecting to " + serverHost);
    }
}
import java.util.Arrays;
import java.util.List;

public class ProxyInternetAccess implements InternetAccess {

    private String employeeName;
    private RealInternetAccess realAccess;
    private static final List<String> allowedUsers = Arrays.asList("Admin", "CTO", "Manager");

    public ProxyInternetAccess(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public void connectTo(String serverHost) throws Exception {
        if (allowedUsers.contains(employeeName)) {
            realAccess = new RealInternetAccess(employeeName);
            realAccess.connectTo(serverHost);
        } else {
            throw new Exception("Access Denied: " + employeeName + " does not have internet access");
        }
    }
}
public class Main {
    public static void main(String[] args) {
        InternetAccess access1 = new ProxyInternetAccess("Admin");
        InternetAccess access2 = new ProxyInternetAccess("Intern");

        try {
            access1.connectTo("openai.com");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            access2.connectTo("github.com");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

```
Null Object Pattern
```java
interface Vehicle {
    String getType();
    void startEngine();
    boolean isNull();
}
class Car implements Vehicle {
    private String name;

    public Car(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }

    public void startEngine() {
        System.out.println(name + " engine started.");
    }

    public boolean isNull() {
        return false;
    }
}
class NullVehicle implements Vehicle {
    public String getType() {
        return "Unknown Vehicle";
    }

    public void startEngine() {
        System.out.println("No engine to start.");
    }

    public boolean isNull() {
        return true;
    }
}

```
State Desig pattern
```java

package com.conceptandcoding.LowLevelDesign.DesignVendingMachine.VendingStates;
import com.conceptandcoding.LowLevelDesign.DesignVendingMachine.Coin;
import com.conceptandcoding.LowLevelDesign.DesignVendingMachine.Item;
import com.conceptandcoding.LowLevelDesign.DesignVendingMachine.VendingMachine;
import java.util.List;
public interface State {
    public void clickOnInsertCoinButton(VendingMachine machine) throws Exception;
    public void clickOnStartProductSelectionButton(VendingMachine machine) throws Exception;
    public void insertCoin(VendingMachine machine , Coin coin) throws Exception;
    public void chooseProduct(VendingMachine machine, int codeNumber) throws Exception;
    public int getChange(int returnChangeMoney) throws Exception;
    public Item dispenseProduct(VendingMachine machine, int codeNumber) throws Exception;
    public List<Coin> refundFullMoney(VendingMachine machine) throws Exception;
    public void updateInventory(VendingMachine machine, Item item, int codeNumber) throws Exception;
}

package com.conceptandcoding.LowLevelDesign.DesignVendingMachine;

import com.conceptandcoding.LowLevelDesign.DesignVendingMachine.VendingStates.IdleState;
import com.conceptandcoding.LowLevelDesign.DesignVendingMachine.VendingStates.State;

import java.util.*;

public class VendingMachine {
    private State currentState;
    private Map<Integer, Item> inventory = new HashMap<>();
    private List<Coin> coins = new ArrayList<>();

    public VendingMachine() {
        this.currentState = new IdleState();
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Map<Integer, Item> getInventory() {
        return inventory;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    // Delegate actions
    public void clickOnInsertCoinButton() throws Exception {
        currentState.clickOnInsertCoinButton(this);
    }

    public void clickOnStartProductSelectionButton() throws Exception {
        currentState.clickOnStartProductSelectionButton(this);
    }

    public void insertCoin(Coin coin) throws Exception {
        currentState.insertCoin(this, coin);
    }

    public void chooseProduct(int code) throws Exception {
        currentState.chooseProduct(this, code);
    }

    public void dispenseProduct(int code) throws Exception {
        currentState.dispenseProduct(this, code);
    }
}


```
``` java
//ATM
public abstract class ATMState {
    public void insertCard(ATM atm, Card card) { ... }
    public void authenticatePin(ATM atm, Card card, int pin) { ... }
    public void selectOperation(ATM atm, Card card, TransactionType txnType) { ... }
    public void cashWithdrawal(ATM atm, Card card, int amount) { ... }
    public void displayBalance(ATM atm, Card card) { ... }
    public void returnCard() { ... }
    public void exit(ATM atm) { ... }
}
public class IdleState extends ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card inserted.");
        atm.setCurrentATMState(new CardInsertedState());
    }
}

public class IdleState extends ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card inserted.");
        atm.setCurrentATMState(new CardInsertedState());
    }
}
public class PinAuthenticatedState extends ATMState {
    @Override
    public void selectOperation(ATM atm, Card card, TransactionType txnType) {
        switch (txnType) {
            case CASH_WITHDRAWAL -> atm.setCurrentATMState(new CashWithdrawalState());
            case BALANCE_CHECK -> {
                atm.setCurrentATMState(new TransactionState());
                atm.getCurrentATMState().displayBalance(atm, card);
            }
        }
    }
}
public class CashWithdrawalState extends ATMState {
    @Override
    public void cashWithdrawal(ATM atm, Card card, int amount) {
        if (amount > card.getBankBalance()) {
            System.out.println("Insufficient funds.");
            return;
        }

        if (amount > atm.getCash()) {
            System.out.println("ATM has insufficient cash.");
            return;
        }

        atm.getCashDispenser().dispense(amount);
        atm.deductATMBalance(amount);
        card.deductBankBalance(amount);

        System.out.println("Withdrawal successful. Please collect your cash.");
        atm.setCurrentATMState(new IdleState());
    }
}
public class TransactionState extends ATMState {
    @Override
    public void displayBalance(ATM atm, Card card) {
        System.out.println("Your account balance is: ₹" + card.getBankBalance());
        atm.setCurrentATMState(new IdleState());
    }
}
public class ExitState extends ATMState {
    @Override
    public void exit(ATM atm) {
        System.out.println("Thank you. Ejecting card...");
        atm.setCurrentATMState(new IdleState());
    }
}
public class ATM {
    private ATMState currentATMState;
    private int cash;
    private CashDispenser cashDispenser;

    public ATM() {
        this.currentATMState = new IdleState();
        this.cash = 100000; // initial cash
        initializeDispenserChain();
    }

    public void setCurrentATMState(ATMState state) {
        this.currentATMState = state;
    }

    public ATMState getCurrentATMState() {
        return currentATMState;
    }

    public int getCash() {
        return cash;
    }

    public void deductATMBalance(int amount) {
        this.cash -= amount;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }

    private void initializeDispenserChain() {
        this.cashDispenser = new TwoThousandDispenser();
        CashDispenser fiveHundred = new FiveHundredDispenser();
        CashDispenser oneHundred = new OneHundredDispenser();

        cashDispenser.setNext(fiveHundred);
        fiveHundred.setNext(oneHundred);
    }
}
public abstract class CashDispenser {
    protected CashDispenser next;

    public void setNext(CashDispenser next) {
        this.next = next;
    }

    public void dispense(int amount) {
        if (next != null) next.dispense(amount);
    }
}
public class TwoThousandDispenser extends CashDispenser {
    @Override
    public void dispense(int amount) {
        int notes = amount / 2000;
        int remainder = amount % 2000;
        if (notes > 0) System.out.println("Dispensing " + notes + " x ₹2000 notes");
        if (remainder > 0 && next != null) next.dispense(remainder);
    }
}
public class ATMClient {
    public static void main(String[] args) {
        ATM atm = new ATM();
        Card card = new Card(1234, 5000);

        try {
            atm.getCurrentATMState().insertCard(atm, card);
            atm.getCurrentATMState().authenticatePin(atm, card, 1234);
            atm.getCurrentATMState().selectOperation(atm, card, TransactionType.CASH_WITHDRAWAL);
            atm.getCurrentATMState().cashWithdrawal(atm, card, 3700);
            atm.getCurrentATMState().exit(atm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


```
Composite Design Pattern
```
import java.util.ArrayList;
import java.util.List;

// Component
interface FileSystem {
    void ls();  // list files/directories
}

// Leaf
class File implements FileSystem {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void ls() {
        System.out.println("File: " + name);
    }
}

// Composite
class Directory implements FileSystem {
    private String name;
    private List<FileSystem> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystem component) {
        children.add(component);
    }

    public void remove(FileSystem component) {
        children.remove(component);
    }

    public void ls() {
        System.out.println("Directory: " + name);
        for (FileSystem child : children) {
            child.ls();
        }
    }
}

```
Adaptor Design pattern
```java
public interface WeightMachine {
    double getWeightInPound();
}
public class WeightMachineForBabies implements WeightMachine {
    public double getWeightInPound() {
        return 28;
    }
}
public class WeightMachineAdapterImpl implements WeightMachineAdapter {
    private final WeightMachine weightMachine;

    public WeightMachineAdapterImpl(WeightMachine weightMachine) {
        this.weightMachine = weightMachine;
    }

    public double getWeightInKg() {
        return weightMachine.getWeightInPound() * 0.45;
    }
}
public class Main {
    public static void main(String[] args) {
        WeightMachineAdapter adapter = new WeightMachineAdapterImpl(new WeightMachineForBabies());
        System.out.println("Weight in Kg: " + adapter.getWeightInKg());
    }
}
```
Builder Design Pattern
```java
public class Student {
    // Required fields
    private final int id;
    private final String name;

    // Optional fields
    private final int age;
    private final String phone;
    private final String address;

    // Private constructor — only accessible via Builder
    private Student(StudentBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // Builder class (static inner class)
    public static class StudentBuilder {
        private final int id;
        private final String name;

        private int age;
        private String phone;
        private String address;

        public StudentBuilder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public StudentBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public StudentBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public StudentBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", age=" + age +
               ", phone='" + phone + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}

```

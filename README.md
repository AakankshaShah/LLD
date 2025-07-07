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

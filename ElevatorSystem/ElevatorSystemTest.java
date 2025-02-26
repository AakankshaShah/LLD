import java.util.ArrayList;
import java.util.List;

public class ElevatorSystemTest {
    public static void main(String[] args) {
        // Create Elevators
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(new Elevator(1));
        elevators.add(new Elevator(2));

        // Create Elevator Controller
        ElevatorController elevatorController = new ElevatorController();
        elevatorController.elevatorList = elevators;

        // Create External Dispatcher
        ExternalButtonDispatcher externalDispatcher = new ExternalButtonDispatcher(elevatorController);

        // Simulating External Requests
        System.out.println("🔹 External Requests:");
        externalDispatcher.submitRequest(3, Direction.UP);  // Request at Floor 3 going UP
        externalDispatcher.submitRequest(5, Direction.DOWN); // Request at Floor 5 going DOWN

        // Simulating Internal Requests for Elevator 1
        System.out.println("\n🔹 Internal Requests (Elevator 1):");
        elevators.get(0).internalButton.pressButton(4, elevators.get(0));  // Inside Elevator 1, press Floor 4
        elevators.get(0).internalButton.pressButton(2, elevators.get(0));  // Inside Elevator 1, press Floor 2

        // Simulating Internal Requests for Elevator 2
        System.out.println("\n🔹 Internal Requests (Elevator 2):");
        elevators.get(1).internalButton.pressButton(1, elevators.get(1));  // Inside Elevator 2, press Floor 1
        elevators.get(1).internalButton.pressButton(5, elevators.get(1));  // Inside Elevator 2, press Floor 5
    }
}

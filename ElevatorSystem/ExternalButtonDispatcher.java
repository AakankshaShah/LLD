
public class ExternalButtonDispatcher {
    ElevatorController elevatorController;

    public ExternalButtonDispatcher(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    public void submitRequest(int floor, Direction direction) {

        elevatorController.submitExternalRequest(floor, direction);
    }
}


public class InternalDispatcher {
    public void submitInternalRequest(int floor, Elevator elevatorCar){
        if(elevatorCar.currentFloor<floor)
        elevatorCar.moveElevator(floor, Direction.UP);
        else
        elevatorCar.moveElevator(floor, Direction.DOWN);

    }


}

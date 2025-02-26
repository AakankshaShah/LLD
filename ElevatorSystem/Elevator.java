public class Elevator {
    InternalButton internalButton;
    Display display;
    int id;
    int currentFloor;
    Status status;
    Direction direction;

    public Elevator(int id) {
        this.id = id;
        this.direction = Direction.UP;
        this.currentFloor = 0;
        this.status = Status.IDLE;
        this.display = new Display();
        this.internalButton = new InternalButton();
    }

    public void showDisplay() {
        display.showDisplay();
    }

    public void setDisplay() {
        display.setDisplay(currentFloor, direction);
    }

    public boolean moveElevator(int destinationFloor, Direction direction) {
        status = Status.MOVING;
        int startFloor = currentFloor;

        if (direction == Direction.UP) {
            for (int i = startFloor; i <= destinationFloor; i++) {
                this.currentFloor = i;
                this.direction=Direction.UP;
                setDisplay();
                showDisplay();
            }
        } else if (direction == Direction.DOWN) {
            for (int i = startFloor; i >= destinationFloor; i--) {
                this.currentFloor = i;
                this.direction=Direction.DOWN;
                setDisplay();
                showDisplay();
            }
        }

        status = Status.IDLE; // Elevator is now idle after reaching
        return true;
    }
}

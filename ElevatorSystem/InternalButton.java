public class InternalButton {
    InternalDispatcher dispatcher = new InternalDispatcher();
    int[] availableButton={1,2,3,4,5};
    int buttonSelected;
    void pressButton(int destination, Elevator elevatorCar) {
        buttonSelected=destination;
        dispatcher.submitInternalRequest(destination, elevatorCar);
    }

    
}

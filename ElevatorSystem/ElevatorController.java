import java.util.PriorityQueue;
import java.util.List;

public class ElevatorController {
    List<Elevator> elevatorList;
    

    PriorityQueue<Integer> upMinPQ;
    

    PriorityQueue<Integer> downMaxPQ;

    public ElevatorController() {
        upMinPQ = new PriorityQueue<>(); 
        downMaxPQ = new PriorityQueue<>((a, b) -> b - a); 
    }

    public void submitExternalRequest(int floor, Direction direction) {
        if (direction == Direction.UP) {
            upMinPQ.offer(floor);
        } else {
            downMaxPQ.offer(floor);
        }
        assignElevator();
        // Make use of strategy here 
    }

    private void assignElevator() {
        for (Elevator elevator : elevatorList) {
            if (!upMinPQ.isEmpty() && elevator.direction == Direction.UP) {
                elevator.moveElevator(upMinPQ.poll(), Direction.UP);
            } else if (!downMaxPQ.isEmpty() && elevator.direction == Direction.DOWN) {
                elevator.moveElevator(downMaxPQ.poll(), Direction.DOWN);
            }
        }
    }
}

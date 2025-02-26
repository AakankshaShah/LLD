
public class Display {
    int floor;
    Direction direction;

    public void setDisplay(int floor,Direction direction)
    {
        this.floor=floor;
        this.direction=direction;
    }
    public void showDisplay()
    {
        System.out.println("The current floor is "+floor+" it is moving in Direction"+direction);

    }

    
}

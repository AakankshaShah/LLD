public class Floor {
     int floorNumber;
     ExternalButtonDispatcher externalButtonDispatcher;

     public Floor(int floorNumber,ExternalButtonDispatcher externalButtonDispatcher)
     {
        this.floorNumber=floorNumber;
        this.externalButtonDispatcher=externalButtonDispatcher;

     }
     public void pressButton(Direction dir)
     {
        externalButtonDispatcher.submitRequest(floorNumber,dir);

     }
    
}

import java.util.*;

// Enums
enum VehicleType {
    CAR, BIKE, SCOOTY
}

// Vehicle base and derived classes
abstract class Vehicle {
    int vehicleID;
    VehicleType vehicleType;
    int price;
    int seats;

    public Vehicle(int id, VehicleType type, int price, int seats) {
        this.vehicleID = id;
        this.vehicleType = type;
        this.price = price;
        this.seats = seats;
    }

    @Override
    public String toString() {
        return vehicleType + " (ID: " + vehicleID + ", Seats: " + seats + ", Price: $" + price + ")";
    }
}

class Car extends Vehicle {
    public Car(int id, int price, int seats) {
        super(id, VehicleType.CAR, price, seats);
    }
}

class Bike extends Vehicle {
    public Bike(int id, int price, int seats) {
        super(id, VehicleType.BIKE, price, seats);
    }
}

class Scooty extends Vehicle {
    public Scooty(int id, int price, int seats) {
        super(id, VehicleType.SCOOTY, price, seats);
    }
}

// User and Location
class User {
    int userID;
    String name;
    String drivingLicense;

    public User(int userID, String name, String drivingLicense) {
        this.userID = userID;
        this.name = name;
        this.drivingLicense = drivingLicense;
    }
}

class Location {
    String city;
    String address;
    String pincode;

    public Location(String city, String address, String pincode) {
        this.city = city;
        this.address = address;
        this.pincode = pincode;
    }
}

// Reservation
class Reservation {
    int reservationID;
    User user;
    Vehicle vehicle;
    Date startDate;
    Date endDate;

    public Reservation(int id, User user, Vehicle vehicle, Date start, Date end) {
        this.reservationID = id;
        this.user = user;
        this.vehicle = vehicle;
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    public String toString() {
        return "Reservation " + reservationID + ": " + vehicle + " for " + user.name +
               " from " + startDate + " to " + endDate;
    }
}

// Bill
class Bill {
    Reservation reservation;
    double amount;

    public Bill(Reservation reservation) {
        this.reservation = reservation;
        long diff = reservation.endDate.getTime() - reservation.startDate.getTime();
        long days = Math.max(1, diff / (1000 * 60 * 60 * 24));
        this.amount = reservation.vehicle.price * days;
    }

    @Override
    public String toString() {
        return "Bill for Reservation " + reservation.reservationID + ": $" + amount;
    }
}

// Payment Strategy Pattern
interface PaymentStrategy {
    boolean pay(double amount);
}

class CashPayment implements PaymentStrategy {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " in Cash.");
        return true;
    }
}

class CardPayment implements PaymentStrategy {
    private String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using Card ending with " +
            cardNumber.substring(cardNumber.length() - 4));
        return true;
    }
}

class Payment {
    private PaymentStrategy strategy;

    public Payment(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean process(Bill bill) {
        System.out.println("Processing payment of $" + bill.amount + "...");
        return strategy.pay(bill.amount);
    }
}

// Inventory Management per Store
class VehicleInventoryManagement {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public List<Vehicle> getAvailableVehicles(VehicleType type) {
        List<Vehicle> available = new ArrayList<>();
        Date now = new Date();
        for (Vehicle v : vehicles) {
            if (v.vehicleType == type && isAvailable(v, now)) {
                available.add(v);
            }
        }
        return available;
    }

    private boolean isAvailable(Vehicle vehicle, Date date) {
        for (Reservation r : reservations) {
            if (r.vehicle.vehicleID == vehicle.vehicleID &&
                r.startDate.before(date) && r.endDate.after(date)) {
                return false;
            }
        }
        return true;
    }

    public Reservation createReservation(User user, Vehicle vehicle, Date start, Date end) {
        if (!vehicles.contains(vehicle)) {
            System.out.println("Vehicle not in this store's inventory.");
            return null;
        }

        if (!isAvailable(vehicle, start)) {
            System.out.println("Vehicle is not available.");
            return null;
        }

        Reservation r = new Reservation(reservations.size() + 1, user, vehicle, start, end);
        reservations.add(r);
        return r;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

// Store
class Store {
    int storeID;
    Location location;
    VehicleInventoryManagement inventory;

    public Store(int storeID, Location location) {
        this.storeID = storeID;
        this.location = location;
        this.inventory = new VehicleInventoryManagement();
    }

    public void addVehicle(Vehicle vehicle) {
        inventory.addVehicle(vehicle);
    }

    public List<Vehicle> getAvailableVehicles(VehicleType type) {
        return inventory.getAvailableVehicles(type);
    }

    public Reservation reserveVehicle(User user, Vehicle vehicle, Date start, Date end) {
        return inventory.createReservation(user, vehicle, start, end);
    }

    public List<Reservation> getAllReservations() {
        return inventory.getReservations();
    }
}

// Rental Service
class RentalService {
    private List<Store> stores = new ArrayList<>();

    public void addStore(Store store) {
        stores.add(store);
    }

    public List<Vehicle> searchVehicles(VehicleType type) {
        List<Vehicle> results = new ArrayList<>();
        for (Store store : stores) {
            results.addAll(store.getAvailableVehicles(type));
        }
        return results;
    }

    public Reservation reserveAtStore(int storeID, User user, Vehicle vehicle, Date start, Date end) {
        for (Store store : stores) {
            if (store.storeID == storeID) {
                return store.reserveVehicle(user, vehicle, start, end);
            }
        }
        System.out.println("Store not found.");
        return null;
    }

    public void addVehicleToStore(int storeID, Vehicle vehicle) {
        for (Store store : stores) {
            if (store.storeID == storeID) {
                store.addVehicle(vehicle);
                return;
            }
        }
        System.out.println("Store not found.");
    }

    public List<Store> getAllStores() {
        return stores;
    }
}

// Main Class
public class CarRentalSystem {
    public static void main(String[] args) {
        RentalService rentalService = new RentalService();

        // Users
        User alice = new User(1, "Alice", "DL1234");
        User bob = new User(2, "Bob", "DL5678");

        // Locations and Stores
        Store store1 = new Store(101, new Location("New York", "5th Ave", "10001"));
        Store store2 = new Store(102, new Location("Los Angeles", "Sunset Blvd", "90001"));

        rentalService.addStore(store1);
        rentalService.addStore(store2);

        // Vehicles
        Vehicle car1 = new Car(1, 100, 4);
        Vehicle bike1 = new Bike(2, 50, 2);
        Vehicle scooty1 = new Scooty(3, 40, 2);
        Vehicle car2 = new Car(4, 120, 4);

        rentalService.addVehicleToStore(101, car1);
        rentalService.addVehicleToStore(101, bike1);
        rentalService.addVehicleToStore(102, scooty1);
        rentalService.addVehicleToStore(102, car2);

        // View available cars
        System.out.println("\nAvailable Cars:");
        for (Vehicle v : rentalService.searchVehicles(VehicleType.CAR)) {
            System.out.println(v);
        }

        // Make Reservation
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        cal.add(Calendar.DATE, 2);
        Date end = cal.getTime();

        Reservation res = rentalService.reserveAtStore(101, alice, car1, start, end);
        if (res != null) {
            System.out.println("\nReservation Created:");
            System.out.println(res);

            Bill bill = new Bill(res);
            System.out.println(bill);

            PaymentStrategy paymentMethod = new CardPayment("1234567890123456");
            Payment payment = new Payment(paymentMethod);
            payment.process(bill);
        }

        // View Reservations
        System.out.println("\nReservations at Store 101:");
        for (Reservation r : store1.getAllReservations()) {
            System.out.println(r);
        }

        // Check availability again
        System.out.println("\nAvailable Cars after reservation:");
        for (Vehicle v : rentalService.searchVehicles(VehicleType.CAR)) {
            System.out.println(v);
        }
    }
}

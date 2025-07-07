import java.util.*;

enum VehicleType {
    TWO_WHEELER,
    FOUR_WHEELER
}

class Vehicle {
    int vehicleId;
    VehicleType vehicleType;
    Ticket ticket;

    public Vehicle(int vehicleId, VehicleType vehicleType) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
    }
}

class Ticket {
    int ticketId;
    int startTime;
    int endTime;
    ParkingSlot parkingSlot;

    public Ticket(int ticketId, int startTime, ParkingSlot parkingSlot) {
        this.ticketId = ticketId;
        this.startTime = startTime;
        this.parkingSlot = parkingSlot;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}

class Bill {
    int ticketId;
    int cost;

    public Bill(int ticketId, int cost) {
        this.ticketId = ticketId;
        this.cost = cost;
    }
}

interface PricingStrategy {
    int calculatePrice(int startTime, int endTime, int rate);
}

class MinuteBasedStrategy implements PricingStrategy {
    public int calculatePrice(int startTime, int endTime, int rate) {
        return (endTime - startTime) * rate;
    }
}

interface ParkingSlot {
    boolean assignParkingSlot(Vehicle vehicle);

    void freeParkingSlot();

    boolean isAvailable();

    int getRate();

    int getId();
}

class TwoWheelerSlot implements ParkingSlot {
    int id;
    boolean available = true;
    Vehicle vehicle;
    int price;

    public TwoWheelerSlot(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public boolean assignParkingSlot(Vehicle vehicle) {
        if (available) {
            this.vehicle = vehicle;
            this.available = false;
            return true;
        }
        return false;
    }

    public void freeParkingSlot() {
        this.vehicle = null;
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getRate() {
        return price;
    }

    public int getId() {
        return id;
    }
}

class FourWheelerSlot implements ParkingSlot {
    int id;
    boolean available = true;
    Vehicle vehicle;
    int price;

    public FourWheelerSlot(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public boolean assignParkingSlot(Vehicle vehicle) {
        if (available) {
            this.vehicle = vehicle;
            this.available = false;
            return true;
        }
        return false;
    }

    public void freeParkingSlot() {
        this.vehicle = null;
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getRate() {
        return price;
    }

    public int getId() {
        return id;
    }
}

class ParkingSlotManager {
    private List<ParkingSlot> twoWheelerSlots;
    private List<ParkingSlot> fourWheelerSlots;
    private SlotSelectionStrategy selectionStrategy;

    public ParkingSlotManager(List<ParkingSlot> twoWheelerSlots,
            List<ParkingSlot> fourWheelerSlots,
            SlotSelectionStrategy selectionStrategy) {
        this.twoWheelerSlots = twoWheelerSlots;
        this.fourWheelerSlots = fourWheelerSlots;
        this.selectionStrategy = selectionStrategy;
    }

    public ParkingSlot assignSlot(Vehicle vehicle) {
        List<ParkingSlot> slots = getSlotsByType(vehicle.vehicleType);
        ParkingSlot slot = selectionStrategy.selectSlot(slots);
        if (slot != null && slot.assignParkingSlot(vehicle)) {
            return slot;
        }
        return null;
    }

    public void releaseSlot(ParkingSlot slot) {
        slot.freeParkingSlot();
    }

    private List<ParkingSlot> getSlotsByType(VehicleType type) {
        return (type == VehicleType.TWO_WHEELER) ? twoWheelerSlots : fourWheelerSlots;
    }
}

interface SlotSelectionStrategy {
    ParkingSlot selectSlot(List<ParkingSlot> slots);
}

class NearestSlotStrategy implements SlotSelectionStrategy {
    @Override
    public ParkingSlot selectSlot(List<ParkingSlot> slots) {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                return slot;
            }
        }
        return null;
    }
}

class EntryGate {
    private ParkingSlotManager slotManager;
    private static int ticketCounter = 1;

    public EntryGate(ParkingSlotManager slotManager) {
        this.slotManager = slotManager;
    }

    public Ticket allotParkingSpot(Vehicle vehicle, int startTime) {
        ParkingSlot slot = slotManager.assignSlot(vehicle);
        if (slot != null) {
            Ticket ticket = new Ticket(ticketCounter++, startTime, slot);
            vehicle.ticket = ticket;
            return ticket;
        }
        return null; // No slot available
    }
}

class ExitGate {
    private PricingStrategy pricingStrategy;

    public ExitGate(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public Bill generateBill(Ticket ticket, int endTime) {
        ticket.setEndTime(endTime);
        int rate = ticket.parkingSlot.getRate();
        int cost = pricingStrategy.calculatePrice(ticket.startTime, endTime, rate);
        ticket.parkingSlot.freeParkingSlot();
        return new Bill(ticket.ticketId, cost);
    }
}

class ParkingSpace {
    List<ParkingSlot> twoWheelerSlots = new ArrayList<>();
    List<ParkingSlot> fourWheelerSlots = new ArrayList<>();
    EntryGate entryGate;
    ExitGate exitGate;

    public ParkingSpace() {
        for (int i = 1; i <= 5; i++) {
            twoWheelerSlots.add(ParkingSlotFactory.createSlot(i, VehicleType.TWO_WHEELER, 1));
            fourWheelerSlots.add(ParkingSlotFactory.createSlot(i, VehicleType.FOUR_WHEELER, 2));
        }

        SlotSelectionStrategy slotStrategy = new NearestSlotStrategy();

        ParkingSlotManager slotManager = new ParkingSlotManager(twoWheelerSlots, fourWheelerSlots, slotStrategy);
        entryGate = new EntryGate(slotManager);
        exitGate = new ExitGate(new MinuteBasedStrategy());
    }

    public EntryGate getEntryGate() {
        return entryGate;
    }

    public ExitGate getExitGate() {
        return exitGate;
    }
}

class ParkingSlotFactory {
    public static ParkingSlot createSlot(int id, VehicleType type, int price) {
        switch (type) {
            case TWO_WHEELER:
                return new TwoWheelerSlot(id, price);
            case FOUR_WHEELER:
                return new FourWheelerSlot(id, price);
            default:
                throw new IllegalArgumentException("Unknown VehicleType: " + type);
        }
    }
}

public class ParkingLot {
    public static void main(String[] args) {
        ParkingSpace lot = new ParkingSpace();

        Vehicle bike = new Vehicle(1, VehicleType.TWO_WHEELER);
        Ticket ticket = lot.getEntryGate().allotParkingSpot(bike, 10); // entry at 10th minute

        Bill bill = lot.getExitGate().generateBill(ticket, 40); // exit at 40th minute

        System.out.println("Ticket ID: " + bill.ticketId);
        System.out.println("Total cost: " + bill.cost);
    }
}

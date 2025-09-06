import java.util.*;
import java.math.BigDecimal;
import java.time.Instant;

// ------------------- Ticket & Observer -------------------
interface TicketObserver {
    void onTicketConfirmed(Ticket ticket);
    void onTicketCancelled(Ticket ticket);
}

class SmsNotifier implements TicketObserver {
    public void onTicketConfirmed(Ticket ticket) {
        System.out.println("📲 SMS sent for ticket " + ticket.pnr);
    }
    public void onTicketCancelled(Ticket ticket) {
        System.out.println("📲 SMS cancellation sent for ticket " + ticket.pnr);
    }
}

class EmailNotifier implements TicketObserver {
    public void onTicketConfirmed(Ticket ticket) {
        System.out.println("📧 Email sent for ticket " + ticket.pnr);
    }
    public void onTicketCancelled(Ticket ticket) {
        System.out.println("📧 Email cancellation sent for ticket " + ticket.pnr);
    }
}

class Ticket {
    enum Status { HELD, CONFIRMED, CANCELLED, EXPIRED }

    UUID id;
    UUID userId;
    UUID journeyId;
    Status status;
    Instant createdAt;
    List<BookingItem> items = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;
    String pnr;
    PricingStrategy pricingStrategy;

    private List<TicketObserver> observers = new ArrayList<>();

    Ticket(UUID userId, UUID journeyId, PricingStrategy strategy) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.journeyId = journeyId;
        this.status = Status.HELD;
        this.createdAt = Instant.now();
        this.pnr = "PNR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.pricingStrategy = strategy;
    }

    void addObserver(TicketObserver observer) {
        observers.add(observer);
    }

    void notifyConfirmed() {
        for (TicketObserver o : observers) o.onTicketConfirmed(this);
    }

    void notifyCancelled() {
        for (TicketObserver o : observers) o.onTicketCancelled(this);
    }

    void addSeat(Seat seat, String compartmentType) {
        BigDecimal price = pricingStrategy.calculatePrice(compartmentType);
        items.add(new BookingItem(seat.id, price));
        totalPrice = totalPrice.add(price);
    }

    void confirm() {
        this.status = Status.CONFIRMED;
        notifyConfirmed();
    }

    void cancel() {
        this.status = Status.CANCELLED;
        notifyCancelled();
    }
}

// ------------------- BookingItem -------------------
class BookingItem {
    UUID id;
    UUID seatInstanceId;
    BigDecimal price;

    BookingItem(UUID seatInstanceId, BigDecimal price) {
        this.id = UUID.randomUUID();
        this.seatInstanceId = seatInstanceId;
        this.price = price;
    }
}

// ------------------- User -------------------
class User {
    UUID id;
    String name;
    int age;
    String gender;
    String idProof;

    User(String name, int age, String gender, String idProof) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.idProof = idProof;
    }
}

// ------------------- Train, Compartment, Seat -------------------
class Train {
    UUID id;
    String trainNumber;
    String name;
    List<Compartment> coaches = new ArrayList<>();

    Train(String trainNumber, String name) {
        this.id = UUID.randomUUID();
        this.trainNumber = trainNumber;
        this.name = name;
    }
}

class Compartment {
    UUID id;
    UUID trainId;
    String type;
    int capacity;
    List<Seat> seats = new ArrayList<>();

    Compartment(UUID trainId, String type, int capacity) {
        this.id = UUID.randomUUID();
        this.trainId = trainId;
        this.type = type;
        this.capacity = capacity;
        this.seats = SeatFactory.createSeats(this.id, type, capacity);
    }
}

class Seat {
    UUID id;
    UUID coachId;
    String seatNumber;

    Seat(UUID coachId, String seatNumber) {
        this.id = UUID.randomUUID();
        this.coachId = coachId;
        this.seatNumber = seatNumber;
    }
}

class SeatFactory {
    public static List<Seat> createSeats(UUID coachId, String type, int capacity) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            String seatNum;
            switch (type.toUpperCase()) {
                case "SLEEPER": seatNum = "S-" + i; break;
                case "AC": seatNum = "A-" + i; break;
                case "CHAIR": seatNum = "C-" + i; break;
                default: seatNum = "X-" + i;
            }
            seats.add(new Seat(coachId, seatNum));
        }
        return seats;
    }
}

// ------------------- Journey -------------------
class Journey {
    UUID id;
    UUID trainId;
    Date journeyDate;
    String sourceStation;
    String destinationStation;

    Journey(UUID trainId, Date journeyDate, String source, String dest) {
        this.id = UUID.randomUUID();
        this.trainId = trainId;
        this.journeyDate = journeyDate;
        this.sourceStation = source;
        this.destinationStation = dest;
    }
}

// ------------------- Pricing Strategy -------------------
interface PricingStrategy {
    BigDecimal calculatePrice(String compartmentType);
}

class SleeperPricing implements PricingStrategy {
    public BigDecimal calculatePrice(String compartmentType) { return BigDecimal.valueOf(200); }
}

class AcPricing implements PricingStrategy {
    public BigDecimal calculatePrice(String compartmentType) { return BigDecimal.valueOf(500); }
}

class ChairCarPricing implements PricingStrategy {
    public BigDecimal calculatePrice(String compartmentType) { return BigDecimal.valueOf(300); }
}

// ------------------- BookingRegistry (Thread-Safe) -------------------
class BookingRegistry {
    private static BookingRegistry instance;
    private final Map<UUID, Set<UUID>> bookedSeats = new HashMap<>();

    private BookingRegistry() {}

    public static synchronized BookingRegistry getInstance() {
        if (instance == null) instance = new BookingRegistry();
        return instance;
    }

    public synchronized boolean tryBook(UUID journeyId, UUID seatId) {
        Set<UUID> seats = bookedSeats.computeIfAbsent(journeyId, k -> new HashSet<>());
        if (seats.contains(seatId)) return false;
        seats.add(seatId);
        return true;
    }
}

// ------------------- Booking Service -------------------
class BookingService {
    public Ticket bookTicket(User user, Journey journey, List<Seat> seats, String compartmentType, PricingStrategy strategy) {
        Ticket ticket = new Ticket(user.id, journey.id, strategy);
        ticket.addObserver(new SmsNotifier());
        ticket.addObserver(new EmailNotifier());

        BookingRegistry registry = BookingRegistry.getInstance();

        for (Seat seat : seats) {
            if (!registry.tryBook(journey.id, seat.id)) {
                System.out.println("❌ Seat " + seat.seatNumber + " already booked for this journey!");
                continue;
            }
            ticket.addSeat(seat, compartmentType);
        }

        if (ticket.items.isEmpty()) {
            System.out.println("❌ No seats booked. Ticket not created.");
            return null;
        }

        ticket.confirm();

        System.out.println("✅ Ticket booked! PNR: " + ticket.pnr +
                " | Seats: " + ticket.items.size() +
                " | Passenger: " + user.name +
                " | Total Price: ₹" + ticket.totalPrice);
        return ticket;
    }
}

// ------------------- Main -------------------
public class TicketBookingSystem {
    public static void main(String[] args) {
        Train train = new Train("12345", "Express");
        Compartment sleeperCoach = new Compartment(train.id, "Sleeper", 3);
        Compartment acCoach = new Compartment(train.id, "AC", 2);
        train.coaches.add(sleeperCoach);
        train.coaches.add(acCoach);

        Journey journey = new Journey(train.id, new Date(), "Delhi", "Mumbai");
        User alice = new User("Alice", 28, "F", "A12345");

        BookingService bookingService = new BookingService();

        // Alice books 2 sleeper seats
        bookingService.bookTicket(alice, journey,
                Arrays.asList(sleeperCoach.seats.get(0), sleeperCoach.seats.get(1)),
                sleeperCoach.type,
                new SleeperPricing());

        // Alice tries to book the same seat again (should fail)
        bookingService.bookTicket(alice, journey,
                Arrays.asList(sleeperCoach.seats.get(0)),
                sleeperCoach.type,
                new SleeperPricing());

        // Alice books 1 AC seat
        bookingService.bookTicket(alice, journey,
                Arrays.asList(acCoach.seats.get(0)),
                acCoach.type,
                new AcPricing());
    }
}

```
import java.util.*;

// ========== ENUMS ==========
enum Severity {
    HIGH, MEDIUM, LOW
}

enum Channel {
    EMAIL, PHONE, MESSAGE
}

// ========== STRATEGY INTERFACE ==========
interface NotificationStrategy {
    void send(String recipient, String message);
}

// ========== CONCRETE STRATEGIES ==========
class EmailStrategy implements NotificationStrategy {
    public void send(String recipient, String message) {
        System.out.println("[EMAIL] Sending to " + recipient + ": " + message);
    }
}

class PhoneStrategy implements NotificationStrategy {
    public void send(String recipient, String message) {
        System.out.println("[PHONE] Calling/SMS to " + recipient + ": " + message);
    }
}

class MessageStrategy implements NotificationStrategy {
    public void send(String recipient, String message) {
        System.out.println("[MESSAGE] Push sent to " + recipient + ": " + message);
    }
}

// ========== SUBSCRIBER ==========
class Subscriber {
    String name;
    Map<Severity, Set<Channel>> preferences = new HashMap<>();
    Map<Channel, String> contactInfo = new HashMap<>(); // e.g., EMAIL → user@x.com

    public Subscriber(String name) {
        this.name = name;
    }

    public void setPreference(Severity severity, Channel... channels) {
        preferences.put(severity, new HashSet<>(Arrays.asList(channels)));
    }

    public Set<Channel> getChannels(Severity severity) {
        return preferences.getOrDefault(severity, new HashSet<>());
    }

    public void setContact(Channel channel, String contact) {
        contactInfo.put(channel, contact);
    }

    public String getContact(Channel channel) {
        return contactInfo.get(channel);
    }
}

// ========== CLIENT ==========
class Client {
    String name;
    List<Subscriber> subscribers = new ArrayList<>();

    public Client(String name) {
        this.name = name;
    }

    public void addSubscriber(Subscriber s) {
        subscribers.add(s);
    }
}

// ========== STRATEGY FACTORY ==========
class StrategyFactory {
    private static final Map<Channel, NotificationStrategy> strategyMap = Map.of(
        Channel.EMAIL, new EmailStrategy(),
        Channel.PHONE, new PhoneStrategy(),
        Channel.MESSAGE, new MessageStrategy()
    );

    public static NotificationStrategy getStrategy(Channel channel) {
        return strategyMap.get(channel);
    }
}

// ========== NOTIFICATION SERVICE ==========
class NotificationService {
    public void notifyClient(Client client, Severity severity, String message) {
        System.out.println("\n🔔 Sending " + severity + " notification for client: " + client.name);
        for (Subscriber s : client.subscribers) {
            for (Channel channel : s.getChannels(severity)) {
                NotificationStrategy strategy = StrategyFactory.getStrategy(channel);
                String recipient = s.getContact(channel);
                strategy.send(recipient, message);
            }
        }
    }
}

// ========== DEMO ==========
public class Main {
    public static void main(String[] args) {

        // Create client AWS
        Client aws = new Client("AWS");

        // Subscriber Aakanksha
        Subscriber aakanksha = new Subscriber("Aakanksha");
        aakanksha.setPreference(Severity.LOW, Channel.PHONE);
        aakanksha.setContact(Channel.PHONE, "+91-9999999999");

        // Subscriber Amrit
        Subscriber amrit = new Subscriber("Amrit");
        amrit.setPreference(Severity.HIGH, Channel.EMAIL);
        amrit.setContact(Channel.EMAIL, "amrit@aws.com");

        // Add subscribers
        aws.addSubscriber(aakanksha);
        aws.addSubscriber(amrit);

        // Notify
        NotificationService service = new NotificationService();
        service.notifyClient(aws, Severity.HIGH, "AWS High CPU usage detected!");
        service.notifyClient(aws, Severity.LOW, "AWS maintenance completed successfully.");
    }
}

```

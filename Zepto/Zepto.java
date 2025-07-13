



import java.util.*;

class Address {
    private String street, city, state, zip;
    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}


class ProductCategory {
    private String id, name;
    public ProductCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }
}


class Product {
    private String id, name;
    private double price;
    private ProductCategory category;

    public Product(String id, String name, double price, ProductCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public double getPrice() { return price; }
    public String getName() { return name; }
}

// Cart.java


class Cart {
    private Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p, int qty) {
        items.put(p, items.getOrDefault(p, 0) + qty);
    }

    public Map<Product, Integer> getItems() {
        return items;
    }
}

// User.java
class User {
    private String id, name;
    private Address address;
    private Cart cart = new Cart();

    public User(String id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Cart getCart() { return cart; }
    public Address getAddress() { return address; }
}

// UserController.java


class UserController {
    private Map<String, User> users = new HashMap<>();

    public User registerUser(String id, String name, Address address) {
        User user = new User(id, name, address);
        users.put(id, user);
        return user;
    }

    public User getUser(String id) {
        return users.get(id);
    }
}

// Inventory.java


class Inventory {
    private Map<Product, Integer> stock = new HashMap<>();

    public void addStock(Product p, int qty) {
        stock.put(p, stock.getOrDefault(p, 0) + qty);
    }

    public boolean hasStock(Product p, int qty) {
        return stock.getOrDefault(p, 0) >= qty;
    }

    public void reduceStock(Product p, int qty) {
        if (hasStock(p, qty)) {
            stock.put(p, stock.get(p) - qty);
        }
    }
}

// Warehouse.java
class Warehouse {
    private String id;
    private Address location;
    private Inventory inventory = new Inventory();

    public Warehouse(String id, Address location) {
        this.id = id;
        this.location = location;
    }

    public Inventory getInventory() { return inventory; }
    public Address getLocation() { return location; }
    public String getId() { return id; }
}

// WarehouseController.java


class WarehouseController {
    private Map<String, Warehouse> warehouses = new HashMap<>();

    public Warehouse createWarehouse(String id, Address location) {
        Warehouse warehouse = new Warehouse(id, location);
        warehouses.put(id, warehouse);
        return warehouse;
    }

    public Warehouse getWarehouse(String id) {
        return warehouses.get(id);
    }

    public List<Warehouse> getAllWarehouses() {
        return new ArrayList<>(warehouses.values());
    }
}

// WarehouseSelectionStrategy.java


interface WarehouseSelectionStrategy {
    Warehouse selectWarehouse(User user, List<Warehouse> warehouses, Map<Product, Integer> items);
}

// NearestWarehouseSelectionStrategy.java
class NearestWarehouseSelectionStrategy implements WarehouseSelectionStrategy {
    public Warehouse selectWarehouse(User user, List<Warehouse> warehouses, Map<Product, Integer> items) {
        for (Warehouse w : warehouses) {
            boolean allAvailable = true;
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                if (!w.getInventory().hasStock(entry.getKey(), entry.getValue())) {
                    allAvailable = false;
                    break;
                }
            }
            if (allAvailable) return w;
        }
        return null;
    }
}

// OrderStatus.java
enum OrderStatus {
    CREATED, SHIPPED, DELIVERED
}

// Order.java


class Order {
    private String orderId;
    private User user;
    private Warehouse warehouse;
    private Map<Product, Integer> items;
    private OrderStatus status;
    private Invoice invoice; // Link invoice directly

    public Order(String orderId, User user, Warehouse warehouse, Map<Product, Integer> items, OrderStatus status) {
        this.orderId = orderId;
        this.user = user;
        this.warehouse = warehouse;
        this.items = items;
        this.status = status;
        this.invoice = new Invoice("inv-" + orderId, this); // Create invoice at order placement
    }

    public Map<Product, Integer> getItems() { return items; }
    public String getOrderId() { return orderId; }
    public Invoice getInvoice() { return invoice; }
}

// Invoice.java
class Invoice {
    private String invoiceId;
    private Order order;

    public Invoice(String invoiceId, Order order) {
        this.invoiceId = invoiceId;
        this.order = order;
    }

    public double getTotal() {
        return order.getItems().entrySet().stream()
            .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
            .sum();
    }
}

// Payment.java
class Payment {
    private String paymentId;
    private double amount;
    private String mode;

    public Payment(String paymentId, double amount, String mode) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.mode = mode;
    }

    public String getPaymentId() { return paymentId; }
}

// PaymentMode.java
interface PaymentMode {
    Payment pay(Invoice invoice);
}

// CardPaymentMode.java
class CardPaymentMode implements PaymentMode {
    public Payment pay(Invoice invoice) {
        return new Payment(UUID.randomUUID().toString(), invoice.getTotal(), "Card");
    }
}

// UpiPaymentMode.java
class UpiPaymentMode implements PaymentMode {
    public Payment pay(Invoice invoice) {
        return new Payment(UUID.randomUUID().toString(), invoice.getTotal(), "UPI");
    }
}

// OrderController.java
class OrderController {
    public Order placeOrder(User user, Warehouse w) {
        Order order = new Order(UUID.randomUUID().toString(), user, w, user.getCart().getItems(), OrderStatus.CREATED);
        for (Map.Entry<Product, Integer> e : user.getCart().getItems().entrySet()) {
            w.getInventory().reduceStock(e.getKey(), e.getValue());
        }
        return order;
    }
}

// Main.java


public class Zepto {
    public static void main(String[] args) {
        Product apple = new Product("p1", "Apple", 10.0, new ProductCategory("c1", "Fruits"));
        Address userAddr = new Address("MG Rd", "Bangalore", "KA", "560001");

        UserController userController = new UserController();
        User user = userController.registerUser("u1", "Ravi", userAddr);
        user.getCart().addProduct(apple, 3);

        WarehouseController warehouseController = new WarehouseController();
        Warehouse warehouse = warehouseController.createWarehouse("w1", userAddr);
        warehouse.getInventory().addStock(apple, 10);

        WarehouseSelectionStrategy selector = new NearestWarehouseSelectionStrategy();
        Warehouse selected = selector.selectWarehouse(user, warehouseController.getAllWarehouses(), user.getCart().getItems());

        OrderController oc = new OrderController();
        Order order = oc.placeOrder(user, selected);

        Invoice invoice = order.getInvoice();
        Payment payment = new CardPaymentMode().pay(invoice);

        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Invoice Total: " + invoice.getTotal());
        System.out.println("Payment ID: " + payment.getPaymentId());
    }
}

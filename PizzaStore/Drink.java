package PizzaStore;

import java.math.BigDecimal;

public class Drink implements OrderItem {
    private String name;
    private BigDecimal price;

    public Drink(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
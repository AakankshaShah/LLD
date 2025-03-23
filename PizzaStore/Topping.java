package PizzaStore;

import java.math.BigDecimal;

public class Topping implements OrderItem {
    private String name;
    private BigDecimal price;

    public Topping(String name, BigDecimal price) {
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

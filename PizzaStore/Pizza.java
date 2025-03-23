package PizzaStore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pizza implements OrderItem {
    private Base pizzaBase;
    private List<Topping> toppings;

    public Pizza(Base pizzaBase) {
        this.pizzaBase = pizzaBase;
        this.toppings = new ArrayList<>();
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void addTopping(Topping topping) {
        this.toppings.add(topping);
    }

    @Override
    public BigDecimal getPrice() {
        BigDecimal totalPrice = pizzaBase.getPrice();
        for (Topping topping : toppings) {
            totalPrice = totalPrice.add(topping.getPrice());
        }
        return totalPrice;
    }
}
package PizzaStore;

import java.math.BigDecimal;
import java.util.Comparator;

public class MostExpensiveToppingFreeDeal implements Deal {
    @Override
    public BigDecimal calculateDiscount(Order order) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            if (item instanceof Pizza) {
                Pizza pizza = (Pizza) item;
                if (!pizza.getToppings().isEmpty()) {
                    Topping mostExpensiveTopping = pizza.getToppings().stream()
                            .max(Comparator.comparing(Topping::getPrice))
                            .orElse(null);
                    if (mostExpensiveTopping != null) {
                        totalDiscount = totalDiscount.add(mostExpensiveTopping.getPrice());
                    }
                }
            }
        }
        System.out.println("Discount for MostExpensiveToppingFreeDeal : " + totalDiscount);
        return totalDiscount;
    }
}

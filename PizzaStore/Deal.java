package PizzaStore;

import java.math.BigDecimal;

public interface Deal {
    BigDecimal calculateDiscount(Order order);
}

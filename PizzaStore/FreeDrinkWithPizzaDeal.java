package PizzaStore;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

public class FreeDrinkWithPizzaDeal implements Deal {
    @Override
    public BigDecimal calculateDiscount(Order order) {
        boolean hasPizza = order.getItems().stream().anyMatch(item -> item instanceof Pizza);
        if (!hasPizza) {
            return BigDecimal.ZERO;
        }

        Optional<Drink> cheapestDrink = order.getItems().stream()
                .filter(item -> item instanceof Drink)
                .map(item -> (Drink) item)
                .min(Comparator.comparing(Drink::getPrice));

        BigDecimal discount = cheapestDrink.map(Drink::getPrice).orElse(BigDecimal.ZERO);
        System.out.println("Discount for FreeDrinkWithPizzaDeal : " + discount);
        return discount;
    }
}
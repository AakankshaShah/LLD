package PizzaStore;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BuyOneGetOneFreePizzaDeal implements Deal {
    @Override
    public BigDecimal calculateDiscount(Order order) {
        List<Pizza> pizzas = order.getItems().stream()
                .filter(item -> item instanceof Pizza)
                .map(item -> (Pizza) item)
                .sorted(Comparator.comparing(Pizza::getPrice))
                .collect(Collectors.toList());

        int pairs = pizzas.size() / 2;
        BigDecimal discount = BigDecimal.ZERO;
        for (int i = 0; i < pairs; i++) {
            discount = discount.add(pizzas.get(i * 2).getPrice());
        }
        System.out.println("Discount for BuyOneGetOneFreePizzaDeal : " + discount);
        return discount;
    }
}

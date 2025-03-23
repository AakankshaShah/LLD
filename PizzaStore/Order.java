package PizzaStore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private Store store;
    private List<OrderItem> items;

    public Order(Store store) {
        this.store = store;
        this.items = new ArrayList<>();
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public BigDecimal calculateTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            subTotal = subTotal.add(item.getPrice());
        }

        BigDecimal discounts = BigDecimal.ZERO;
        for (Deal deal : store.getDeals()) {
            discounts = discounts.add(deal.calculateDiscount(this));
        }

        System.out.println("-------------------------");
        System.out.println("SubTotal : " + subTotal);
        System.out.println("Discounts : " + discounts);
        return subTotal.subtract(discounts);
    }
}

package PizzaStore;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // Configure store
        Store londonStore = new Store("London");
        londonStore.addBasePrice("Thin Crust", new BigDecimal("5.0"));
        londonStore.addToppingPrice("Cheese", new BigDecimal("1.0"));
        londonStore.addToppingPrice("Pepperoni", new BigDecimal("2.0"));
        londonStore.addDrinkPrice("Cola", new BigDecimal("3.0"));

        londonStore.addDeal(new BuyOneGetOneFreePizzaDeal());
        londonStore.addDeal(new FreeDrinkWithPizzaDeal());
        londonStore.addDeal(new MostExpensiveToppingFreeDeal());

        // Create pizzas
        Pizza thinCrustCheesePepperoniPizza = londonStore.buildPizza()
                .withBase("Thin Crust")
                .addTopping("Cheese")
                .addTopping("Pepperoni")
                .build();

        Pizza thinCrustCheesePizza = londonStore.buildPizza()
                .withBase("Thin Crust")
                .addTopping("Cheese")
                .build();

        // Create Sides
        Drink cola = londonStore.createDrink("Cola");

        // Create order
        Order order = new Order(londonStore);
        order.addItem(thinCrustCheesePepperoniPizza);
        order.addItem(thinCrustCheesePizza);
        order.addItem(cola);

        System.out.println("-------------------------");
        System.out.println("Total Price: " + order.calculateTotal());
    }
}

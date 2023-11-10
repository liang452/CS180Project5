package src;/*
 */

import java.io.IOException;

public class Customer extends User {
    public Customer(String email, String password) throws IOException {
        super(email, password);
    }
    public void purchase() {

    }
    public void addToCart() {

    }
    public void pastPurchases() {

    }
    public void viewCart() {

    }
    public void viewDashBord() {
        //dashboard
    }

}


// I wrote some code below for the methods above, you can use it 
// when you write the class

/* import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Customer extends User {
    private List<ShoppingCartEntry> shoppingCart;
    private List<Order> pastOrders;
    public Customer(String email, String password) {
        super(email, password);
        this.shoppingCart = new ArrayList<>();
        this.pastOrders = new ArrayList<>();
    }
    public void removeFromShoppingCart(Seller seller, Product product) {
        ShoppingCartEntry entryToRemove = null;
        for (ShoppingCartEntry entry : shoppingCart) {
            if (entry.getSeller().equals(seller) && entry.getProduct().equals(product)) {
                entryToRemove = entry;
                break;
            }
        }
        if (entryToRemove != null) {
            shoppingCart.remove(entryToRemove);
            System.out.println("Product removed from the shopping cart!");
        } else {
            System.out.println("Product not found in the shopping cart.");
        }
    }
    public void purchase() {
        Order newOrder = new Order(this, shoppingCart);
        pastOrders.add(newOrder);
        shoppingCart.clear();
        System.out.println("Purchase completed!");
    }
    public void addToShoppingCart(Seller seller, Product product) {
        shoppingCart.add(new ShoppingCartEntry(seller, product));
        System.out.println("Product added to the shopping cart!");
    }
    public void pastPurchases() {
        System.out.println("Past Purchases:");
        for (Order order : pastOrders) {
            System.out.println("Order ID: " + order.getOrderID());
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                System.out.println("Store: " + entry.getSeller().getEmail());
                System.out.println("  Product: " + entry.getProduct().getName());
                System.out.println("  Quantity: " + entry.getProduct().getQuantity());
                System.out.println("  Price: $" + entry.getProduct().getPrice());
                System.out.println("  -------------");
            }
            System.out.println();
        }
    }
    // Method to view the shopping cart
    public void viewCart() {
        System.out.println("Shopping Cart:");
        for (ShoppingCartEntry entry : shoppingCart) {
            System.out.println("Store: " + entry.getSeller().getEmail());
            System.out.println("  Product: " + entry.getProduct().getName());
            System.out.println("  Quantity: " + entry.getProduct().getQuantity());
            System.out.println("  Price: $" + entry.getProduct().getPrice());
            System.out.println("  -------------");
        }
    }
    public void viewDashboard() {
        List<Seller> storesByProductsSold = getStoresSortedByProductsSold();
        List<Seller> storesByCustomerPurchases = getStoresSortedByCustomerPurchases();
        System.out.println("Dashboard:");
        System.out.println("1. Stores by Products Sold:");
        displayStores(storesByProductsSold);
        System.out.println("\n2. Stores by Customer Purchases:");
        displayStores(storesByCustomerPurchases);
    }
    private List<Seller> getStoresSortedByProductsSold() {
        Map<Seller, Integer> productsSoldMap = new HashMap<>();
        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                Seller seller = entry.getSeller();
                int productsSold = productsSoldMap.getOrDefault(seller, 0);
                productsSoldMap.put(seller, productsSold + entry.getProduct().getQuantity());
            }
        }
        List<Map.Entry<Seller, Integer>> list = new ArrayList<>(productsSoldMap.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        List<Seller> sortedStores = new ArrayList<>();
        for (Map.Entry<Seller, Integer> entry : list) {
            sortedStores.add(entry.getKey());
        }
        return sortedStores;
    }
    private List<Seller> getStoresSortedByCustomerPurchases() {
        Map<Seller, Integer> customerPurchasesMap = new HashMap<>();
        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                Seller seller = entry.getSeller();
                int customerPurchases = customerPurchasesMap.getOrDefault(seller, 0);
                customerPurchasesMap.put(seller, customerPurchases + 1);
            }
        }
        List<Map.Entry<Seller, Integer>> list = new ArrayList<>(customerPurchasesMap.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        List<Seller> sortedStores = new ArrayList<>();
        for (Map.Entry<Seller, Integer> entry : list) {
            sortedStores.add(entry.getKey());
        }
        return sortedStores;
    }
    private void displayStores(List<Seller> stores) {
        for (Seller store : stores) {
            System.out.println("Store: " + store.getEmail());
            System.out.println("  Products Sold: " + getProductsSoldByStore(store));
            System.out.println("  Customer Purchases: " + getCustomerPurchasesByStore(store));
            System.out.println("  -------------");
        }
    }
    private int getProductsSoldByStore(Seller store) {
        int productsSold = 0;
        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                if (entry.getSeller().equals(store)) {
                    productsSold += entry.getProduct().getQuantity();
                }
            }
        }
        return productsSold;
    }
    private int getCustomerPurchasesByStore(Seller store) {
        int customerPurchases = 0;
        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                if (entry.getSeller().equals(store)) {
                    customerPurchases++;
                }
            }
        }
        return customerPurchases;
    }
}
*/

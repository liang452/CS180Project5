import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Customer extends User {
    private HashMap<Product, Integer> cart;
    public Customer(String username, String email, String password) throws IOException {
        super(username, email, password);
        File f = new File(username);
        if (!f.exists()) {
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.write("CUSTOMER");
        } else {
            //if existing user, basically.
        }
        //TODO: load cart in from file
    }
    public boolean purchase(Product product, int purchaseAmount) {
        //calls the removeQuantity method
        product.removeQuantity(purchaseAmount);
        return true;
        //return true if purchase was successful, return false if not
    }
    public void addToCart(Product product, int quantity) {
        cart.put(product, quantity);
    }
    public void removeFromCart(Product product, int quantity) {
        //remove an amount of item from cart, and if amount is equal to total amount, remove the item entirely
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

/* import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        List<StoreStatistics> storesByProductsSold = getStoresSortedByProductsSold();
        List<StoreStatistics> storesByCustomerPurchases = getStoresSortedByCustomerPurchases();

        System.out.println("Dashboard:");
        System.out.println("1. Stores by Products Sold:");
        displayStores(storesByProductsSold);
        System.out.println("\n2. Stores by Customer Purchases:");
        displayStores(storesByCustomerPurchases);
    }

    public void exportPurchaseHistory(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Order order : pastOrders) {
                writer.write("Order ID: " + order.getOrderID() + "\n");
                for (ShoppingCartEntry entry : order.getOrderedItems()) {
                    writer.write("Store: " + entry.getSeller().getEmail() + "\n");
                    writer.write("  Product: " + entry.getProduct().getName() + "\n");
                    writer.write("  Quantity: " + entry.getProduct().getQuantity() + "\n");
                    writer.write("  Price: $" + entry.getProduct().getPrice() + "\n");
                    writer.write("  -------------\n");
                }
                writer.write("\n");
            }
            System.out.println("Purchase history exported to " + filename);
        } catch (IOException e) {
            System.err.println("Error exporting purchase history: " + e.getMessage());
        }
    }

    private List<StoreStatistics> getStoresSortedByProductsSold() {
        List<StoreStatistics> storeStatisticsList = new ArrayList<>();

        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                Seller seller = entry.getSeller();
                StoreStatistics storeStatistics = getStoreStatistics(storeStatisticsList, seller);
                storeStatistics.incrementProductsSold(entry.getProduct().getQuantity());
            }
        }

        storeStatisticsList.sort(Comparator.comparingInt(StoreStatistics::getProductsSold).reversed());
        return storeStatisticsList;
    }

    private List<StoreStatistics> getStoresSortedByCustomerPurchases() {
        List<StoreStatistics> storeStatisticsList = new ArrayList<>();

        for (Order order : pastOrders) {
            for (ShoppingCartEntry entry : order.getOrderedItems()) {
                Seller seller = entry.getSeller();
                StoreStatistics storeStatistics = getStoreStatistics(storeStatisticsList, seller);
                storeStatistics.incrementCustomerPurchases();
            }
        }

        storeStatisticsList.sort(Comparator.comparingInt(StoreStatistics::getCustomerPurchases).reversed());
        return storeStatisticsList;
    }

    private StoreStatistics getStoreStatistics(List<StoreStatistics> storeStatisticsList, Seller seller) {
        for (StoreStatistics storeStatistics : storeStatisticsList) {
            if (storeStatistics.getSeller().equals(seller)) {
                return storeStatistics;
            }
        }

        StoreStatistics newStoreStatistics = new StoreStatistics(seller);
        storeStatisticsList.add(newStoreStatistics);
        return newStoreStatistics;
    }

    private void displayStores(List<StoreStatistics> storeStatisticsList) {
        for (StoreStatistics storeStatistics : storeStatisticsList) {
            Seller seller = storeStatistics.getSeller();
            System.out.println("Store: " + seller.getEmail());
            System.out.println("  Products Sold: " + storeStatistics.getProductsSold());
            System.out.println("  Customer Purchases: " + storeStatistics.getCustomerPurchases());
            System.out.println("  -------------");
        }
    }

    private static class StoreStatistics {
        private Seller seller;
        private int productsSold;
        private int customerPurchases;

        public StoreStatistics(Seller seller) {
            this.seller = seller;
            this.productsSold = 0;
            this.customerPurchases = 0;
        }

        public Seller getSeller() {
            return seller;
        }

        public int getProductsSold() {
            return productsSold;
        }

        public int getCustomerPurchases() {
            return customerPurchases;
        }

        public void incrementProductsSold(int quantity) {
            this.productsSold += quantity;
        }

        public void incrementCustomerPurchases() {
            this.customerPurchases++;
        }
    }

    private static class ShoppingCartEntry {
        private Seller seller;
        private Product product;

        public ShoppingCartEntry(Seller seller, Product product) {
            this.seller = seller;
            this.product = product;
        }

        public Seller getSeller() {
            return seller;
        }

        public Product getProduct() {
            return product;
        }
    }
}
*/

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

public class Customer extends User {
  
    private ArrayList<Book> cart;
    private ArrayList<Book> pastPurchases;
//    private List<Order> pastOrders;

    public Customer(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.cart = new ArrayList<>();
        this.pastPurchases = new ArrayList<>();
        //FORMAT OF FILE:
        /*
         * PAST PURCHASES - product details separated by commas, but quantity is amount purchased instead of stock
         * CART - separated by commas
         */

        //if existing customer:
        if (User.isExistingUser(username) && User.isExistingEmail(email)) {
            BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
            String line = bfr.readLine(); // past purchases line
            if (line != null && !line.isEmpty()) {
                this.pastPurchases = Util.readCSV(line);
            }
            line = bfr.readLine(); //cart line
            if (line != null && !line.isEmpty()) {
                this.cart = Util.readCSV(line);
            }
            bfr.close();
        }
    }

    public void addToPastPurchases(Book product, int quantity) {
        product.setQuantity(quantity);
        this.pastPurchases.add(product);
    }

    public void addToCart(Book product, int quantity) {
        product.setQuantity(quantity); //sets quantity of
        this.cart.add(product);
        //if already exists in cart, just add quantity onto it?
        //TODO
    }

    public ArrayList<Book> getCart() {
        return this.cart;
    }

    public void setCart(ArrayList<Book> newCart) {
        this.cart = newCart;
    }

    public boolean removeFromCart(String productName, int quantity) {
        //find product in cart list
        for (Book product : cart) {
            if (productName.equals(product.getName())) {
                if (quantity == product.getQuantity()) {
                    cart.remove(product);
                    return true;
                } else if (quantity < product.getQuantity()) {
                    //saves product
                    Book cartProduct = product;
                    cart.remove(product);
                    cartProduct.removeQuantity(quantity);
                    cart.add(cartProduct);
                    return true;
                } else if (quantity > product.getQuantity() || quantity <= 0) {
                    System.out.println("Please input a valid quantity.");
                    return false;
                }
            }
        }
        System.out.println("Book not found in shopping cart.");
        return false;
    }

    public ArrayList<Book> purchaseShoppingCart() {
        //TODO - purchase all of cart (aka clears it out) returns the cart.
        ArrayList<Book> placeholder = this.cart;
        this.cart = new ArrayList<>();
        return placeholder;
    }

    public void viewPastPurchases() {
        if (pastPurchases.isEmpty()) {
            System.out.println("You have made no purchases before.");
            return;
        }
        System.out.println("Past Purchases: \n" + "-------");
        for (int i = 0; i < this.pastPurchases.size(); i++) {
            System.out.println("Product: " + this.pastPurchases.get(i).getName());
            System.out.println("Store: " + this.pastPurchases.get(i).getStore());
            System.out.println("Description: " + this.pastPurchases.get(i).getDescription());
            System.out.println("Quantity: " + this.pastPurchases.get(i).getQuantity());
            System.out.println("Price: $" + this.pastPurchases.get(i).getPrice());
            System.out.println("-------\n");
        }
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your shopping cart is empty.");
        }
        else {
            System.out.println("Your Shopping Cart: ");
            for (Book product : cart) {
                System.out.println("Product: " + product.getName());
                System.out.println("Store: " + product.getStore());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("Price: $" + product.getPrice() + "\n");
            }
        }
    }
    public void exportToFile() throws IOException {
        //TODO
        if (!User.isExistingUser(this.getUsername())) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv", true));
            bw.write(this.getUsername() + "," + this.getEmail() + "," + this.getPassword() + "," + "CUSTOMER");
            bw.write("\n");
            bw.close();
        }

        File f = new File(this.getUsername() + ".csv");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); //overwrites existing file
        //write in cart data
        String details = "";
        for (Book pastItem : pastPurchases) {
            details += pastItem.toCSVFormat();
        }
        bw.write(details);
        bw.close();
        //write in past purchases
        bw = new BufferedWriter((new FileWriter(f, true))); //appends into file
        String cartItems = "";
        for (Book cartProd : cart) {
            cartItems += cartProd.toCSVFormat();
        }
        bw.write(cartItems);
        bw.close();
    }

    public boolean exportPastPurchases(String filename) throws IOException {
        //exports to a file
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("This file already exists.");
            return false;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < pastPurchases.size(); i++) {
            String name = pastPurchases.get(i).getName();
            String storeName = pastPurchases.get(i).getStore();
            String description = pastPurchases.get(i).getDescription();
            int quantityBought = pastPurchases.get(i).getQuantity();
            double price = pastPurchases.get(i).getPrice();
            bw.write(name + "," + storeName + "," + description + "," + quantityBought + ","+ price);
        }
        bw.close();
        return true;
    }

    public void storesByProductsSold() {
        //TODO
        System.out.println("Stores sorted by number of products sold: ");

    }
}
    
//    public void viewDashboard() {
//        List<StoreStatistics> storesByProductsSold = getStoresSortedByProductsSold();
//        List<StoreStatistics> storesByCustomerPurchases = getStoresSortedByCustomerPurchases();
//
//        System.out.println("Dashboard:");
//        System.out.println("1. Stores by Products Sold:");
//        displayStores(storesByProductsSold);
//        System.out.println("\n2. Stores by Customer Purchases:");
//        displayStores(storesByCustomerPurchases);
//    }


//    private List<StoreStatistics> getStoresSortedByProductsSold() {
//        List<StoreStatistics> storeStatisticsList = new ArrayList<>();
//
//        for (Order order : pastOrders) {
//            for (ShoppingCartEntry entry : order.getOrderedItems()) {
//                Seller seller = entry.getSeller();
//                StoreStatistics storeStatistics = getStoreStatistics(storeStatisticsList, seller);
//                storeStatistics.incrementProductsSold(entry.getProduct().getQuantity());
//            }
//        }
//
//        storeStatisticsList.sort(Comparator.comparingInt(StoreStatistics::getProductsSold).reversed());
//        return storeStatisticsList;
//    }
//}


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
    
    public void removeFromShoppingCart(Seller seller, Book product) {
        ShoppingCartEntry entryToRemove = null;
        for (ShoppingCartEntry entry : shoppingCart) {
            if (entry.getSeller().equals(seller) && entry.getProduct().equals(product)) {
                entryToRemove = entry;
                break;
            }
        }
        if (entryToRemove != null) {
            shoppingCart.remove(entryToRemove);
            System.out.println("Book removed from the shopping cart!");
        } else {
            System.out.println("Book not found in the shopping cart.");
        }
    }

    public void purchase() {
        Order newOrder = new Order(this, shoppingCart);
        pastOrders.add(newOrder);
        shoppingCart.clear();
        System.out.println("Purchase completed!");
    }

    public void addToShoppingCart(Seller seller, Book product) {
        shoppingCart.add(new ShoppingCartEntry(seller, product));
        System.out.println("Book added to the shopping cart!");
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

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

public class Customer extends User {

    private ArrayList<Product> cart;
    private ArrayList<Product> pastPurchases;
//    private List<Order> pastOrders;

    public Customer(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.cart = new ArrayList<>();
        this.pastPurchases = new ArrayList<>();

        //FORMAT OF FILE:
        /*
         * CART - separate items separated by semicolons, separate item details separated by commas
         * PAST PURCHASES - product details separated by commas, but quantity is amount purchased instead of stock
         */

        //if existing customer:
        if (User.isExistingUser(username) && User.isExistingEmail(email)) {
            BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
            bfr.readLine();
            String line = bfr.readLine(); // cart line
            if (line != null && !line.equals("")) {
                String[] cartArray = line.split(";"); //splits into separate items
                for (int i = 0; i < cartArray.length; i++) {
                    String[] productArray = cartArray[i].split(",");
                    this.cart.add(new Product(productArray[0], productArray[1],
                            productArray[2], Integer.parseInt(productArray[3]),
                            Double.parseDouble(productArray[4])));
                }
            }

            line = bfr.readLine(); //third line - pastPurchases
            if (line != null && !line.equals("")) {
                String[] cartArray = line.split(";"); //splits into separate items
                for (int i = 0; i < cartArray.length; i++) {
                    String[] productArray = cartArray[i].split(",");
                    this.pastPurchases.add(new Product(productArray[0], productArray[1], Integer.parseInt(productArray[2]),
                            Double.parseDouble(productArray[3])));
                }
            }
        }

    }

    public boolean purchase(Product product, int quantity) {
        //TODO
        return true;
    }

    public void addToCart(Product product, int quantity) {
        Product cartProduct = product;
        cartProduct.setQuantity(quantity);
        cart.add(cartProduct);
    }

    public boolean removeFromCart(Product product, int quantity) {
        for (int i = 0; i < cart.size(); i++) {
            if (product.equals(cart.get(i))) {//fix
                //if quantity is equal to the amount in cart, remove item entirely
                if (quantity == product.getQuantity()) {
                    cart.remove(i);
                    return true;
                } else if (quantity < cart.get(i).getQuantity()) {
                    //saves product
                    Product cartProduct = cart.get(i);
                    cart.remove(i);
                    cartProduct.removeQuantity(quantity);
                    cart.add(cartProduct);
                    return true;
                } else if (quantity > cart.get(i).getQuantity() || quantity <= 0) {
                    throw new InvalidQuantityError();
                }
            }
        }
        System.out.println("Product not found in shopping cart.");
        return false;
    }

    public void purchaseShoppingCart() {
        //TODO
    }

    public void getPastPurchases() {
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
            return;
        }
        System.out.println("Your Shopping Cart: ");
        for (int i = 0; i < cart.size(); i++) {
            System.out.println("Product: " + this.cart.get(i).getName());
            System.out.println("Store: " + this.cart.get(i).getStore());
            System.out.println("Description: " + this.cart.get(i).getDescription());
            System.out.println("Quantity: " + this.cart.get(i).getQuantity());
            System.out.println("Price: $" + this.cart.get(i).getPrice());
            System.out.println("-------\n");
        }
    }
    public void exportToFile() throws IOException {
        //TODO
        //export cart
        //export pastpurchases
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

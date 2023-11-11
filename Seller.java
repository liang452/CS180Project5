import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/*
 */

public class Seller extends User {
    private HashMap<Store, Product> storeProductHashMap;
    private String username;

    //	private List<Product> products;
	// private List<Sale> sales;
	// private List<CustomerShoppingCartEntry> customerShoppingCarts;

    public Seller(String username, String email, String password) throws IOException {
        super(username, email, password);
        File f = new File(username);
        if (!f.exists()) {
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.write("SELLER");
        }
    }
    //TODO: decide what to return there
    public Store getStore() {
        return new Store("");
    }
    public void setStore() {

    }


    public void viewCustomerShoppingCarts() {
        System.out.println("Customer Shopping Carts:");
        for (CustomerShoppingCartEntry entry : customerShoppingCarts) {
            Customer customer = entry.getCustomer();
            Product product = entry.getProduct();
            System.out.println("Customer: " + customer.getEmail());
            System.out.println("  Product: " + product.getName());
            System.out.println("  Quantity: " + product.getQuantity());
            System.out.println("  Price: $" + product.getPrice());
            System.out.println("  -------------");
        }
    }


    public void viewDashboard() {
        List<CustomerStatistics> customerStats = getCustomerStats();
        List<ProductStatistics> productStats = getProductStats();

        System.out.println("Dashboard:");
        System.out.println("1. Customers by Items Purchased:");
        displayCustomerStats(customerStats);
        System.out.println("\n2. Products by Number of Sales:");
        displayProductStats(productStats);
    }

    public void makeSale(Customer customer, Product product, int quantity) {
        if (products.contains(product) && product.getQuantity() >= quantity) {
            product.updateQuantity(quantity); // Update the quantity of the product
            Sale sale = new Sale(customer, product, quantity);
            sales.add(sale); // Add the sale to the list
            updateCustomerShoppingCarts(sale);
            System.out.println("Sale successful!");
        } else {
            System.out.println("Product not found or insufficient quantity available.");
        }
    }
}


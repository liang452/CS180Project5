import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;

/*
 */

public class Seller extends User {

    private List<Product> products;
    private List<Sale> sales;
    private List<CustomerShoppingCartEntry> customerShoppingCarts;

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

    public void exportProductsToCSV(String filename) {
    	try (FileWriter writer = new FileWriter(filename)) {
        	for (Product product : products) {
            	writer.write(product.toCSV() + "\n");
        	}
        	System.out.println("Products exported to " + filename);
    	} catch (IOException e) {
        	System.err.println("Error exporting products: " + e.getMessage());
            }
        }

    	public void importProductsFromCSV(String filename) {
    	try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        	String line;
        	while ((line = reader.readLine()) != null) {
            	String[] parts = line.split(",");
            	if (parts.length == 4) {
                	String name = parts[0].trim();
                	double price = Double.parseDouble(parts[1].trim());
                	int quantity = Integer.parseInt(parts[2].trim());
                	String description = parts[3].trim();
                	Product product = new Product(name, price, quantity, description);
                	products.add(product);
            	}
        	}
        	System.out.println("Products imported from " + filename);
    	} catch (IOException | NumberFormatException e) {
        	System.err.println("Error importing products: " + e.getMessage());
            }
	}

	private List<CustomerStatistics> getCustomerStats() {
    	List<CustomerStatistics> customerStats = new ArrayList<>();

    	for (Sale sale : sales) {
        	Customer customer = sale.getCustomer();
        	CustomerStatistics stats = getCustomerStatistics(customerStats, customer);
        	stats.incrementItemsPurchased(sale.getQuantity());
    	}

    	customerStats.sort(Comparator.comparingInt(CustomerStatistics::getItemsPurchased).reversed());
    	return customerStats;
	}



	
	
}


/*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Seller extends User {
	private List<Product> products;
	private List<Sale> sales;
	private List<CustomerShoppingCartEntry> customerShoppingCarts;

	public Seller(String email, String password) {
    	super(email, password);
    	this.products = new ArrayList<>();
    	this.sales = new ArrayList<>();
    	this.customerShoppingCarts = new ArrayList<>();
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

	public void exportProductsToCSV(String filename) {
    	try (FileWriter writer = new FileWriter(filename)) {
        	for (Product product : products) {
            	writer.write(product.toCSV() + "\n");
        	}
        	System.out.println("Products exported to " + filename);
    	} catch (IOException e) {
        	System.err.println("Error exporting products: " + e.getMessage());
            }
	}

	public void importProductsFromCSV(String filename) {
    	try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        	String line;
        	while ((line = reader.readLine()) != null) {
            	String[] parts = line.split(",");
            	if (parts.length == 4) {
                	String name = parts[0].trim();
                	double price = Double.parseDouble(parts[1].trim());
                	int quantity = Integer.parseInt(parts[2].trim());
                	String description = parts[3].trim();
                	Product product = new Product(name, price, quantity, description);
                	products.add(product);
            	}
        	}
        	System.out.println("Products imported from " + filename);
    	} catch (IOException | NumberFormatException e) {
        	System.err.println("Error importing products: " + e.getMessage());
    	}
	}

	private List<CustomerStatistics> getCustomerStats() {
    	List<CustomerStatistics> customerStats = new ArrayList<>();

    	for (Sale sale : sales) {
        	Customer customer = sale.getCustomer();
        	CustomerStatistics stats = getCustomerStatistics(customerStats, customer);
        	stats.incrementItemsPurchased(sale.getQuantity());
    	}

    	customerStats.sort(Comparator.comparingInt(CustomerStatistics::getItemsPurchased).reversed());
    	return customerStats;
	}

	private List<ProductStatistics> getProductStats() {
    	List<ProductStatistics> productStats = new ArrayList<>();

    	for (Sale sale : sales) {
        	Product product = sale.getProduct();
        	ProductStatistics stats = getProductStatistics(productStats, product);
        	stats.incrementSales();
    	}

    	productStats.sort(Comparator.comparingInt(ProductStatistics::getSales).reversed());
    	return productStats;
	}

	private CustomerStatistics getCustomerStatistics(List<CustomerStatistics> customerStats, Customer customer) {
    	for (CustomerStatistics stats : customerStats) {
        	if (stats.getCustomer().equals(customer)) {
            	return stats;
        	}
    	}

    	CustomerStatistics newStats = new CustomerStatistics(customer);
    	customerStats.add(newStats);
    	return newStats;
	}

	private ProductStatistics getProductStatistics(List<ProductStatistics> productStats, Product product) {
    	for (ProductStatistics stats : productStats) {
        	if (stats.getProduct().equals(product)) {
            	return stats;
        	}
    	}

    	ProductStatistics newStats = new ProductStatistics(product);
    	productStats.add(newStats);
    	return newStats;
	}

	private void displayCustomerStats(List<CustomerStatistics> customerStats) {
    	for (CustomerStatistics stats : customerStats) {
        	System.out.println("Customer: " + stats.getCustomer().getEmail());
        	System.out.println("  Items Purchased: " + stats.getItemsPurchased());
        	System.out.println("  -------------");
    	}
	}

	private void displayProductStats(List<ProductStatistics> productStats) {
    	for (ProductStatistics stats : productStats) {
        	System.out.println("Product: " + stats.getProduct().getName());
        	System.out.println("  Sales: " + stats.getSales());
        	System.out.println("  -------------");
    	}
	}

	// Method to make a sale
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

	private static class CustomerStatistics {
    	private Customer customer;
    	private int itemsPurchased;

    	public CustomerStatistics(Customer customer) {
        	this.customer = customer;
        	this.itemsPurchased = 0;
    	}

    	public Customer getCustomer() {
        	return customer;
    	}

    	public int getItemsPurchased() {
        	return itemsPurchased;
    	}

    	public void incrementItemsPurchased(int quantity) {
        	this.itemsPurchased += quantity;
    	}
	}

	private static class ProductStatistics {
    	private Product product;
    	private int sales;

    	public ProductStatistics(Product product) {
        	this.product = product;
        	this.sales = 0;
    	}

    	public Product getProduct() {
        	return product;
    	}

    	public int getSales() {
        	return sales;
    	}

    	public void incrementSales() {
        	this.sales++;
    	}
	}

	private static class Sale {
    	private static int nextSaleId = 1;

    	private int saleId;
    	private Customer customer;
    	private Product product;
    	private int quantity;
    	private double revenue;

    	public Sale(Customer customer, Product product, int quantity) {
        	this.saleId = nextSaleId++;
        	this.customer = customer;
        	this.product = product;
        	this.quantity = quantity;
        	this.revenue = product.getPrice() * quantity;
    	}

    	public int getSaleId() {
        	return saleId;
    	}

    	public Customer getCustomer() {
        	return customer;
    	}

    	public Product getProduct() {
        	return product;
    	}

    	public int getQuantity() {
        	return quantity;
    	}

    	public double getRevenue() {
        	return revenue;
    	}
	}
}
*/



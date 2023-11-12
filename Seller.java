import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

/*
 */

public class Seller extends User {
    private ArrayList<Store> stores;
    private ArrayList<Product> sales;
    //private ArrayList<Product> customerShoppingCarts;
    //private List<CustomerShoppingCartEntry> customerShoppingCarts;

    public Seller(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.stores = new ArrayList<Store>();

        File f = new File(username + ".csv");
        if (f.exists()) {
            BufferedReader bfr = new BufferedReader(new FileReader(username+ ".csv"));
            String line = bfr.readLine();
            //TODO: read in stores
            while (line != null && !line.equals("")) {
                String[] productDetails = line.split(",");
                Store store = new Store(productDetails[1]);
                for (int i = 0; i < productDetails.length; i += 5) {
                    String name = productDetails[i];
                    String storeName = productDetails[i + 1];
                    String description = productDetails[i + 2];
                    int quantity = Integer.parseInt(productDetails[i + 3]);
                    double price = Double.parseDouble(productDetails[i + 4]);
                    //if store does not match and there is no already existing store with the same name, create new.
                    if (!storeName.equals(store.getName())) {
                        if (!Util.isExistingStore(storeName, this.stores)) {
                            store = new Store(storeName);
                        } else if (Util.isExistingStore(storeName, this.stores)) {
                            //if already existing store
                            //loop through stores list
                            for (Store currStore : stores) {
                                if (currStore.getName().equals(storeName)) {
                                    Store placeholder = currStore;
                                    placeholder.addProduct((new Product(name, storeName, description, quantity,
                                            price)));
                                    stores.remove(currStore);
                                    stores.add(placeholder);
                                    break;
                                }
                            }
                        }
                    } else {
                        store.addProduct(new Product(name, storeName, description, quantity, price));
                    }
                    this.stores.add(store);
                }
                line = bfr.readLine();
            }
            bfr.close();
        }
    }
    public Seller getSeller() {
        return this;
    }
    public void addStore(Store store) {
        this.stores.add(store);
    }
    public ArrayList<Store> getStore() {
        return this.stores;
    }

    public boolean exportToFile() throws IOException {
        //logins.csv:
        if (!User.isExistingUser(this.getUsername()) && !User.isExistingEmail(this.getEmail())) {
            //if not an existing username or email
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv", true));
                bw.write(this.getUsername() + "," + this.getEmail() + "," + this.getPassword() + "," + "SELLER");
                bw.write("\n");
                bw.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            //file with username should have been created when account was created
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getUsername() + ".csv"));
            //overwrites file every time
            String storeString = "";
            ArrayList<String> allStoreDetails = new ArrayList<>();
            //lists products
            //TODO: a way to read this in. add way to read multiple stores into importFile method.
            for (Store store : this.stores) { //loops through entire list of stores
                ArrayList<Product> looper = store.getProducts();
                for (Product product : looper) {
                    storeString += product.getName();
                    storeString += ",";
                    storeString += store.getName();
                    storeString += ",";
                    storeString += product.getDescription();
                    storeString += ",";
                    storeString += product.getQuantity();
                    storeString += ",";
                    storeString += product.getPrice();
                    System.out.println("Store: " + storeString);
                    allStoreDetails.add(storeString);
                    storeString = "";
                }
            }
            for (String string : allStoreDetails) {
                bw.write(string + "\n");
            }
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void displayProducts() {
        for (Store store : this.stores) {
            ArrayList<Product> products = store.getProducts();
            for (Product product : products) {
                System.out.println(product.getName());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Associated Store: " + product.getStore());
                System.out.println("Amount in Stock: " + product.getQuantity());
                System.out.println("$" + product.getPrice() + "\n");
            }
        }
    }

    public void viewCustomerShoppingCarts() throws IOException {
        //TODO:
        System.out.println("Customer Shopping Carts:");

    }
    //assume one product per line - UNNEEDED
//    public void importProducts(String filename) {
//        try (BufferedReader bfr = new BufferedReader(new FileReader(filename))) {
//            Store store = null;
//            String line = bfr.readLine();
//            while (line != null && !line.equals("")) {
//                String[] productDetails = line.split(",");
//                String name = productDetails[0]; //name
//                String storeName = productDetails[1];
//                String description = productDetails[2];
//                int quantity = Integer.parseInt(productDetails[3]);
//                double price = Double.parseDouble(productDetails[4]);
//                Product product = new Product(name, storeName, description, quantity, price);
//                store.addProduct(product);
//                //read into new line
//                line = bfr.readLine();
//            }
//            this.stores.add(store);
//            System.out.println("Products successfully imported from " + filename);
//        } catch (IOException | NumberFormatException e) {
//            System.err.println("Error importing products: " + e.getMessage());
//        }
//    }

    public void viewSales() throws IOException {
        //TODO
        }
    }


//    public void viewCustomerShoppingCarts() {
//        System.out.println("Customer Shopping Carts:");
//        for (CustomerShoppingCartEntry entry : customerShoppingCarts) {
//            Customer customer = entry.getCustomer();
//            Product product = entry.getProduct();
//            System.out.println("Customer: " + customer.getEmail());
//            System.out.println("  Product: " + product.getName());
//            System.out.println("  Quantity: " + product.getQuantity());
//            System.out.println("  Price: $" + product.getPrice());
//            System.out.println("  -------------");
//        }
//    }

//    public void viewDashboard() {
//        List<CustomerStatistics> customerStats = getCustomerStats();
//        List<ProductStatistics> productStats = getProductStats();
//
//        System.out.println("Dashboard:");
//        System.out.println("1. Customers by Items Purchased:");
//        displayCustomerStats(customerStats);
//        System.out.println("\n2. Products by Number of Sales:");
//        displayProductStats(productStats);
//    }
//
//
//    public void makeSale(Customer customer, Product product, int quantity) {
//        if (products.contains(product) && product.getQuantity() >= quantity) {
//            product.updateQuantity(quantity); // Update the quantity of the product
//            Sale sale = new Sale(customer, product, quantity);
//            sales.add(sale); // Add the sale to the list
//            updateCustomerShoppingCarts(sale);
//            System.out.println("Sale successful!");
//        } else {
//            System.out.println("Product not found or insufficient quantity available.");
//        }
//    }
//}


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



import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

/*
 */

public class Seller extends User {
    private ArrayList<Store> stores;
    private ArrayList<Product> sales;
    private ArrayList<CustomerShoppingCartEntry> customerShoppingCarts;

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
                bw.write(string);
            }
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void displayProductsByStore() {
        for (Store store : stores) {
            ArrayList<Product> products = store.getProducts();
            System.out.println(store.getName().toUpperCase());
            System.out.println("PRODUCTS:");
            for (Product product : products) {
                System.out.println("\n" + product.getName());
                System.out.println(product.getDescription());
                System.out.println(product.getQuantity());
                System.out.println(product.getPrice());
            }
        }
    }

    public void viewCustomerShoppingCarts() throws IOException{
        System.out.println("Customer Shopping Carts:");
        for (CustomerShoppingCartEntry entry : customerShoppingCarts) {
            Customer customer = entry.getCustomer();
            Product product = entry.getProduct();
            System.out.println("Customer: " + customer.getEmail());
            System.out.println("  Product: " + product.getName());
            System.out.println("  Quantity: " + entry.getQuantity());
            System.out.println("  Price: $" + product.getPrice());
            System.out.println("  -------------");
        }
    }

    public void viewSales() throws IOException {
        System.out.println("Sales:");
        for (Product product : sales) {
            int quantitySold = 0;
            double totalRevenue = 0;

            for (CustomerShoppingCartEntry entry : customerShoppingCarts) {
                if (entry.getProduct().equals(product)) {
                    quantitySold += entry.getQuantity();
                    totalRevenue += entry.getQuantity() * product.getPrice();
                }
            }

            System.out.println("Product: " + product.getName());
            System.out.println("  Quantity Sold: " + quantitySold);
            System.out.println("  Total Revenue: $" + totalRevenue);
            System.out.println("  -------------");
        }


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

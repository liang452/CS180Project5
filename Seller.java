import java.io.*;
import java.util.ArrayList;

/*
 */

public class Seller extends User {
    private ArrayList<Store> stores;
    private ArrayList<Book> products;
    private ArrayList<String> storeNames;

    public Seller(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.stores = new ArrayList<Store>();

        File f = new File(username + ".csv");
        if (f.exists()) {
            this.products = Util.readCSV(username + ".csv");
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
    public void addToStore(String storeName, Book book) {
        this.products.add(book);
        ArrayList<Store> placeholder = new ArrayList<>();
        Store newStore = null;
        for (Store store : stores) {
            if (!store.getName().equals(storeName)) {
                placeholder.add(store);
            } else {
                newStore = store;
                newStore.addProduct(book);
            }
        }
        if (newStore != null) {
            placeholder.add(newStore);
        }
        this.stores = placeholder;
    }
    public ArrayList<Book> getProducts() {
        return this.products;
    }
    public boolean exportProducts(String fileName) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (Book book : products) {
                bw.write(book.toCSVFormat());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
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
                ArrayList<Book> looper = store.getProducts();
                for (Book product : looper) {
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
        for (Book product : this.products) {
            System.out.println(product.getName());
            System.out.println(product.getAuthor());
            System.out.println(product.getGenre().toString());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Associated Store: " + product.getStore());
            System.out.println("Amount in Stock: " + product.getQuantity());
            System.out.println("$" + product.getPrice() + "\n");
        }
    }

}


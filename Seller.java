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
            this.products = Util.readCSVToBook(username + ".csv"); //products is instantiated
            ArrayList<Book> iterator = this.products;
            for (Book book : iterator) {
                this.addToStore(book.getStore(), book);
            }
        } else {
            this.products = new ArrayList<>();
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
        ArrayList<Store> placeholder = new ArrayList<>(); //placeholder array
        Store addedStore = null; //new store to be added
        boolean existing = false;
        for (Store store : this.stores) { //for every store in this seller already
            if (!store.getName().equals(storeName)) { //if stores do not equal each other
                placeholder.add(store); //add into placeholder
                existing = true;
            } else {
                addedStore = store; //if stores do equal each other
                addedStore.addProduct(book);
                existing = true;
            }
        }
        if (!existing) { //if no such store name was found
            addedStore = new Store(storeName);
            addedStore.addProduct(book);
        }
        if (addedStore != null) {
            placeholder.add(addedStore);
        }
        this.stores = placeholder;
    }
    public ArrayList<Book> getProducts() {
        return this.products;
    }
    public void setProducts(ArrayList<Book> newBooks) {
        this.products = newBooks;
    }
    public void addProducts(ArrayList<Book> newBooks) {
        this.products.addAll(newBooks);
        for (Book book : newBooks) {
            this.addToStore(book.getStore(), book);
        }
    }
    public void addProduct(Book book) {
        this.products.add(book);
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
            for (Book book : this.products) { //loops through entire list of products
                bw.write(book.toCSVFormat());
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


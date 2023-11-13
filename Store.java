import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
public class Store {
    private String name;
    private ArrayList<Book> books;
    private String seller;

    public Store(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public Store(String name, ArrayList<Book> books) {
        //declares without file; manually adds books later
        this.name = name;
        this.books = books;
    }

    public Store(String name, String filename) throws IOException {
        //read name from file
        this.importProducts(filename);

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Book> getProducts() {
        return books;
    }

    public boolean importProducts(String fileName) throws IOException {
        //reads file
        if (fileName.contains(".csv")) {
            this.books = Util.readCSV(fileName);
            return true;
        } else {
            System.out.println("Please input a valid file.");
            return false;
        }
    }


    public void removeProduct(String productName) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getName().equalsIgnoreCase(productName)) {
                books.remove(i);
                System.out.println("Product '" + productName + "' removed successfully.");
                return;
            }
        }
        System.out.println("Product '" + productName + "' not found in the store.");
    }

    public void addProduct(Book book) {
        books.add(book);
        System.out.println("Product '" + book.getName() + "' added to the store.");
    }

    public void displayStore() {
        System.out.println("Store Name: " + this.name);
        if (books.isEmpty()) {
            System.out.println("You have no books.");
        } else {
            System.out.println("Products: ");
            for (int i = 0; i < books.size(); i++) {
                //name, price
                System.out.println(books.get(i).getName() + " - $" + books.get(i).getPrice());
            }
        }
    }
}
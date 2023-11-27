import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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
        this.name = name;
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
            this.books = Util.readCSVToBook(fileName);
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

    public void setProducts(ArrayList<Book> books) {
        this.books = books;
    }

    public void displayStore() {
        if (this.books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no books in this store.");
        } else {
            JPanel storePanel = new JPanel(); //panel for store
            storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));

            JLabel storeName = new JLabel(this.name);
            storePanel.add(storeName, BorderLayout.NORTH); //adds storeName to very top

            JPanel productPanel = Util.bookPanel(this.getProducts()); //calls bookPanel
            storePanel.add(productPanel);
            storePanel.setVisible(true);
        }

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

    public void displayData() {
        System.out.println("Store Name: " + this.getName());
        ArrayList<Book> purchases = this.getProducts();
        double revenue = 0;
        for (Book book : purchases) {
            revenue += book.getQuantity() * book.getPrice();
        }
        System.out.println("Total Revenue: " + revenue);
        System.out.println("Products Sold: ");
        for (Book book : this.getProducts()) {
            System.out.println(book.getName() + " - " + book.getQuantity() + " - $" + book.getPrice());
        }
    }
}
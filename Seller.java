import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/*
 */

public class Seller extends User implements Serializable {
    private ArrayList<Book> products;
    private ArrayList<String> storeNames;

    public Seller(String username, String email, String password) throws IOException {
        super(username, email, password);
        storeNames = new ArrayList<>();
        File f = new File(username + ".csv");
        if (f.exists()) {
            this.products = Util.readCSVToBook(username + ".csv"); //products is instantiated
            for (Book book : products) {
                if (!storeNames.contains(book.getStore())) {
                    storeNames.add(book.getStore());
                }
            }
        } else {
            this.products = new ArrayList<>();
        }
    }
    public Seller getSeller() {
        return this;
    }
    public ArrayList<String> getStoreNames() {
        return this.storeNames;
    }

    public ArrayList<Book> getProducts() {
        return this.products;
    }
    public void setProducts(ArrayList<Book> newBooks) {
        this.products = newBooks;
    }
    public void addProducts(ArrayList<Book> newBooks) {
        this.products.addAll(newBooks);
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
                bw.newLine();
            }
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public JPanel displayProducts() {
        BookPanel productPanel = new BookPanel(this.getProducts());
        return productPanel.getBookPanel();
    }

    public void updateProduct(Book oldBook, Book newBook) {
        //update product locally
        if (products.contains(oldBook)) {
            int index = products.indexOf(oldBook);
            products.set(index, newBook);
        }
    }
    public synchronized static boolean updateProductFile(Book oldBook, Book newBook, String seller) throws IOException {
        ArrayList<Book> oldProductsList = Util.readCSVToBook(seller + ".csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(seller + ".csv"));
        System.out.println("First product: " + oldProductsList.get(0).getName() + " " + oldProductsList.get(0).getAuthor());
        int index = -1;
        for (Book book: oldProductsList) {
            System.out.println("List item: " + book.getName() + " VS. Item being searched for: " +  oldBook.getName());
            if (book.equals(oldBook)) {
                index = oldProductsList.indexOf(book);
                System.out.println("Index of item: " + index);
            }
        }
        if (index != -1) {
            oldProductsList.remove(index);
            oldProductsList.add(newBook);
            StringBuilder newBooksList = new StringBuilder();
            for (Book b : oldProductsList) {
                bw.write(b.toCSVFormat() + "\n");
                bw.flush();
            }
            return true;
        } else {
            String oldBooks = "";
            for (Book b : oldProductsList) {
                oldBooks += b.toCSVFormat() + ",";
            }
            System.out.println("Stays the same: " + oldBooks);
            bw.write(oldBooks + "\n");
            bw.flush();
            return false;
        }
    }

}


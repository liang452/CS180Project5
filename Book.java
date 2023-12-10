import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Book implements Serializable {
    private String name;
    private String author;
    private static Genre genre;
    private String description;
    private String store;
    private int quantity;
    private double price;

    enum Genre {
        FANTASY,
        SCIENCE_FICTION,
        HORROR,
        THRILLER,
        HISTORICAL,
        NONFICTION,
        ROMANCE,
        YOUNG_ADULT,
        INVALID_GENRE
    }
    public Book() {
        this.name = "";
        this.author = "";
    }
  
    public Book(String name, String author, String genre, String description, String store, int quantity,
                double price) {
        this.name = name;
        this.author = author;
        this.genre = Book.readGenre(genre);
        this.description = description;
        this.store = store;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStore() {
        return this.store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = readGenre(genre);
    }

    public static Genre readGenre(String genreString) {
        Genre readGenre = null;
        if (genreString.equalsIgnoreCase("FANTASY")) {
            readGenre = Genre.FANTASY;
        } else if (genreString.equalsIgnoreCase("SCIENCE FICTION") ||
        genreString.equalsIgnoreCase("SCIENCE_FICTION")) {
            readGenre = Genre.SCIENCE_FICTION;
        } else if (genreString.equalsIgnoreCase("HORROR")) {
            readGenre = Genre.HORROR;
        } else if (genreString.equalsIgnoreCase("THRILLER")) {
            readGenre = Genre.THRILLER;
        } else if (genreString.equalsIgnoreCase("HISTORICAL")) {
            readGenre = Genre.HISTORICAL;
        } else if (genreString.equalsIgnoreCase("NONFICTION")) {
            readGenre = Genre.NONFICTION;
        } else if (genreString.equalsIgnoreCase("ROMANCE")) {
            readGenre = Genre.ROMANCE;
        } else if (genreString.equalsIgnoreCase("YOUNG ADULT") ||
                genreString.equalsIgnoreCase("YOUNG_ADULT")) {
            readGenre = Genre.YOUNG_ADULT;
        } else {
            readGenre = Genre.INVALID_GENRE;
        }
        return readGenre;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity; //
        }
    }

    public boolean removeQuantity(int amount) {
        if (amount <= quantity) {
            this.quantity -= amount;
            return true;
        } else {
            return false;
        }
    }
    public boolean addQuantity(int amountToAdd) {
        if (amountToAdd <= 0) {
            return false;
        }
        this.quantity += amountToAdd;
        return true;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void displayProduct() {
        System.out.println(this.name + " - " + this.getStore() + " - $" + this.getPrice());
    }

    public void displayProductInfo() {
        System.out.println("Title: " + this.name);
        System.out.println("Author: " + this.author);
        System.out.println(("Genre: "+ this.getGenre()));
        System.out.println("Description: " + this.description);
        System.out.println("Store: " + this.store);
        System.out.println("Quantity: " + this.quantity);
        System.out.println("Price: $" + this.price);
    }
  
    //takes integer as input, removes that amount from the current quantity

    public static Book createBookFromUserInput(String store) {
        if (store.equals("")) {
            store = JOptionPane.showInputDialog("Enter store name:");
        }
        if (store == null) {
            return new Book();
        }
        String name = JOptionPane.showInputDialog("Enter book name:");
        if (name == null) {
            return new Book();
        }
        String author = JOptionPane.showInputDialog("Enter author:");
        if (author == null) {
            return new Book();
        }
        String genre = JOptionPane.showInputDialog("Enter the genre:");
        if (genre == null) {
            return new Book();
        }
        String description = JOptionPane.showInputDialog("Enter book description:");
        if (description == null) {
            return new Book();
        }
        String quantity;
        do {
            quantity = JOptionPane.showInputDialog("Enter quantity available:");
            if (quantity == null) {
                return new Book();
            } else if (!Util.isNumeric(quantity)) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } while(!Util.isNumeric(quantity));
         String price;
        do {
            price = JOptionPane.showInputDialog("Enter the price: ");
            if (!Util.isNumeric(price)) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } while(!Util.isNumeric(price));

        return new Book(name, author, genre, description, store, Integer.parseInt(quantity), Double.parseDouble(price));
    }
      
    public boolean equals(Book book) {
        if (this.getName().equals(book.getName()) && this.getAuthor().equals(book.getAuthor())
                && (this.getStore().equals(this.getStore()))) {
            return true;
        } else {
            return false;
        }
    }
      
    public String toCSVFormat() {
        String name = this.getName();
        String author = this.getAuthor();
        String genre = this.getGenre().name();
        String description = this.getDescription();
        String storeName = this.getStore();
        String quantity = Integer.toString(this.getQuantity());
        String price = Double.toString(this.getPrice());
        String csv = name + "," +  author + "," + genre + "," + description + "," +
                storeName + "," + quantity + "," + price;
        return csv;
    }

}


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {
    private String name;
    private String store;
    private String description;
    private int quantity;
    private double price;


    public Product(String name, String description, int quantity, double price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
  
    public Product(String name, String store, String description, int quantity, double price) {
        this.name = name;
        this.store = store;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStore() {
        return this.store;
    }

    public void setStore(String store) {
        this.store = store;
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
            this.quantity = newQuantity;
        }
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
        System.out.println("Product: " + this.name);
        System.out.println("Store: " + this.store);
        System.out.println("Description: " + this.description);
        System.out.println("Quantity: " + this.quantity);
        System.out.println("Price: $" + this.price);
    }
  
    //TODO: add other ways to change product quantity : added setquantity
    public boolean removeQuantity(int removeQuantity) {
        if (removeQuantity <= quantity) {
            this.quantity -= removeQuantity;
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

    public static Product createProductFromUserInput(String storeName) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter product name:");
        String name = scan.nextLine();

        System.out.println("Enter store name:");
        String store = scan.nextLine();

        System.out.println("Enter product description:");

        String description = scan.nextLine();

        System.out.println("Enter quantity available:");
        int quantity = scan.nextInt();
        scan.nextLine();

        System.out.println("Enter price:");
        double price = scan.nextDouble();

        return new Product(name, store, description, quantity, price);
    }
      
    public boolean equals(Product product) {
        if (this.getName().equals(product.getName())
                && this.getPrice() == product.getPrice()
                && this.getDescription().equals(product.getDescription())) {
            return true;
        } else {
            return false;
        }
    }
      
    public String toCSVFormat() {
        String name = this.getName();
        String storeName = this.getStore();
        String description = this.getDescription();
        String quantity = Integer.toString(this.getQuantity());
        String price = Double.toString(this.getPrice());
        String csv = name + "," + storeName + "," + description + "," + quantity + "," + price;
        return csv;
    }

}


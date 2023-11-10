import java.util.Scanner;

public class Product {
    private String name;
    private String store;
    private String description;
    private int quantity;
    private double price;

    public Product(String name, String store, String description, int quantity, double price) {
        this.name = name;
        this.store = store;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void displayProductInfo() {
        System.out.println("Product: " + name);
        System.out.println("Store: " + store);
        System.out.println("Description: " + description);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: $" + price);
    }
//TODO: add other ways to change product quantity
    public void updateQuantity(int purchasedQuantity) {
        if (purchasedQuantity <= quantity) {
            quantity -= purchasedQuantity;
            System.out.println("Purchase successful!");
        } else {
            System.out.println("Not enough quantity available.");
        }
    }

    public static Product createProductFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter product name:");
        String name = scanner.nextLine();

        System.out.println("Enter store name:");
        String store = scanner.nextLine();

        System.out.println("Enter product description:");
        String description = scanner.nextLine();

        System.out.println("Enter quantity available:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter price:");
        double price = scanner.nextDouble();

        return new Product(name, store, description, quantity, price);
    }
}


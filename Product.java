import java.util.Scanner;

public class Product {
    private String name;
    private String description;
    private int quantity;
    private double price;

    public Product(String name, String description, int quantity, double price) {
        this.name = name;
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

    public String getDescription() {
        return this.description;
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
        System.out.println("Description: " + description);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: $" + price);
    }
    //TODO: add other ways to change product quantity : added setquantity
    public boolean removeQuantity(int purchasedQuantity) {
        if (purchasedQuantity <= quantity) {
            quantity -= purchasedQuantity;
            return true;
        } else {
            throw new InvalidQuantityError();
        }
    }
    public int addQuantity(int amountToAdd) {
        if (amountToAdd <= 0) {
            throw new InvalidQuantityError();
        }
        this.quantity += amountToAdd;
        return this.quantity;
    }

    public boolean setQuantity(int newQuantity) {
    if (newQuantity >= 0) {
        quantity = newQuantity;
        return true;
    } else {
            throw new InvalidQuantityError();
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

        return new Product(name, description, quantity, price);
    }
}


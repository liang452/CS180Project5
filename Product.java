import java.util.Scanner;

public class Product {
    private String name;
    private String storeName;
    private String description;
    private int quantity;
    private double price;

    public Product(String name, String description, int quantity, double price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public Product(String name, String storeName, String description, int quantity, double price) {
        //with storename
        this.name = name;
        this.storeName = storeName;
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
    public String getStore() {
        return this.storeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
        } else {
            throw new InvalidQuantityError();
        }
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
    public int removeQuantity(int purchasedQuantity) {
        if (purchasedQuantity <= quantity) {
            this.quantity -= purchasedQuantity;
            return this.quantity;
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

    public static Product createProductFromUserInput(String storeName) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter product name:");
        String name = scanner.nextLine();

        System.out.println("Enter description:");
        String description = scanner.nextLine();

        System.out.println("Enter quantity available:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter price:");
        double price = scanner.nextDouble();

        return new Product(name, storeName, description, quantity, price);
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
    public String toCSVFormat(Product product) {
        String name = this.getName();
        String storeName = this.getStore();
        String description = this.getDescription();
        String quantity = Integer.toString(this.getQuantity());
        String price = Double.toString(this.getPrice());
        String csv = name + "," + storeName + "," + description + "," + quantity + "," + price;
        return csv;
    }
}


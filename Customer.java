import java.io.*;
import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<Book> cart;
    private ArrayList<Book> pastPurchases;

    public Customer(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.cart = new ArrayList<>();
        this.pastPurchases = new ArrayList<>();
        //FORMAT OF FILE:
        /*
         * PAST PURCHASES - product details separated by commas, but quantity is amount purchased instead of stock
         * CART - separated by commas
         */

        //if existing customer:
        if (User.isExistingUser(username) && User.isExistingEmail(email)) {
            BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
            String line = bfr.readLine(); // past purchases line
            if (line != null && !line.isEmpty()) {
                this.pastPurchases = Util.readCSV(line);
            }
            line = bfr.readLine(); //cart line
            if (line != null && !line.isEmpty()) {
                this.cart = Util.readCSV(line);
            }
            bfr.close();
        }
    }

    public void addToPastPurchases(Book product, int quantity) {
        product.setQuantity(quantity);
        this.pastPurchases.add(product);
    }

    public void addToCart(Book product, int quantity) {
        product.setQuantity(quantity); //sets quantity of
        this.cart.add(product);
        //if already exists in cart, just add quantity onto it?
        //TODO
    }

    public ArrayList<Book> getCart() {
        return this.cart;
    }

    public void setCart(ArrayList<Book> newCart) {
        this.cart = newCart;
    }

    public boolean removeFromCart(String productName, int quantity) {
        //find product in cart list
        for (Book product : cart) {
            if (productName.equals(product.getName())) {
                if (quantity == product.getQuantity()) {
                    cart.remove(product);
                    return true;
                } else if (quantity < product.getQuantity()) {
                    //saves product
                    Book cartProduct = product;
                    cart.remove(product);
                    cartProduct.removeQuantity(quantity);
                    cart.add(cartProduct);
                    return true;
                } else if (quantity > product.getQuantity() || quantity <= 0) {
                    System.out.println("Please input a valid quantity.");
                    return false;
                }
            }
        }
        System.out.println("Book not found in shopping cart.");
        return false;
    }

    /**
     * @return Purchases the entire shopping cart and returns what was purchased.
     */
    public ArrayList<Book> purchaseShoppingCart() {
        ArrayList<Book> placeholder = this.cart;
        this.cart = new ArrayList<>();
        return placeholder;
    }

    public void viewPastPurchases() {
        if (pastPurchases.isEmpty()) {
            System.out.println("You have made no purchases before.");
            return;
        }
        System.out.println("Past Purchases: \n" + "-------");
        for (int i = 0; i < this.pastPurchases.size(); i++) {
            System.out.println("Product: " + this.pastPurchases.get(i).getName());
            System.out.println("Store: " + this.pastPurchases.get(i).getStore());
            System.out.println("Description: " + this.pastPurchases.get(i).getDescription());
            System.out.println("Quantity: " + this.pastPurchases.get(i).getQuantity());
            System.out.println("Price: $" + this.pastPurchases.get(i).getPrice());
            System.out.println("-------\n");
        }
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your shopping cart is empty.");
        }
        else {
            System.out.println("Your Shopping Cart: ");
            for (Book product : cart) {
                System.out.println("Product: " + product.getName());
                System.out.println("Store: " + product.getStore());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("Price: $" + product.getPrice() + "\n");
            }
        }
    }
    public void exportToFile() throws IOException {
        //TODO
        if (!User.isExistingUser(this.getUsername())) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv", true));
            bw.write(this.getUsername() + "," + this.getEmail() + "," + this.getPassword() + "," + "CUSTOMER");
            bw.write("\n");
            bw.close();
        }

        File f = new File(this.getUsername() + ".csv");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); //overwrites existing file
        //write in cart data
        String details = "";
        for (Book pastItem : pastPurchases) {
            details += pastItem.toCSVFormat();
        }
        bw.write(details + "\n");
        bw.close();
        //write in past purchases
        bw = new BufferedWriter((new FileWriter(f, true))); //appends into file
        String cartItems = "";
        for (Book cartProd : cart) {
            cartItems += cartProd.toCSVFormat();
        }
        bw.write(cartItems);
        bw.close();
    }

    public boolean exportPastPurchases(String filename) throws IOException {
        //exports to a file
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("This file already exists.");
            return false;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < pastPurchases.size(); i++) {
            String name = pastPurchases.get(i).getName();
            String storeName = pastPurchases.get(i).getStore();
            String description = pastPurchases.get(i).getDescription();
            int quantityBought = pastPurchases.get(i).getQuantity();
            double price = pastPurchases.get(i).getPrice();
            bw.write(name + "," + storeName + "," + description + "," + quantityBought + ","+ price);
        }
        bw.close();
        return true;
    }

    public void storesByProductsSold() {
        //TODO
        System.out.println("Stores sorted by number of products sold: ");

    }
}

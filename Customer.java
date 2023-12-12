import java.io.*;
import java.util.ArrayList;

public class Customer extends User implements Serializable {
    private String username;
    private String email;
    private String password;
    private ArrayList<Book> cart;
    private ArrayList<Book> pastPurchases;

    public Customer(String username, String email, String password) throws IOException {
        super(username, email, password);
        this.cart = null;
        this.pastPurchases = null;
        this.username = username;
        this.email = email;
        this.password = password;
        //FORMAT OF FILE:
        /*
         * CART - separated by commas
         * PAST PURCHASES - product details separated by commas, but quantity is amount purchased instead of stock
         */
    }

    public String getUsername() {
        return this.username;
    }
    public void addToPastPurchases(Book product, int quantity) {
        product.setQuantity(quantity);
        if (pastPurchases == null) {
            pastPurchases = new ArrayList<>();
        }
        boolean productExists = false;
        for (Book cartProduct : pastPurchases) {
            if (cartProduct.equals(product)) {
                cartProduct.addQuantity(quantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            this.pastPurchases.add(product);
        }
    }

    public void setPastPurchases(ArrayList<Book> purchases) {
        this.pastPurchases = purchases;
    }

    public ArrayList<Book> getPastPurchases() {
        return this.pastPurchases;
    }
    public void addToCart(Book product, int quantity) {
        product.setQuantity(quantity);
        if (cart == null) {
            cart = new ArrayList<Book>();
        }
        boolean productExists = false;
        for (Book cartProduct : cart) {
            if (cartProduct.equals(product)) {
                cartProduct.addQuantity(quantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            this.cart.add(product);
        }
    } //TODO: test


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

    /**
     * Exports customer information to their information on file.
     * @throws IOException
     */
    public synchronized void exportToFile() throws IOException {
        if (!User.isExistingUser(this.username)) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv", true));
            bw.write(this.username + "," + this.email + "," + this.password + "," + "CUSTOMER");
            bw.write("\n");
            bw.close();
        }

        File f = new File(this.username + ".csv");
        if (!f.exists()) {
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("CART\n");
            bw.flush();
            bw.write("PAST\n");
            bw.flush();
        } else {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f)); //overwrites existing file
            //write in cart data
            String pastItems = "";
            String cartItems = "";
            if (this.cart == null || this.cart.isEmpty()) {
                System.out.println("EMPTY CART");
                bw.write("CART\n");
                bw.flush();
            } else {
                for (Book cartProd : cart) {
                    cartItems += cartProd.toCSVFormat() + ",";
                }
                System.out.println("Cart items: " + cartItems);
                bw.write(cartItems + "\n");
                bw.flush();
                bw.close();
            }

            //write in past purchases
            bw = new BufferedWriter((new FileWriter(f, true))); //appends into file
            if (this.pastPurchases == null || this.pastPurchases.isEmpty()) {
                System.out.println("NO PAST PURCHASES");
                bw.write("PAST\n");
                bw.flush();
            } else {
                for (Book pastItem : pastPurchases) {
                    pastItems += pastItem.toCSVFormat() + ",";
                }
                System.out.println("Past items: " + pastItems);
                bw.write(pastItems + "\n");
                bw.flush();

            }
        }
        BufferedReader bfr = new BufferedReader(new FileReader(f));
        System.out.println(this.getUsername() + ".csv");
        System.out.println("CART: " + bfr.readLine());
        System.out.println("PAST: " + bfr.readLine());
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
}

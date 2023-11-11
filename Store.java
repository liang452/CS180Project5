import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class Store {
    private String name;
    private ArrayList<Product> products;
    private String sellerName;

    public Store(String name) {
        //declares without file; manually adds products later
        this.name = name;
        this.products = new ArrayList<Product>();
    }
    public Store(String name, String filename) throws IOException {
        this.name = name;
        this.readProductsFromFile(filename);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(String filename) {
        if (filename != null) { //and also is an existing path
            //read in
        }
    }
    public void readProductsFromFile(String fileName) throws IOException {
        //reads file
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] productDetails = line.split(",", 0);
                String name = productDetails[0];
                String desc = productDetails[1];
                int quantity = Integer.parseInt(productDetails[2]);
                double price = Double.parseDouble(productDetails[3]);
                this.products.add(new Product(name, desc, quantity, price));
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("Please input a valid file.");
            throw e;
        }
    }

    public void removeProduct(String productName) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(productName)) {
                products.remove(i);
                System.out.println("Product '" + productName + "' removed successfully.");
                return;
            }
        }
        System.out.println("Product '" + productName + "' not found in the store.");
    }
    
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product '" + product.getName() + "' added to the store.");
    }
    
    public void displayStore() {
        System.out.println("Store Name:" + this.name);
        System.out.println("Seller: " + "");
        System.out.println("Products: ");
        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.get(i).getName());
        }
    }

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 */

public class Seller extends User {
    private Bakery bakery;
    //keep track of what user this is
    private int userID;
    public Seller(String username, String password) throws IOException {
        super(username, password);
        this.userID = super.getUserID();
    }
    public Bakery getBakery() {
        return bakery;
    }
    public void setStore(String storeName, int userID, String fileName) throws IOException {
        //reads file
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] products = line.split(",", 0);
                String name = products[0];
                line = bfr.readLine();
            }

        } catch (IOException e) {
            System.out.println("Please input a valid file.");
            throw e;
        }

        this.bakery = new Bakery(storeName, fileName);
        this.userID = userID;

    }
    public void addProduct() {
        //checks first if they would like to input it manually
        System.out.println("Would you like to input manually? (Y/N)");
        Scanner scan = new Scanner(System.in);
        String ans = scan.nextLine();
        if (ans.equalsIgnoreCase("Y")) {
            System.out.println("Please input the product name.");
            String prodName = scan.nextLine();
            System.out.println("Please input the product description.");
            String prodDesc =  scan.nextLine();
            System.out.println("Please input the quantity available for purchase.");
            int prodQuant = scan.nextInt();
            scan.nextLine();
            System.out.println("Please input the product price.");
            double prodPrice = scan.nextDouble();
            scan.nextLine();

        } else if (ans.equalsIgnoreCase("N")) {
            //input via filename
            System.out.println("Input a filename.");
        }
        String newProduct = scan.nextLine();
        //export to store products file.
    }
    public void editProduct() {

    }
    public void deleteProduct() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Which product would you like to delete?");
        String deleted = scan.nextLine();
        String match = "";
        ArrayList<BakedGood> products = this.getBakery().getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (deleted.equals(products.get(i).getName())) {
                match = products.get(i).getName();
                products.remove(i);
            }
        }
        if (match == "") {
            //maybe make an error for this?
            System.out.println("Please select a valid product.");
        }
    }

    public String displaySales() {
        //returns a list of sales by store - includes customer information and revenues from sale
        //need a revenue calculating method?
        return "";
    }
    //statistics display
    //statistics sort
    //shopping cart. how to do this???
}

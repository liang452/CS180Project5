import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Useful functions for displaying the marketplace.
 */
public abstract class Market {
    private ArrayList<Product> listedProducts;

    public static ArrayList<Product> displayAllStores() throws IOException {
        ArrayList<Product> listedProducts = new ArrayList<>();
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        ArrayList<String> sellers = new ArrayList<>();
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (User.accountType(loginDetails[0]) == 1) {
                sellers.add(loginDetails[0]);
            }
            line = bfr.readLine();
        }
        bfr.close();
        //iterate through seller username list
        for (int i = 0; i < sellers.size(); i++) {
            File f = new File(sellers.get(i) + ".csv");
            if (f.exists()) {
                //read from user file, display as it iterates through
                bfr = new BufferedReader(new FileReader(f));
                line = bfr.readLine();
                while (line != null) {
                    String[] productDetails = line.split(",");
                    for (int j = 0; i < productDetails.length; i += 4) {
                        String name = productDetails[j];
                        String storeName = productDetails[j + 1];
                        String description = productDetails[j + 2];
                        int quantity = Integer.parseInt(productDetails[j + 3]);
                        double price = Double.parseDouble(productDetails[j + 4]);
                        listedProducts.add(new Product(name, storeName, description, quantity, price));
                        System.out.println(name + " - " + storeName + " - $" + price);
                    }
                    line = bfr.readLine();
                }
            }
        }
        return listedProducts;
    }
    public static boolean displayProductsMenu(ArrayList<Product> products) {
        Scanner scan = new Scanner(System.in);
        System.out.println("1 - Select a Product You are Interested in");
        System.out.println("2 - Search for a Product by Keyword");
        System.out.println("3 - Return to the Main Menu");
        String option = scan.nextLine();
        if (option.equals("1")) {
            System.out.println("Input the product name:");
            String selection = scan.nextLine();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getName().equals(selection)) {
                    Product viewing = products.get(i);
                    System.out.println(viewing.getName());
                    System.out.println("Store: " + viewing.getStore());
                    System.out.println("Description: " + viewing.getDescription());
                    System.out.println("Amount in Stock: " + viewing.getQuantity());
                    System.out.println("$" + viewing.getPrice());

                    //mini little menu within
                    boolean looping = false;
                    do {
                        System.out.println("1 - Purchase");
                        System.out.println("2 - Add To Cart");
                        String buy = scan.nextLine();
                        if (buy.equals("1")) {
                            System.out.println("How much would you like to purchase?");
                            String amt = scan.nextLine();
                            if (!Util.isNumeric(amt)) {
                                System.out.println("Please input a number.");
                            }
                            System.out.println("Purchasing...");
                            products.remove(i);
                            viewing.removeQuantity(Integer.parseInt(amt));
                            products.add(viewing);
                            System.out.println("You have bought " + amt + " " + viewing.getName());
                            //add to purchase history as well
                            return true;
                        } else if (buy.equals("2")) {
                            return true;
                        } else {
                            System.out.println("Please select a valid option.");
                            looping = true;
                        }
                    } while (looping);
                }
            }
            System.out.println("No matching products found.");
        } else if (option.equals("2")) {
            return true;
        } else if (option.equals("3")) {
            return false;
        }
        return true;
    }
    public static void editAccountMenu(String username, String email, String password) {
        //TODO
        System.out.println("1 - Change Your Email");
        System.out.println("2 - Change Your Password");
        System.out.println("3 - Change Your Username");
        System.out.println("4 - Delete Your Account");
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Useful functions for displaying the marketplace.
 */
public class Market {
    private ArrayList<Product> listedProducts;
    private ArrayList<String> sellerNames;
    public Market() throws IOException {
        ArrayList<Product> listedProducts = new ArrayList<>();
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        this.sellerNames = new ArrayList<>();
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (User.accountType(loginDetails[0]).equals("SELLER")) {
                this.sellerNames.add(loginDetails[0]);
            }
            line = bfr.readLine();
        }
        bfr.close();

        this.listedProducts = new ArrayList<>();
        for (int i = 0; i < this.sellerNames.size(); i++) {
            File f = new File(this.sellerNames.get(i) + ".csv");
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
                        this.listedProducts.add(new Product(name, storeName, description, quantity, price));
                    }
                    line = bfr.readLine();
                }
            }
        }
    }
    public ArrayList<Product> displayAllProducts() {
        //iterate through listedProducts
        for (int i = 0; i < this.listedProducts.size(); i++) {
            Product product = this.listedProducts.get(i);
            String name = product.getName();
            ;
            String storeName = product.getStore();
            double price = product.getPrice();
            System.out.println(name + " - " + storeName + " - $" + price);
        }
        return this.listedProducts;
    }

    public boolean displayProductsMenu(ArrayList<Product> products) {
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
            System.out.println("What would you like to search for?");
            String search = scan.nextLine();
            //TODO search through names, stores, descriptions.
            this.searchProducts(search);
        } else if (option.equals("3")) {
            return false;
        }
        return true;
    }
    public ArrayList<Product> searchProducts(String input) {
        ArrayList<Product> matches = new ArrayList<>();
        //loop through listedProducts
        for (int i = 0; i < listedProducts.size(); i++) {
            String name = listedProducts.get(i).getName();
            String storeName = listedProducts.get(i).getStore();
            String description = listedProducts.get(i).getDescription();
            if (name.contains(input)) {
                matches.add(listedProducts.get(i));
            } else if (storeName.contains(input)) {
                matches.add(listedProducts.get(i));
            } else if (description.contains(input)) {
                matches.add(listedProducts.get(i));
            }
        }
        return matches;
    }
    public static boolean editAccountMenu(String username, String email, String password) throws IOException {
        //TODO
        Scanner scan = new Scanner(System.in);
        boolean repeat;
        do {
            repeat = false;
            System.out.println("1 - Change Your Email");
            System.out.println("2 - Change Your Password");
            System.out.println("3 - Change Your Username");
            System.out.println("4 - Delete Your Account");
            System.out.println("5 - Return to Main Menu");
            String input = scan.nextLine();
            boolean valid;
            if (input.equals("1")) {
                do {
                    System.out.println("What would you like your new email to be? Type CANCEL to return.");
                    String emailInput = scan.nextLine();
                    valid = Util.isValidEmail(emailInput);
                    if (!valid) { //if invalid email
                        System.out.println("Please input a valid email.");
                    } else if (emailInput.equals("CANCEL")) {
                        break;
                    } else {
                        //TODO: change email in files.
                    }
                } while(!valid);
            } else if (input.equals("2")) {
                System.out.println("What would you like your new password to be?");
            } else if (input.equals("3")) {

            } else if (input.equals("4")) {
                System.out.println("Are you sure?");
                String sure = scan.nextLine();
                if (!Util.yesNo(sure)) {
                    System.out.println("OK! Returning to menu...");
                    break;
                }
                User.deleteAccount(username, email, password);
                System.out.println("Account successfully deleted!");
                System.out.println("Logging you out...");
                return false;
            } else if (input.equals("5")) {

            }
        } while (repeat);
        return true;
    }
    public void editProductsMenu() {
        System.out.println("1 - Create a New Product");
        System.out.println("2 - Edit an Existing Product");
        System.out.println("3 - Delete a Product");
        System.out.println("4 - Return");
    }
}

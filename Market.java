import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/*
 * Useful functions for displaying the marketplace.
 */
public class Market {
    private ArrayList<Product> listedProducts;
    private ArrayList<String> sellerNames;

    public Market() throws IOException {
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        this.sellerNames = new ArrayList<>();
        this.listedProducts = new ArrayList<>();
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (User.accountType(loginDetails[0]).equals("SELLER")) {
                this.sellerNames.add(loginDetails[0]); //adds username
            }
            line = bfr.readLine();
        }
        bfr.close();

        this.listedProducts = new ArrayList<>();
        for (String name : sellerNames) {
            File f = new File(name + ".csv");
            if (f.exists()) {
                //read from user file, display as it iterates through
                bfr = new BufferedReader(new FileReader(f));
                line = bfr.readLine();
                while (line != null) {
                    String[] productDetails = line.split(",");
                    for (int i = 0;  i < productDetails.length; i += 5) {
                        String prodName = productDetails[i];
                        String storeName = productDetails[i + 1];
                        String description = productDetails[i + 2];
                        int quantity = Integer.parseInt(productDetails[i + 3]);
                        double price = Double.parseDouble(productDetails[i + 4]);
                        this.listedProducts.add(new Product(prodName, storeName, description, quantity, price));
                    }
                    line = bfr.readLine();
                }
            }
        }
    }

    public ArrayList<Product> listProducts() {
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
        boolean repeat;
        do {
            repeat = false;
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
                            System.out.println("3 - Cancel");
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
                            } else if (buy.equals("3")) {
                                looping = false;
                                break;
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
                ArrayList<Product> results = this.searchProducts(search);
                if (results.isEmpty()) {
                    System.out.println("No matching products found.");
                } else {
                    System.out.println("Your Search Results: ");
                    for (Product product : results) {
                        product.displayProduct();
                    }
                }
                repeat = true;
            } else if (option.equals("3")) {
                return false;
            }
        } while(repeat);
        return true;
    }

    public ArrayList<Product> searchProducts(String input) {
        ArrayList<Product> matches = new ArrayList<>();
        input = input.toUpperCase();
        //loop through listedProducts
        for (int i = 0; i < listedProducts.size(); i++) {
            String name = listedProducts.get(i).getName().toUpperCase();
            String storeName = listedProducts.get(i).getStore().toUpperCase();
            String description = listedProducts.get(i).getDescription().toUpperCase();
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
                    String newEmail = scan.nextLine();
                    valid = Util.isValidEmail(newEmail);
                    if (newEmail.equals("CANCEL")) { //if invalid email
                        valid = true;
                        repeat = true;
                    } else if (!valid) {
                        System.out.println("Please input a valid email.");
                    } else {
                        //TODO: change email in files.
                        repeat = true;
                        User.editEmail(email, newEmail);
                        email = newEmail;
                        System.out.println("Successfully changed!");
                    }
                } while (!valid);
            } else if (input.equals("2")) {
                System.out.println("What would you like your new password to be? Type CANCEL to exit.");
                String newPassword = scan.nextLine();
                if (!newPassword.equals("CANCEL")) {
                    User.editPassword(email, newPassword);
                    password = newPassword;
                    System.out.println("Successfully changed.");
                }
                repeat = true;
            } else if (input.equals("3")) {
                System.out.println("What would you like your new username to be? Type CANCEL to exit.");
                String newUser = scan.nextLine();
                if (!newUser.equals("CANCEL")) {
                    User.editUsername(username, newUser);
                    System.out.println("Successfully changed!");
                    username = newUser;
                    System.out.println("This is your new username: " + username);
                }
                repeat = true;
            } else if (input.equals("4")) {
                System.out.println("Are you sure?");
                String sure = scan.nextLine();
                if (!Util.yesNo(sure)) {
                    System.out.println("OK! Returning to menu...");
                    repeat = false;
                } else {
                    User.deleteAccount(username, email, password);
                    System.out.println("Account successfully deleted!");
                    System.out.println("Logging you out...");
                    return false;
                }
            } else if (input.equals("5")) {
                repeat = false;
            }
        } while (repeat);
        return true;
    }

    public void editProductsMenu(Seller user) {
        boolean loop;
        Scanner scan = new Scanner(System.in);
        do {
            loop = false;
            System.out.println("1 - Create a New Product");
            System.out.println("2 - Edit an Existing Product");
            System.out.println("3 - Delete a Product");
            System.out.println("4 - Return");
            String input = scan.nextLine();

            if (input.equals("1")) {
                System.out.println("What store would you like to add to?");
                String storeName = scan.nextLine();
                Product product = Product.createProductFromUserInput(storeName);
                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("2")) {
                System.out.println("What product would you like to edit?");
                String productName = scan.nextLine();
                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("3")) {
                System.out.println("What product would you like to edit?");
                String productName = scan.nextLine();
                //search for product with matching name
                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("4")) {
                System.out.println("Returning to main menu...");
                break;
            } else {
                System.out.println("Please select a valid option.");
                loop = true;
            }
        } while (loop);
    }
}

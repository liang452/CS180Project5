import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;
/*
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //creates logins file
        File f = new File("logins.csv");
        if (!f.exists()) {
            f.createNewFile();
        }

        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome!");
        String existing;
        do {
            System.out.println("Are you an existing user?");
            existing = scan.nextLine();
            if (!existing.equalsIgnoreCase("no") && !existing.equalsIgnoreCase("yes")) {
                System.out.println("Incorrect input. Try again, or type CANCEL to exit.");
                String cancel = scan.nextLine();
                if (cancel.equals("CANCEL")) {
                    return;
                }
            }
        } while (!existing.equalsIgnoreCase("no") && !existing.equalsIgnoreCase("yes"));

        String username = "";
        String email = "";
        String password = "";

        boolean incorrectInput;
        if (!Util.yesNo(existing)) { //if not an existing user
            do {
                //username
                incorrectInput = false;
                System.out.println("Input your username: ");
                username = scan.nextLine();

                if (User.isExistingUser(username)) {
                    System.out.println("That username is taken already. Try again, or type CANCEL to exit.");
                    String cancel = scan.nextLine();
                    if (cancel.equals("CANCEL")) {
                        return;
                    }
                    incorrectInput = true;
                }
            } while (incorrectInput);
            //email
            do {
                incorrectInput = false;
                System.out.println("Input your email: ");
                email = scan.nextLine();

                //check if email is already take
                if (User.isExistingEmail(email)) {
                    System.out.println("That email is taken already. Try again, or type CANCEL to exit.");
                    String cancel = scan.nextLine();
                    if (cancel.equals("CANCEL")) {
                        return;
                    }
                    incorrectInput = true;
                    email = "";
                }
            } while (incorrectInput);

            do {
                incorrectInput = false;
                System.out.println("Input your password:");
                password = scan.nextLine();
                //checks if password contains commas
                if (password.contains(",")) {
                    System.out.println("Please do not use commas.");
                    incorrectInput = true;
                }
            } while (incorrectInput);
        } else if (Util.yesNo(existing)) { //if user is an existing user
            //email
            do {
                incorrectInput = false;
                System.out.println("Input your email:");
                email = scan.nextLine();

                if (!User.isExistingEmail(email)) {
                    System.out.println("Not an existing email. Try again, or type CANCEL to exit.");
                    incorrectInput = true;
                    email = "";
                } else if (email.equals("CANCEL")) {
                    return;
                }
            } while(incorrectInput);
            //password
            do {
                incorrectInput = false;
                System.out.println("Input your password: ");
                password = scan.nextLine();

                //if wrong password
                if (!User.checkPassword(email, password)) {
                    System.out.println("Wrong password. Try again, or type CANCEL to exit.");
                    incorrectInput = true;
                } else if (password.equals("CANCEL")) {
                        return;
                }
            } while(incorrectInput);
            System.out.println("Logged in successfully. Welcome back!");
        }

        //if not existing, instantiates
        String accountType = "";
        if (existing.equalsIgnoreCase("no")) {
            do {
                incorrectInput = false;
                System.out.println("Are you a seller or customer?");
                accountType = scan.nextLine();
                if (!accountType.equalsIgnoreCase("seller") && !accountType.equalsIgnoreCase("customer")) {
                    System.out.println("Please input a valid option.");
                    incorrectInput = true;
                }
            } while (incorrectInput);
            System.out.println("Welcome! You have successfully made an account.");
        }
        //TODO: if an existing user

        User user;
        if (User.accountType(username) == 1 || accountType.equalsIgnoreCase("seller")) {
            user = new Seller(username, email, password);
        } else {
            user = new Customer(username, email, password);
        }

        //seller specific part; asks if
        if (user instanceof Seller) {
            if (!Util.yesNo(existing) || ((Seller) user).getStore() == null) {
                do {
                    incorrectInput = false;
                    System.out.println("Would you like to set up your store manually?");
                    String input = scan.nextLine();
                    System.out.println("What would you like your store name to be?");
                    String storeName = scan.nextLine();
                    ArrayList<Product> products = new ArrayList<>();
                    Store store;
                    if (Util.yesNo(input)) {
                        boolean repeat;
                        do {
                           products.add(Product.createProductFromUserInput(storeName));
                            System.out.println("Would you like to create another product?");
                            repeat = Util.yesNo(scan.nextLine());
                        } while(repeat);
                        store = new Store(storeName, products);
                    } else {
                        System.out.println("Please input a .csv file name to upload your products.");
                        String filename = scan.nextLine();
                        boolean checker;
                        do {
                            store = new Store(filename);
                            checker = true;
                            if (!store.getProducts().isEmpty()) {
                                checker = false;
                            }
                        } while(checker);
                        System.out.println("Successfully imported!");
                    }
                    store.displayStore();
                    System.out.println("Is this what you want?");
                    String ans = scan.nextLine();

                    if (Util.yesNo(ans)) {
                        System.out.println("OK! Saving data...");
                        //TODO: write out to file
                        //and done with initial creation of store!
                        ((Seller) user).addStore(store);
                        ((Seller) user).exportToFile(); //TODO: fix this
                        System.out.println("Successfully saved!");
                    } else {
                        System.out.println("Returning to editing stage...");
                        incorrectInput = true;
                    }
                } while(incorrectInput);
            }
        }

        Market market = new Market();
        if (user instanceof Customer) {
            //if customer, go straight to displaying stores.
            //print out options:
            String input = "";
            do {
                System.out.println("1 - View Marketplace");
                System.out.println("2 - View Your Cart");
                System.out.println("3 - View Statistics");
                System.out.println("4 - View Your Past Purchases");
                System.out.println("5 - Edit Account");
                System.out.println("6 - Log Out");

                input = scan.nextLine();

                if (!Util.isNumeric(input)) {
                    System.out.println("Please input a valid option.");
                }
                else if (input.equals("1")) {
                    boolean looping;
                    do {
                        ArrayList<Product> listedProducts = market.displayAllProducts();
                        looping = market.displayProductsMenu(listedProducts);
                        if (!looping) {
                            input = "0"; //returns to main menu if displayProductsMenu returns false
                        }
                    } while(looping);
                } else if (input.equals("2")) {
                    //display cart
                } else if (input.equals("3")) {
                    //display statistics menu
                } else if (input.equals("4")) {
                    //view past purchases
                } else if (input.equals("5")) {
                    //print out account details
                    boolean deleted = Market.editAccountMenu(username, email, password);
                    if (deleted) {
                        input = "0";
                        break;
                    }
                    System.out.println("1 - Continue Editing Account");
                    System.out.println("2 - Return to Main Menu");
                    //TODO
                } else if (input.equals("6")) {
                    System.out.println("Are you sure you want to log out?");
                    String logout = scan.nextLine();
                    if (Util.yesNo(logout)) {
                        //call save data method
                        System.out.println("Have a nice day!");
                        return;
                    } else if (!Util.yesNo(logout)) {
                        System.out.println("Returning to the main menu...");
                        input = "0";
                    }
                }
            } while(input.equals("0"));

        }
        if (user instanceof Seller) {
            System.out.println("1 - View Your Products");
            System.out.println("2 - View Your Sales By Store");
            System.out.println("3 - View Statistics");
            System.out.println("4 - View Customer Shopping Carts");
            System.out.println("5 - Edit Account Details");
            System.out.println("6 - Logout");
        }
    }
}
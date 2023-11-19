import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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

        JOptionPane.showMessageDialog(null, "Welcome to Vellichor!", "Vellichor",
                JOptionPane.PLAIN_MESSAGE);

        String email = "";
        String username = "";
        String password = "";
        String accountType = "";

        String[] loginDetails;
        boolean existingAccount = false;
        boolean repeat = false;
        //initial page
        //loop if select cancel on login or create account page
        do {
            String[] options = {"Login", "Create Account", "Exit"};
            String init = String.valueOf(JOptionPane.showOptionDialog(null, "What would you like to do?", "Vellichor",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]));

            if (init.equals("1")) {
                //if "Create Account" is selected
                loginDetails = Market.userInitialization();
                if (!loginDetails[0].equals("CANCEL")) {
                    username = loginDetails[0];
                    email = loginDetails[1];
                    password = loginDetails[2];
                    accountType = loginDetails[3];
                    existingAccount = false;
                    JOptionPane.showMessageDialog(null, "You have successfully created an account!");
                } else {
                    repeat = true;
                }
            } else if (init.equals("0")) {
                //if login is selected
                loginDetails = Market.userLogin();
                if (!loginDetails[0].equals("CANCEL")) {
                    username = loginDetails[0];
                    email = loginDetails[1];
                    password = loginDetails[2];
                    accountType = loginDetails[3];
                    existingAccount = true;
                } else {
                    repeat = true;
                }
            } else if (init.equals("2")) {
                //if EXIT is selected
                JOptionPane.showMessageDialog(null, "Thank you for using Vellichor, and have a nice day!");
                return;
            }
        } while (repeat);

        //if not existing, instantiates


        User user;
        if (accountType.equals("SELLER")) {
            user = new Seller(username, email, password);
        } else {
            user = new Customer(username, email, password);
            ((Customer) user).exportToFile();
        }

        //seller specific part; if not a previously existing seller
        if (user instanceof Seller && (!existingAccount) && !new File (username + "csv").exists()) {
            boolean invalidInput;
            do {
                invalidInput = false;
                System.out.println("Would you like to set up your store manually?");
                String input = scan.nextLine();
                System.out.println("What would you like your store name to be?");
                String storeName = scan.nextLine();
                ArrayList<Book> products = new ArrayList<>();
                Store store;
                if (Util.yesNo()) {
                    repeat = false;
                    do {
                        products.add(Book.createBookFromUserInput());
                        System.out.println("Would you like to create another product?");
                        repeat = Util.yesNo();
                    } while (repeat);
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
                    } while (checker);
                    System.out.println("Successfully imported!");
                }
                store.displayStore();
                System.out.println("Is this what you want?");
                boolean ans = Util.yesNo();

                if (ans) {
                    System.out.println("OK! Saving data...");
                    //initial creation of store
                    ((Seller) user).addStore(store);
                    ((Seller) user).exportToFile();
                    System.out.println("Successfully saved!");
                } else {
                    System.out.println("Returning to editing stage...");
                    invalidInput = true;
                }
            } while (invalidInput);
        }


        Market market = new Market(user);

        if (user instanceof Customer) {
            //print out options:
            String input = "";
            do {
                System.out.println("1 - View Marketplace");
                System.out.println("2 - View Your Cart");
                System.out.println("3 - View Your Past Purchases");
                System.out.println("4 - View Statistics");
                System.out.println("5 - Edit Account");
                System.out.println("6 - Log Out");

                String[] optionsText = {"View Marketplace","View Your Cart","View Your Past Purchases", "Edit " +
                        "Account", "Log Out"};
                String option = (String) JOptionPane.showInputDialog(null, "What would you like to do?", "Menu",
                        JOptionPane.OK_OPTION, null, optionsText, optionsText[0]);


                if (option.equals(optionsText[0])) {
                    boolean looping;
                    do {
                        market.displayMarket();
                        looping = market.displayProductsMenu();
                        if (!looping) {
                            input = "0"; //returns to main menu if displayProductsMenu returns false
                        }
                    } while(looping);
                } else if (input.equals("2")) {
                    //display cart
                    market.viewCartMenu((Customer) user);
                    input = "0";
                } else if (input.equals("3")) {
                    //view past purchases
                    ((Customer) user).viewPastPurchases();
                    market.pastPurchasesMenu(user);
                    input = "0";
                } else if (input.equals("4")) {
                    //view statistics

                } else if (input.equals("5")) {
                    //print out account details
                    boolean deleted = Market.editAccountMenu(username, email, password);
                    if (deleted) {
                        input = "0";
                    } else {
                        System.out.println("1 - Continue Editing Account");
                        System.out.println("2 - Return to Main Menu");
                    }
                    //TODO
                } else if (input.equals("6")) {
                    System.out.println("Are you sure you want to log out?");
                    boolean logout = Util.yesNo();
                    if (logout) {
                        //call save data method
                        System.out.println("Have a nice day!");
                        ((Customer) user).exportToFile();
                        return;
                    } else if (logout) {
                        System.out.println("Returning to the main menu...");
                        input = "0";
                    }
                }
            } while(input.equals("0"));

        }

        if (user instanceof Seller) {
            String input;
            repeat = false;
            do {
                System.out.println("1 - View Your Products");
                System.out.println("2 - View Your Sales By Store");
                System.out.println("3 - View Statistics");
                System.out.println("4 - View Customer Shopping Carts");
                System.out.println("5 - Edit Account Details");
                System.out.println("6 - Logout");
                input = scan.nextLine();

                if (input.equals("1")) {
                    do {
                        ((Seller) user).displayProducts();

                        System.out.println("1 - Edit Products");
                        System.out.println("2 - Export as File");
                        System.out.println("3 - Return to Main Menu");
                        String edit = scan.nextLine();
                        if (edit.equals("1")) {
                            market.editProductsMenu((Seller) user);
                            ((Seller) user).exportToFile();
                            repeat = true;
                        } else if (edit.equals("2")) {
                            System.out.println("Input a filename: ");
                            String filename = scan.nextLine();
                            ((Seller) user).exportProducts(filename);
                            System.out.println("Successfully exported!");
                            System.out.println("Returning to main menu...");
                            repeat = false;
                        } else if (edit.equals("3")) {
                            System.out.println("Returning to main menu...");
                            repeat = false;
                        } else {
                            System.out.println("Please select a valid option.");
                            repeat = true;
                        }
                    } while(repeat);

                } else if (input.equals("2")) {
                    //view sales by store
                    market.viewSalesByStore();
                } else if (input.equals("3")) {
                    //statistics
                } else if (input.equals("4")) {
                    //view customer shopping carts
                } else if (input.equals("5")) {
                    boolean deleted;
                    do {
                        deleted = Market.editAccountMenu(username, email, password); //returns true if account has been deleted
                        ((Seller) user).exportToFile();
                        if (deleted) {
                            input = "0";
                            break;
                        } //if user chooses to cancel, just keep looping
                    } while (!deleted); //loops if deleted is false.
                } else if (input.equals("6")) {
                    //logout
                    System.out.println("Are you sure you want to log out?");
                    boolean logout = Util.yesNo();
                    if (logout) {
                        System.out.println("Have a nice day!");
                        ((Seller) user).exportToFile();
                        break;
                    } else {
                        System.out.println("Returning to main menu...");
                    }
                }
            } while(!input.equals("0"));
        }
    }
}
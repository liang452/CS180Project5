import java.io.File;
import java.io.IOException;
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
        f = new File("storelist.csv");
        if (!f.exists()) {
            f.createNewFile();
        }
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome!");
        String existing = "";
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
        if (!Util.yesNo(existing)) {
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
        } else if (Util.yesNo(existing)) {
            //if user is an existing user
            //email
            do {
                incorrectInput = false;
                System.out.println("Input your email:");
                email = scan.nextLine();
                //email does not exist
                if (!User.isExistingEmail(email)) {
                    System.out.println("Not an existing email. Try again, or type CANCEL to exit.");
                    String cancel = scan.nextLine();
                    if (cancel.equals("CANCEL")) {
                        return;
                    }
                    incorrectInput = true;
                }
            } while(incorrectInput);
            //password
            do {
                System.out.println("Input your password: ");
                password = scan.nextLine();

                //if wrong password
                if (!User.checkPassword(email, password)) {
                    System.out.println("Wrong password. Try again, or type CANCEL to exit.");
                    String cancel = scan.nextLine();
                    if (cancel.equals("CANCEL")) {
                        return;
                    }
                    incorrectInput = true;
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
                    Store store = new Store(storeName);

                    if (Util.yesNo(input)) {
                        boolean repeat = false;
                        do {
                            store.addProduct(Product.createProductFromUserInput(storeName));
                            System.out.println("Would you like to create another product?");
                            repeat = Util.yesNo(scan.nextLine());
                        } while(repeat);

                    } else {
                        System.out.println("Please input a .csv file name to upload your products.");
                        String filename = scan.nextLine();
                        boolean checker;
                        do {
                            checker = store.importProducts(filename);
                        } while (!checker);
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
                        ((Seller) user).exportToFile();
                        System.out.println("Successfully saved!");
                    } else {
                        System.out.println("Returning to editing stage...");
                        incorrectInput = true;
                    }
                } while(incorrectInput);
            }
        }

        if (user instanceof Customer) {
            //if customer, go straight to displaying stores.

        }
    }
}
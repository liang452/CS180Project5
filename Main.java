import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("logins.csv");
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
        if (existing.equalsIgnoreCase("no")) {
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
        } else if (existing.equalsIgnoreCase("yes")) {
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
        //KNOWN ISSUE HERE: will keep putting in same logins. FIX.
        User user;
        if (User.accountType(username) == 1 || accountType.equalsIgnoreCase("seller")) {
            user = new Seller(username, email, password);
        } else {
            user = new Customer(username, email, password);
        }

        if (user instanceof Seller) {
            if (existing.equalsIgnoreCase("no") || ((Seller) user).getStore().isEmpty()) {
                do {
                    incorrectInput = false;
                    System.out.println("What would you like your store name to be?");
                    String storeName = scan.nextLine();

                    System.out.println("Please input a .csv file name to upload your products.");
                    String filename = scan.nextLine();

                    Store storeOne = new Store(storeName, filename);
                    System.out.println("This is your store: ");
                    storeOne.displayStore();

                    System.out.println("Is this what you want?");
                    String ans = scan.nextLine();

                    if (ans.equalsIgnoreCase("yes")) {
                        ((Seller) user).addStore(storeOne);
                    } else if (ans.equals("no")) {
                        incorrectInput = true;
                    } else {
                        System.out.println("This is a yes or no question.");
                        incorrectInput = true;
                    }
                } while(incorrectInput);
            }
        }
    }
}
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("logins.txt");
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
                System.out.println("Incorrect input.");
            }
        } while (!existing.equalsIgnoreCase("no") && !existing.equalsIgnoreCase("yes"));

        String username = "";
        String email = "";
        String password = "";

        boolean incorrectInput = false;
        if (existing.equalsIgnoreCase("no")) {
            do {
                //username
                incorrectInput = false;
                System.out.println("Input your username: ");
                username = scan.nextLine();

                if (User.isExistingUser(username)) {
                    System.out.println("That username is taken already.");
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
                    System.out.println("That email is taken already.");
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
            System.out.println("Welcome! You have successfully made an account.");
        } else if (existing.equalsIgnoreCase("yes")) {
            //if user is an existing user
            //email
            do {
                incorrectInput = false;
                System.out.println("Input your email:");
                email = scan.nextLine();
                //username does not exist
                if (!User.isExistingUser(email)) {
                    System.out.println("Not an existing email. Try again.");
                    incorrectInput = true;
                }
            } while(incorrectInput);
            //password
            do {
                System.out.println("Input your password: ");
                password = scan.nextLine();

                //if wrong password
                if (!User.checkPassword(email, password)) {
                    System.out.println("Wrong password. Try again.");
                    incorrectInput = true;
                }
            } while(incorrectInput);
            System.out.println("Logged in successfully. Welcome back!");
        }

        //if not existing, instantiates
        if (existing.equalsIgnoreCase("no")) {
            System.out.println("Are you a seller or customer?");
            String type = scan.nextLine();
            if (type.equalsIgnoreCase("seller")) {
                Seller user = new Seller(username, email, password);
            } else if (type.equalsIgnoreCase("customer")){
                Customer user = new Customer(username, email, password);
            } else {
                System.out.println("Please input a valid option.");
                //put into a do while
            }
        } else {
            //TODO: if an existing user

        }
    }
}
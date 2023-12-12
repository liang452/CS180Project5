import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
 *
 */
public class Driver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket socket = new Socket("localhost", 8484);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.flush();

        String email  = "";
        String username = "";
        String password = "";
        String accountType = "";

        ArrayList<Book> cart = null;
        ArrayList<Book> pastPurchases = null;
        ArrayList<Book> products = null;


        String[] accountDetails = new String[4];
        boolean existingAccount = false;
        boolean repeat;
        //initial page
        //loop if select cancel on login or create account page
        do {
            repeat = false;
            String[] options = {"Login", "Create Account", "Exit"};
            String init = String.valueOf(JOptionPane.showOptionDialog(null, "Welcome to Vellichor! What would you" +
                    " like to do?", "Vellichor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null));
            if (init.equals("0")) {
                //if login is selected
                existingAccount = true;
                boolean invalid = false;
                do {
                    oos.writeObject("LOGIN"); //sends to server
                    oos.flush();
                    String[] loginDetails = Market.userLogin();
                    if (!loginDetails[0].equals("CANCEL")) {
                        oos.writeObject(loginDetails[1]); //email
                        oos.flush();
                        System.out.println(loginDetails[1] + " sent to server");
                        String confirm = (String) ois.readObject();
                        System.out.println("Received " + confirm);
                        System.out.println(confirm.equals("INVALID"));
                        if (confirm.equals("INVALID")) {
                            JOptionPane.showMessageDialog(null, "Invalid email. Try again!");
                            invalid = true;
                        } else {
                            oos.writeObject(loginDetails[2]); //password
                            oos.flush();
                            confirm = (String) ois.readObject();
                            System.out.println("Password: " + confirm);
                            if (confirm.equals("INVALID")) {
                                JOptionPane.showMessageDialog(null, "Incorrect password. Try again!");
                                invalid = true;
                            } else if (confirm.equals("CANCEL")) {
                                repeat = true;
                            } else {
                                username = loginDetails[0];
                                email = loginDetails[1];
                                password = loginDetails[2];
                                accountType = loginDetails[3];
                                JOptionPane.showMessageDialog(null, "Logged in successfully. Welcome back!");
                                ois.readObject();
                                ois.readObject();
                                if (accountType.equals("CUSTOMER")) {
                                    String cartLine = (String) ois.readObject();
                                    if (!cartLine.equals("EMPTY")) {
                                        cart = Util.readCSVToBook(cartLine);
                                    }
                                    String pastLine = (String) ois.readObject();
                                    if (!pastLine.equals("EMPTY")) {
                                        pastPurchases = Util.readCSVToBook(pastLine);
                                    }
                                }
                            }
                        }
                    } else {
                        repeat = true;
                        break;
                    }
                } while (invalid);
            } else if (init.equals("1")) {
                //if "Create Account" is selected
                oos.writeObject("CREATE ACCOUNT");
                boolean free;
                do {
                    free = false;
                    accountDetails = Market.userInitialization();
                    if (!accountDetails[0].equals("CANCEL")) {
                        username = accountDetails[0];
                        email = accountDetails[1];
                        password = accountDetails[2];
                        accountType = accountDetails[3];

                        oos.writeObject(accountDetails);
                        oos.flush();
                        free = ois.readBoolean();
                        if (free) {
                            System.out.println("Account valid!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Try again! Username or email taken already.");
                            free = false;
                        }
                    } else {
                        free = true;
                        repeat = true;
                    }
                } while (!free);
            } else if (init.equals("2")) {
                //if EXIT is selected
                JOptionPane.showMessageDialog(null, "Thank you for using Vellichor, and have a nice day!");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "Thank you for using Vellichor, and have a nice day!");
                return;
            }
        } while (repeat);

        User user;
        if (accountType.equalsIgnoreCase("SELLER")) {
            user = new Seller(username, email, password); //creates new seller
            //if not a previously existing seller
            if (!existingAccount) {
                boolean cancel;
                Store store = null;
                String storeName = "";
                do {
                    cancel = false;
                    if (Util.yesNo("Would you like to set up your store manually?", "Store Set-up")) {
                        products = new ArrayList<>();
                        do {
                            Book newBook = Book.createBookFromUserInput("");
                            if (newBook.getName().equals("")) { //if user hits cancel at any point
                                cancel = true;
                                break;
                            } else {
                                storeName = newBook.getStore();
                                products.add(newBook);
                            }
                            repeat = Util.yesNo("Would you like to create another product?", "Store Set-up");
                        } while (repeat); //loops book creation process until user is happy
                        store = new Store(storeName, products); //creates new store with storename and products
                        ((Seller) user).addProducts(products);
                    } else {
                        String filename = JOptionPane.showInputDialog("Please input a .csv file name to upload your products.");
                        boolean checker;
                        if (filename != null) {
                            do {
                                store = new Store(username,filename); //automatically reads in
                                checker = true;
                                if (!store.getProducts().isEmpty()) { //if store isn't empty
                                    checker = false;
                                }
                            } while (checker);
                            store.displayStore();
                            JOptionPane.showMessageDialog(null, "Successfully imported!");
                        } else {
                            cancel = true;
                        }
                    }

                    if (!cancel) { //TODO: fix. this is messy. if cancel is false
                        store.displayStore();
                        boolean ans = Util.yesNo("Is this what you want?", "Store Set-up");

                        if (ans) {
                            System.out.println("OK! Saving data...");
                            //initial creation of store
                            ((Seller) user).addStore(store);
                            ((Seller) user).exportToFile();
                            System.out.println("Successfully saved!");
                        } else {
                            System.out.println("Returning to editing stage...");
                            cancel = true;
                        }
                    } else {
                        cancel = true;
                    }
                } while (cancel);
            }
        } else {
            user = new Customer(username, email, password);
            ((Customer) user).setCart(cart);
            ((Customer) user).setPastPurchases(pastPurchases);
        }

        Market market = new Market(user);

        //what the customer will see
        if (user instanceof Customer) {
            do {
                repeat = false;
                String[] options = {"View Marketplace","View Your Cart","View Your Past Purchases", "Edit " +
                        "Account", "Log Out"};
                String input = (String) JOptionPane.showInputDialog(null, "What would you like to do?", "Menu",
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                boolean loop;
                if (input == null) {
                    break;
                } else if (input.equals(options[0])) {
                    do {
                        loop = false;
                        oos.writeObject("MARKET");
                        String output = market.displayCustomerMenu();
                        if (output.equals("RETURN")) {
                            repeat = true;
                            oos.writeObject("RETURN");
                        } else if (output.equals("LOG OUT")) {
                            JOptionPane.showMessageDialog(null, "Have a good day!");
                            break;
                        } else if (output.contains("BUY")) {
                            String[] purchase = output.split(",", 3);
                            int amount = Integer.parseInt(purchase[1]);
                            System.out.println("Purchasing: " + purchase[2]);
                            ArrayList<Book> boughtBooks = Util.readCSVToBook(purchase[2]);
                            Book boughtBook = boughtBooks.get(0);
                            ((Customer) user).addToPastPurchases(boughtBook, amount); //adds to local

                            oos.writeObject("BUY");
                            oos.flush();
                            oos.writeObject(amount);
                            oos.flush();
                            oos.writeObject(boughtBook);
                            oos.flush();
                            oos.writeObject(username);
                            oos.flush();

                            System.out.println("Finished writing info to server!");
                            System.out.println("Awaiting output...");

                            ois.readObject();
                            ois.readObject();
                            String newBookString = (String) ois.readObject();
                            System.out.println("Driver received: " + newBookString);
                            ArrayList<Book> newBookList = Util.readCSVToBook(newBookString);

                            market.setListedProducts(newBookList);

                            oos.writeObject(user);
                            oos.flush();
                            loop = true;

                            BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
                            String line = bfr.readLine();
                            System.out.println("CART: " + line);
                            line = bfr.readLine();
                            System.out.println("PAST: " + line);
                        } else if (output.contains("ADD TO CART")) {
                            oos.writeObject("ADD TO CART");
                            oos.flush();
                            String[] cartAddition = output.split(",", 3);
                            int amount = Integer.parseInt(cartAddition[1]);
                            loop = true;
                        }
                    } while(loop);
                } else if (input.equals(options[1])) { //view cart
                    System.out.println("hi");
                } else if (input.equals(options[2])) { //view past purchases
                    oos.writeObject("PAST PURCHASES");
                    oos.flush();
                    do {
                        loop = false;
                        String output = market.pastPurchasesMenu(user);
                        oos.writeObject(user);
                        if (output.equals("RETURN")) {
                            loop = false;
                            repeat = true;
                            break;
                        } else {
                            loop = true;
                        }
                    } while(loop);
                }
            } while (repeat);
        }

        if (user instanceof Seller) {
            repeat = false;
            String input;
            do {
                String[] options = new String[]{"View Your Products", "View Your Sales by Store", "View Customer " +
                        "Shopping Carts", "Edit Account Details", "Logout"};
                input = (String) JOptionPane.showInputDialog(null, "What would you like to do?", "Menu",
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (input == null) {
                    break;
                } else if (input.equals(options[0])) {
                    oos.writeObject("VIEW PRODUCTS\n");
                    oos.flush();
                    market.viewSellerProducts();
                } else if (input.equals(options[1])) {
                    oos.writeObject("VIEW SALES\n");
                    oos.flush();
                    //view sales by store
                    market.viewSalesByStore();
                } else if (input.equals(options[2])) {
                    //statistics
                } else if ((input.equals(options[3]))) {
                    oos.writeObject("CUSTOMER SHOPPING CARTS\n");
                    oos.flush();
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
                    boolean logout = Util.yesNo("Are you sure you want to log out?", "Logout");
                    if (logout) {
                        System.out.println("Have a nice day!");
                        ((Seller) user).exportToFile();
                        break;
                    } else {
                        System.out.println("Returning to main menu...");
                    }
                }
            } while(input == null && !input.equals("0"));
        }
    }
}

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
 *
 */
public class Driver {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
//        JFrame startFrame = new JFrame("Vellichor");
//        startFrame.setLocationRelativeTo(null);
//
//        JButton loginButton = new JButton("Login");
//        JButton createButton = new JButton("Create New Account");
//        JPanel buttonPane = new JPanel();
//        buttonPane.add(loginButton);
//        buttonPane.add(createButton);
//
//        JPanel textPane = new JPanel();
//        textPane.add(new JLabel("Welcome to Vellichor!"));
//        final String[] loginDetails = new String[4];
//        ActionListener actionListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (e.getSource() == loginButton) {
//                    try {
//                        startFrame.setVisible(false);
//                        loginDetails = Market.userLogin();
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//                if (e.getSource() == createButton) {
//                    try {
//                        startFrame.setVisible(false);
//                        Market.userInitialization();
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            }
//        };
//        startFrame.add(textPane, BorderLayout.CENTER);
//        startFrame.add(buttonPane, BorderLayout.SOUTH);
//        loginButton.addActionListener(actionListener);
//        createButton.addActionListener(actionListener);
//
//        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        startFrame.setSize(400, 100);
//        startFrame.setVisible(true);

        Socket socket = new Socket("localhost", 8484);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());

        String email = "";
        String username = "";
        String password = "";
        String accountType = "";

        String[] loginDetails = new String[4];
        boolean existingAccount = false;
        boolean repeat;
        //initial page
        //loop if select cancel on login or create account page
        do {
            repeat = false;
            String[] options = {"Login", "Create Account", "Exit"};
            String init = String.valueOf(JOptionPane.showOptionDialog(null, "Welcome to Vellichor! What would you" +
                            " like to do?", "Vellichor",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null));

            if (init.equals("1")) {
                //if "Create Account" is selected
                loginDetails = Market.userInitialization();
                if (!loginDetails[0].equals("CANCEL")) {
                    JOptionPane.showMessageDialog(null, "You have successfully created an account!");
                } else {
                    repeat = true;
                }
            } else if (init.equals("0")) {
                //if login is selected
                loginDetails = Market.userLogin();
                if (loginDetails[0] != null) {
                    if (!loginDetails[0].equals("CANCEL")) {
                        pw.write(Util.toCSV(loginDetails)); //writes to the server.
                        existingAccount = true;
                    } else {
                        repeat = true;
                    }
                }
            } else if (init.equals("2")) {
                //if EXIT is selected
                JOptionPane.showMessageDialog(null, "Thank you for using Vellichor, and have a nice day!");
                return;
            }
        } while (repeat);

        username = loginDetails[0];
        email = loginDetails[1];
        password = loginDetails[2];
        accountType = loginDetails[3];

        User user;
        if (accountType.equalsIgnoreCase("SELLER")) {
            user = new Seller(username, email, password); //creates new seller
            //if not a previously existing seller
            if (!existingAccount || !(new File(username + "csv").exists())) {
                boolean cancel;
                Store store = null;
                String storeName = "";
                do {
                    cancel = false;
                    if (Util.yesNo("Would you like to set up your store manually?", "Store Set-up")) {
                        ArrayList<Book> products = new ArrayList<>();
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
                                store = new Store(filename); //automatically reads in
                                checker = true;
                                if (!store.getProducts().isEmpty()) { //if store isn't empty
                                    checker = false;
                                }
                            } while (checker);
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
            ((Customer) user).exportToFile();
        }

        Market market = new Market(user);

        //what the customer will see
        if (user instanceof Customer) {
            //print out options:
            String input = "";
            do {
                String[] options = {"View Marketplace","View Your Cart","View Your Past Purchases", "Edit " +
                        "Account", "Log Out"};
                input = (String) JOptionPane.showInputDialog(null, "What would you like to do?", "Menu",
                        JOptionPane.OK_OPTION, null, options, options[0]);

                if (input.equals(options[0])) { //if "View Marketplace" is displayed
                    boolean looping = market.displayProductsMenu();
                    if (!looping) {
                        input = "0"; //returns to main menu if displayProductsMenu returns false
                    }
                } else if (input.equals(options[1])) {
                    //display cart
                    market.viewCartMenu((Customer) user);
                    input = "0";
                } else if (input.equals(options[2])) {
                    //view past purchases
                    ((Customer) user).viewPastPurchases();
                    market.pastPurchasesMenu(user);
                    input = "0";
                } else if (input.equals(options[3])) {
                    boolean deleted = Market.editAccountMenu(username, email, password);
                    if (deleted) {
                        input = "0";
                    } else {
                        System.out.println("1 - Continue Editing Account");
                        System.out.println("2 - Return to Main Menu");
                    }
                    //TODO
                } else if (input.equals(options[4])) {
                    boolean logout = Util.yesNo("Are you sure you want to log out?", "Logout");
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
                    boolean logout = Util.yesNo("Are you sure you want to log out?", "Logout");
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
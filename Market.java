import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Useful functions for displaying the marketplace.
 */
public class Market {
    private ArrayList<Book> listedProducts;
    private ArrayList<Book> boughtProducts;
    private ArrayList<String> sellerNames;
    private ArrayList<String> customerNames;
    private User user;

    public Market(User user) throws IOException {
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        this.sellerNames = new ArrayList<>();
        this.customerNames = new ArrayList<>();

        this.user = user;
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (User.accountType(loginDetails[0]).equals("SELLER")) {
                this.sellerNames.add(loginDetails[0]); //adds username
            } else {
                this.customerNames.add(loginDetails[0]);
            }
            line = bfr.readLine();
        }
        bfr.close();

        this.listedProducts = new ArrayList<>();
        this.boughtProducts = new ArrayList<>();

        for (String name : sellerNames) {
            File f = new File(name + ".csv");
            if (f.exists()) {
                ArrayList<Book> bookList = Util.readCSVToBook(name + ".csv"); //reads all products from
                // seller class
                this.listedProducts.addAll(bookList);
            }
        }

        for (String name : customerNames) {
            File f = new File(name + ".csv");
            if (f.exists()) {
                bfr = new BufferedReader(new FileReader(f));
                line = bfr.readLine();
                if (line != null && !line.isEmpty()) {
                    ArrayList<Book> purchases = Util.readCSVToBook(line); //one long line. assume no repeats of
                    // products.
                    this.boughtProducts.addAll(purchases);
                }
            }
        }
    }

    /**
     * Logs in an existing user.
     * @return A String[] containing email, username, password, and account type.
     * @throws IOException
     */
    public static String[] userLogin() throws IOException {
        String username;
        String account;

        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();
        String email = "";
        String password = "";
        boolean invalidInput;
        Object[] login = {"Email:", emailField,
                "Password:", passwordField};
        do {
            invalidInput = false;
            int option = JOptionPane.showConfirmDialog(null, login, "Login",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION ) {
                return new String[]{"CANCEL"};
            } else if (emailField.getText() == null || passwordField.getText() == null) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty");
                invalidInput = true;
            } else {
                email = emailField.getText();
                password = passwordField.getText();
            }
        } while (invalidInput);
        username = User.getUserFromEmail(email);
        account = User.accountType(email);

        return new String[]{username, email, password, account};
    }

    /**
     * Creates a new account.
     * @return a String[] containing username, email, password, and account type.
     * @throws IOException if logins file does not exist
     */
    public static String[] userInitialization() throws IOException {
        Socket socket = new Socket("localhost", 8484);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.write("CREATE ACCOUNT\n");
        pw.flush();

        String username;
        String email;
        String password;

        boolean incorrectInput;
        do {
            //username
            incorrectInput = false;
            username = JOptionPane.showInputDialog(null, "Input your username: ", "Account Creation", JOptionPane.PLAIN_MESSAGE);
            if (username == null) {
                pw.write("CANCEL\n");
                pw.flush();
                return new String[]{"CANCEL"};
            } else if (username.contains(",")) {
                JOptionPane.showMessageDialog(null, "Do not use commas.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                pw.write(username + "\n");
                pw.flush();
                String output = bfr.readLine();
                if (output.equals("EXISTING")) {
                    JOptionPane.showMessageDialog(null, "That username is taken already!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    incorrectInput = true;
                }
            }
        } while (incorrectInput);
        //email
        do {
            incorrectInput = false;
            email = JOptionPane.showInputDialog(null, "Input your email: ", "Account Creation",
                    JOptionPane.PLAIN_MESSAGE);
            //check if email is already taken
            if (email == null) {
                socket.close();
                return new String[]{"CANCEL"};
            } else if (email.contains(",")) {
                JOptionPane.showMessageDialog(null, "Please do not use commas.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else if (!email.contains("@")) {
                JOptionPane.showMessageDialog(null, "Please input a valid email.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                pw.write(email + "\n");
                pw.flush();
                String output = bfr.readLine();
                if (output.equals("EXISTING")) {
                    JOptionPane.showMessageDialog(null, "That email is taken already.", "Existing Account",
                            JOptionPane.ERROR_MESSAGE);
                    incorrectInput = true;
                }
            }
            } while (incorrectInput) ;
            do {
                incorrectInput = false;
                password = JOptionPane.showInputDialog(null, "Input your password:");

                //checks if password contains commas
                if (password == null) {
                    return new String[]{"CANCEL"};
                } else if (password.contains(",")) {
                    JOptionPane.showMessageDialog(null, "Please do not use commas.");
                    incorrectInput = true;
                } else {
                    pw.write(password + "\n");
                    pw.flush(); //don't really need any output for this; just put into file.
                }
            } while (incorrectInput);

            String[] accountOptions = {"CUSTOMER", "SELLER"};
            Object account = JOptionPane.showInputDialog(null, "What type of account would you like to create?",
                    "Account Type", JOptionPane.PLAIN_MESSAGE, null, accountOptions, accountOptions[0]);
            if (account == null) {
                return new String[]{"CANCEL"};
            } else {
                pw.write(account + "\n");
                pw.flush();
            }
            return new String[]{username, email, password, (String) account};
        }


    public ArrayList<Book> getListedProducts() {
        return this.listedProducts;
    }

    public void updateListedProducts(Book oldProduct, Book updatedProduct) {
        //TODO: this might need to combined with the method below? if multiple users are accessing at once.
        ArrayList<Book> iterator = new ArrayList<>();
        for (Book book : this.listedProducts) {
            if (!book.equals(oldProduct)) {
                iterator.add(book); //adds everything not the original product
            }
        }
        iterator.add(updatedProduct); //adds updated product to list
        this.listedProducts = iterator; //sets listedProducts to list with new item
    }

    public synchronized void exportToFiles() throws IOException { //update seller's list of products
        for (Book book : this.listedProducts) {
            for (String seller : sellerNames) { //for every seller
                String file = seller + ".csv";
                ArrayList<Book> sellerBooks = new ArrayList<>(); //arraylist of all books in one seller
                for (Book bk : Util.readCSVToBook(file)) { //if book matches title and author. this is mostly for
                    // customer's
                    // benefit; if a seller chooses to alter a product, it should directly save to their file and
                    // also be in listedProducts.
                    // the only thing that should be changing here is quantity.
                    if (bk.equals(book)) {
                        //if it's the same book, add in the one from listedProducts
                        sellerBooks.add(book);
                    }
                }
                //write in to seller file here
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (Book b : sellerBooks) {
                    bw.write(b.toCSVFormat());
                }
            }
        }
    }


    /**
     *
     * @return Returns a list of the book and author names.
     */
    public ArrayList<String> marketProducts() {
        //iterate through listedProducts
        ArrayList<String> books = new ArrayList<>();
        for (Book book : this.listedProducts) {
            books.add(book.getName() + " - " + book.getAuthor() + " - " + book.getStore());
        }
        return books;
    }

    /**
     * Main menu for customers.
     * @return
     * @throws IOException
     */
    public boolean displayProductsMenu() throws IOException {
        JFrame productsMenuFrame = new JFrame();
        productsMenuFrame.setSize(500, 300);
        productsMenuFrame.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());

        JButton searchButton = new JButton("Search");
        JTextField searchField = new JTextField(15);
        JButton returnButton = new JButton("Return");

        JPanel topPanel = new JPanel();
        topPanel.add(returnButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        productsMenuFrame.add(topPanel, BorderLayout.NORTH);
        BookPanel bookDisplay = new BookPanel(this.getListedProducts());
        JPanel bookPanel = bookDisplay.getBookPanel(); //calls method to set up panel for books
        JButton cartButton = new JButton("Add to Cart");
        JButton buyButton = new JButton("Buy");

        bookPanel.add(cartButton);
        bookPanel.add(buyButton);
        productsMenuFrame.add(bookPanel, BorderLayout.CENTER); //TODO: fix layout.
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchButton) {
                        //TODO: getText from searchText
                    String searchText = searchField.getText();
                }
                if (e.getSource() == returnButton) {
                        productsMenuFrame.setVisible(false);
                }
                if (e.getSource() == buyButton) {
                    boolean invalidInput = false;
                    if (bookDisplay.selectedBook() != null) {
                        do {
                            String amount = JOptionPane.showInputDialog("How much would you like to buy?");
                            if (amount == null) {
                                break;
                            } else if (!Util.isNumeric(amount)) {
                                JOptionPane.showMessageDialog(null, "Please input a numeric value", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                invalidInput = true;
                            }
                        } while (invalidInput);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an item to purchase");
                    }
                }
                if (e.getSource() == cartButton) {
                    JOptionPane.showMessageDialog(null, "TODO");
                }
            }
        };

        searchButton.addActionListener(al);
        returnButton.addActionListener(al);
        buyButton.addActionListener(al);
        cartButton.addActionListener(al);

        productsMenuFrame.setVisible(true);

//        Scanner scan = new Scanner(System.in);
//        boolean repeat;
//        String[] options = {"View All Books", "Search for a Book by Keyword", "Return"};
//
//    do {
//            repeat = false;
//            String choice = (String) JOptionPane.showInputDialog(null, "", "Market",
//                    JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
//
//            if (choice.equals(options[0])) { //if "View All Books" is selected
//                ArrayList<String> books = this.marketProducts(); //array of all the books available
//                if (books.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "Sorry! There are no products available to purchase.");
//                }
//                String selection = (String) JOptionPane.showInputDialog(null, "", "Market", JOptionPane.OK_CANCEL_OPTION,
//                       null, books.toArray(), books.get(0)); //displays list of all books as dropdown menu
//                for (Book product : this.listedProducts) {
//                    if (product.getName().equals(selection)) {
//                        //TODO: fix this. selection is no longer just the name of the book.
//                        Book viewing = product;
//                        System.out.println(viewing.getName());
//                        System.out.println("Store: " + viewing.getStore());
//                        System.out.println("Description: " + viewing.getDescription());
//                        System.out.println("Amount in Stock: " + viewing.getQuantity());
//                        System.out.println("$" + viewing.getPrice());
//
//                        //mini little menu within
//                        boolean looping;
//                        do {
//                            looping = false;
//                            System.out.println("1 - Purchase");
//                            System.out.println("2 - Add To Cart");
//                            System.out.println("3 - Cancel");
//                            String buy = scan.nextLine();
//                            boolean incorrectInput;
//                            if (buy.equals("1")) {
//                                do {
//                                    incorrectInput = false;
//                                    System.out.println("How much would you like to purchase? Type CANCEL to exit.");
//                                    String amt = scan.nextLine();
//                                    if (amt.equals("CANCEL")) {
//                                        return true;
//                                    } else if (!Util.isNumeric(amt)) {
//                                        System.out.println("Please input a number.");
//                                        incorrectInput = true;
//                                    } else if (Integer.parseInt(amt) > viewing.getQuantity()) {
//                                        System.out.println("Please input a valid quantity.");
//                                        incorrectInput = true;
//                                    } else {
//                                        System.out.println("Purchasing...");
//                                        viewing.removeQuantity(Integer.parseInt(amt));
//                                        //removes inputted amount from the product
//                                        this.updateListedProducts(product, viewing); //updates the list
//                                        ((Customer) user).addToPastPurchases(product, Integer.parseInt(amt));
//                                        ((Customer) user).exportToFile();
//                                        System.out.println("You have bought " + amt + " " + viewing.getName());
//                                        //add to purchase history as well
//                                        return true;
//                                    }
//                                } while (incorrectInput);
//                            } else if (buy.equals("2")) {
//                                System.out.println("How much of this item would you like to add to your cart?");
//                                String amt = scan.nextLine();
//                                if (!Util.isNumeric(amt)) {
//                                    // throw new InvalidInputError();
//                                    System.out.println("Please input a number.");
//                                } else if (Integer.parseInt(amt) > viewing.getQuantity() || Integer.parseInt(amt) <= 0) {
//                                    // throw new InvalidQuantityError();
//                                    System.out.println("Please input a valid quantity to add to cart.");
//                                } else {
//                                    System.out.println(Integer.parseInt(amt));
//                                    ((Customer) user).addToCart(product, Integer.parseInt(amt));
//                                    ((Customer) user).exportToFile();
//                                    //add this quantity to cart
//                                    //added to customer cart
//                                    System.out.println("Successfully added to cart!");
//                                    System.out.println("Returning to market...");
//                                }
//                                return true;
//                            } else if (buy.equals("3")) {
//                                looping = false;
//                            } else {
//                                System.out.println("Please select a valid option.");
//                                looping = true;
//                            }
//                        } while (looping);
//                    }
//                }
//                System.out.println("No matching books found.");
//            } else if (choice.equals(options[1])) {
//                System.out.println("What would you like to search for?");
//                String search = scan.nextLine();
//                ArrayList<Book> results = this.searchProducts(search);
//                if (results.isEmpty()) {
//                    System.out.println("No matching products found.");
//                } else {
//                    System.out.println("Your Search Results: ");
//                    for (Book book : results) {
//                        book.displayProduct();
//                    }
//                }
//                repeat = true;
//            } else if (choice.equals("3")) {
//                System.out.println("Exiting market...");
//                return false;
//            }
//        } while (repeat);
        return true;
    }

    /**
     *
     * @param input to search for
     * @return Searches for all books that match a given input, and returns a list.
     */
    public ArrayList<Book> searchProducts(String input) {
        ArrayList<Book> matches = new ArrayList<>();
        input = input.toUpperCase();
        //loop through listedProducts
        for (Book book : this.listedProducts) {
            String name = book.getName().toUpperCase();
            String author = book.getAuthor().toUpperCase();
            String genre = book.getGenre().name().toUpperCase();
            String storeName = book.getStore().toUpperCase();
            String description = book.getDescription().toUpperCase();
            if (name.contains(input)) {
                matches.add(book);
            } else if (author.contains(input)) {
                matches.add(book);
            } else if (genre.contains(input)) {
                matches.add(book);
            } else if (storeName.contains(input)) {
                matches.add(book);
            } else if (description.contains(input)) {
                matches.add(book);
            }
        }
        return matches;
    }

    /**
     * Displays menu for users editing their accounts.
     * @param username username
     * @param email email
     * @param password password
     * @return true/false
     * @throws IOException if file not found
     */
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
                boolean delete = Util.yesNo("Are you sure?", "Logout");
                if (!delete) {
                    System.out.println("OK! Returning to menu...");
                    repeat = false;
                } else {
                    User.deleteAccount(username, email, password);
                    System.out.println("Account successfully deleted!");
                    System.out.println("Logging you out...");
                    return false;
                }
            } else if (input.equals("5")) {
                System.out.println("Exiting...");
            }
        } while (repeat);
        return false;
    }

    /**
     * Displays menu for customers viewing their shopping carts.
     * @param user user to search shopping cart for
     * @throws IOException
     */
    public void viewCartMenu(Customer user) throws IOException {
        //read cart of this user - method in Customer?
        JButton checkoutButton = new JButton("Checkout");
        JButton returnButton = new JButton("Return");
        JFrame cartMenu = new JFrame();
        JPanel buttonPane = new JPanel();

        buttonPane.add(returnButton);
        buttonPane.add(checkoutButton);
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == checkoutButton) {
                    //getText from searchText
                }
                if (e.getSource() == returnButton) {
                    cartMenu.setVisible(false);
                }
            }
        };
        returnButton.addActionListener(al);
        checkoutButton.addActionListener(al);

        JPanel cartPane = new JPanel();
        JScrollPane itemsPane = new JScrollPane();
        ArrayList<Book> shoppingCart = new ArrayList<>(); //TODO: put in shopping cart info.
        JList itemList = new JList((ListModel) shoppingCart);
        itemsPane.add(itemList);

        ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //TODO
            }
        };

        cartPane.add(itemsPane);

        Scanner scan = new Scanner(System.in);
        user.viewCart();
        System.out.println("1 - Purchase an Item from Cart");
        System.out.println("2 - Remove an Item From Cart");
        System.out.println("3 - Purchase All Items in Cart");
        System.out.println("4 - Return to Main Menu");
        String input = scan.nextLine();
        boolean loop;
        do {
            loop = false;
            if (input.equals("1")) { //purchase item
                System.out.println("Which item would you like to purchase?");
                String item = scan.nextLine();
                boolean incorrectInput;
                do {
                    incorrectInput = false;
                    System.out.println("How much of this item would you like to buy?");
                    String quantity = scan.nextLine();
                    if (!Util.isNumeric(quantity)) {
                        System.out.println("Please input a valid quantity.");
                        incorrectInput = true;
                    } else if (Integer.parseInt(quantity) <= 0) {
                        System.out.println("Please input a valid quantity.");
                        incorrectInput = true;
                    } else {
                        //actually buy thing. add to past purchases, remove from cart.
                        //loop through cart?
                        incorrectInput = user.removeFromCart(item, Integer.parseInt(quantity));
                        if (incorrectInput) {
                            System.out.println("Successfully purchased!");
                        }
                    }
                    loop = true;
                } while (incorrectInput);
            } else if (input.equals("2")) {
                System.out.println("Which book would you like to remove?"); //multiple items having same name will have
                // problems
                String item = scan.nextLine();
                ArrayList placeholder = new ArrayList<>();
                for (Book book : user.getCart()) {
                    if (!book.getName().equals(item)) {
                        placeholder.add(book);
                    }
                }
                user.setCart(placeholder);
                user.exportToFile();
                System.out.println("Removed " + item + " from cart.");
                loop = true;
            } else if (input.equals("3")) {
                ArrayList<Book> purchasedCart = user.purchaseShoppingCart();
                ArrayList<Book> placeholder = this.listedProducts;
                ArrayList<Book> failed = new ArrayList<Book>();
                ArrayList<Book> succeeded = new ArrayList<Book>();
                for (Book book : placeholder) {
                    for (Book bk : purchasedCart) {
                        if (book.equals(bk)) {
                            if (bk.getQuantity() > book.getQuantity()) {
                                //if the amount you're trying to buy is greater than the amount that exists
                                failed.add(bk);
                            } else {
                                this.updateListedProducts(book, bk);
                                succeeded.add(bk);
                            }
                        }
                    }
                    System.out.println("You bought: ");
                    for (Book bought : succeeded) {
                        bought.displayProductInfo();
                        user.addToPastPurchases(bought, bought.getQuantity());
                    }
                    if (!failed.isEmpty()) {
                        System.out.println("Failed to purchase: ");
                        for (Book fail : failed) {
                            fail.displayProductInfo();
                        }
                        user.setCart(failed);
                    }
                }
                user.exportToFile();
                System.out.println("Exiting...");
                loop = false;
            } else if (input.equals("4")) {
                System.out.println("Exiting cart...");
                loop = false;
            } else {
                System.out.println("Please input a valid option.");
                loop = true;
            }
        } while(loop);
}

    /**
     * Displays menu to give seller options to add, edit, or delete their books
     * @param user
     * @throws IOException
     */
    public void editProductsMenu(Seller user) throws IOException {
        boolean loop;
        Scanner scan = new Scanner(System.in);
        do {
            loop = false;
            System.out.println("1 - Add a New Product");
            System.out.println("2 - Edit an Existing Product");
            System.out.println("3 - Delete a Product");
            System.out.println("4 - Return");
            String input = scan.nextLine();
            Book newBook;
            //NOTE: updates every time
            if (input.equals("1")) {
                Book product = Book.createBookFromUserInput("");
                user.addProduct(product);
                user.addToStore(product.getStore(), product);
                this.listedProducts.add(product);
                System.out.println("You have added " + product.getName() + " to " + product.getStore());
                user.exportToFile();
                loop = true;
            } else if (input.equals("2")) {
                boolean repeat;
                do {
                    repeat = false;
                    System.out.println("What product would you like to edit?");
                    boolean exist = false;
                    String inputProduct = scan.nextLine();
                    ArrayList<Book> placeholder = user.getProducts();
                    for (Book product : user.getProducts()) { //list of all products
                        Book saver = product;
                        if (product.getName().equals(inputProduct)) { //if matches the input
                            //mini edit menu
                             newBook = this.updateProductMenu(saver); //updateProductMenu returns a new book object
                             placeholder.remove(saver); //remove old product
                             placeholder.add(newBook); //adds to placeholder list.
                             exist = true;
                        }
                    }
                    if (!exist) {
                        repeat = Util.yesNo("Book not found. Try again?", "Book not found");
                    } else {
                        user.addProducts(placeholder);
                        user.exportToFile();
                    }
                } while(repeat);
                user.exportToFile();
                loop = true;
            } else if (input.equals("3")) {
                System.out.println("What product would you like to delete?");
                String productName = scan.nextLine();
                ArrayList<Book> placeholder = user.getProducts();
                for (Book product : user.getProducts()) {
                    if (product.getName().equals(productName)) {
                        placeholder.remove(product);
                    }
                }
                user.setProducts(placeholder);
                user.exportToFile();
                loop = true;
            } else if (input.equals("4")) {
                System.out.println("Returning to main menu...");
                user.exportToFile();
                break;
            } else {
                System.out.println("Please select a valid option.");
                loop = true;
            }
        } while (loop);
    }

    /**
     * Displays menu for sellers updating an existing book.
     * @param book
     * @return
     */
    private Book updateProductMenu(Book book) {
        Scanner scan = new Scanner(System.in);
        System.out.println("1 - Proceed");
        System.out.println("2 - CANCEL and Exit");
        String input = scan.nextLine();
        do {
            if (input.equals("1")) {
                System.out.println("Input the old parameters if you would like something to be unchanged.");

                System.out.println("Current name is \"" + book.getName() + "\". What would you like the new name to be?");
                book.setName(scan.nextLine());

                System.out.println("Current author is \"" + book.getAuthor() + "\". What would you like the new author to be?");
                book.setAuthor(scan.nextLine());

                System.out.println("Current genre is \"" + book.getGenre() + "\". What would you like the new genre to " +
                        "be?");
                book.setGenre(scan.nextLine());
                System.out.println("Current description is \"" + book.getDescription() + "\". What would you like the new" +
                        " " +
                        "description to be?");
                book.setDescription(scan.nextLine());

                System.out.println("Current store is \"" + book.getStore() + "\". What would you like the new store to be?");
                book.setStore(scan.nextLine());

                System.out.println("Current quantity is \"" + book.getQuantity() + "\". What would you like the new " +
                        "quantity to be?");
                book.setQuantity(scan.nextInt());
                scan.nextLine();
                System.out.println("Current price is \"" + book.getPrice() + "\". What would you like the new price to " +
                        "be?");
                book.setPrice(scan.nextDouble());
                return book;
            } else if (input.equals("2")) {
                return book;
            } else {
                System.out.println("Please select a valid option.");
            }
        } while (true);
    }

    /**
     * Customer - displays past purchases, and gives an option to export as a file.
     * @param user
     * @throws IOException if file not found
     */
    public void pastPurchasesMenu(User user) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("1 - Export as File");
        System.out.println("2 - Return to Main Menu");
        String input = scan.nextLine();
        if (input.equals("1")) {
            System.out.println("Input the filename: ");
            String filename = scan.nextLine();
            if (!filename.contains(".csv")) {
                filename += ".csv";
            }
            ((Customer) user).exportPastPurchases(filename);
            System.out.println("Exported successfully to " + filename + "!");
        } else if (input.equals("2")) {
            System.out.println("Exiting...");
        } else {
            System.out.println("Please select a valid option.");
        }
    }

    /**
     * Seller - displays sales by store.
     */
    public void viewSalesByStore() {
        //use boughtProducts list.
        ArrayList<Store> stores = ((Seller) user).getStore();
        ArrayList<Book> sold = new ArrayList<>();
        boolean unique = true;
        for (Store store: stores) {
            for (Book book : boughtProducts) { //iterates through all bought products
                if (book.getStore().equals(store.getName())) { //if the store name of the book matches the display store
                    for (Book check : sold) { //iterates through sold; checks if it's an already existing item added in
                        if (check.equals(book)) {
                            unique = false; //book is not unique
                            check.addQuantity(book.getQuantity());
                            sold.add(check);
                        }
                    }
                    if (unique) {
                        sold.add(book);
                    }
                }

            }
            store.setProducts(sold);
            store.displayData();
        }
    }

    public void viewSellerProducts() {
            BookPanel sellerProducts = new BookPanel(this.getListedProducts());
            JFrame productsFrame = new JFrame();
            productsFrame.setLayout(new BorderLayout());
            productsFrame.setSize(new Dimension(500, 300));
            productsFrame.setLocationRelativeTo(null);
            JPanel productsPanel = sellerProducts.getBookPanel();
            productsFrame.add(productsPanel, BorderLayout.CENTER);

            JPanel topPane = new JPanel();
            JPanel bottomPane = new JPanel();

            JButton returnButton = new JButton("Return");
            JButton editButton = new JButton("Edit Selection");
            JButton exportButton = new JButton("Export to File");
            JButton addButton = new JButton("Add New Book");
            JButton deleteButton = new JButton("Delete Selection");

            topPane.add(returnButton);
            topPane.add(exportButton);

            bottomPane.add(addButton);
            bottomPane.add(editButton);
            bottomPane.add(deleteButton);
            productsFrame.add(topPane, BorderLayout.NORTH);
            productsFrame.add(bottomPane, BorderLayout.SOUTH);
            final String[] input = new String[1];
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == returnButton) {
                        productsFrame.setVisible(false);
                    }
                    if (e.getSource() == editButton) {
                        String[] options = new String[]{"Title", "Author", "Genre", "Description", "Store", "Quantity",
                                "Price"}; //return what was changed eventually
                        String selection = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                                "Edit " + sellerProducts.selectedBook().getName(),
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        String newInput = "";
                        if (selection.equals(options[0])) {
                            newInput = JOptionPane.showInputDialog("What would you like the new title to be?");
                        } else if (selection.equals(options[1])) {
                            newInput = JOptionPane.showInputDialog("What would you like the new author to be?");
                        } else if (selection.equals(options[2])) {
                            newInput = JOptionPane.showInputDialog("What would you like the new genre to be?");
                        } else if (selection.equals(options[3])) {
                            newInput = JOptionPane.showInputDialog("What would you like the new description to be?");
                        }
                    }
                    if (e.getSource() == exportButton) {
                        //write to server to export to file
                    }
                }
            };
            editButton.addActionListener(al);
            returnButton.addActionListener(al);
            productsFrame.setVisible(true);
    }
}

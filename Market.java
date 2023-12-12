import javax.swing.*;
import javax.swing.border.Border;
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

        return new String[]{username, email, password, account}; //TODO: move all file reading into serverthread
    }

    /**
     * Creates a new account.
     * @return a String[] containing username, email, password, and account type.
     * @throws IOException if logins file does not exist
     */
    public static String[] userInitialization() throws IOException {

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();
        String username;
        String email;
        String password;
        String account;
        String[] accountOptions = {"CUSTOMER", "SELLER"};
        JComboBox<String> accountTypes = new JComboBox<String>(accountOptions);
        Object[] login = {"Username:", usernameField, "Email:", emailField,
                "Password:", passwordField, "Account Type:", accountTypes};

        boolean incorrectInput;
        do {
            //username
            incorrectInput = false;
            int option = JOptionPane.showConfirmDialog(null, login, "Create Account",
                    JOptionPane.OK_CANCEL_OPTION);
            username = usernameField.getText();
            email = emailField.getText();
            password = passwordField.getText();
            account = accountTypes.getSelectedItem().toString();
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return new String[]{"CANCEL"};
            } else if (username == null || email == null || password == null) {
                JOptionPane.showMessageDialog(null, "Please do not leave any fields empty.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                incorrectInput = true;
            } else if (username.contains(",") || (email.contains(",") || password.contains(","))) {
                JOptionPane.showMessageDialog(null, "Do not use commas in any field.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                incorrectInput = true;
            } else if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(null, "Please input a valid email.");
                incorrectInput = true;
            }
        } while (incorrectInput);
        return new String[]{username, email, password, account};

    }


    public ArrayList<Book> getListedProducts() {
        return this.listedProducts;
    }

    public void setListedProducts(ArrayList<Book> newList) {
        this.listedProducts = newList;
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
    public String displayCustomerMenu() throws IOException, InterruptedException {
        JFrame cMenuFrame = new JFrame();
        cMenuFrame.setSize(500, 350);
        cMenuFrame.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());

        JButton searchButton = new JButton("Search");
        JTextField searchField = new JTextField(10);
        JButton returnButton = new JButton("Return");

        JPanel topPanel = new JPanel();
        topPanel.add(returnButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);


        cMenuFrame.add(topPanel, BorderLayout.NORTH);
        BookPanel bookDisplay = new BookPanel(this.getListedProducts());
        JPanel bookPanel = bookDisplay.getBookPanel(); //calls method to set up panel for books
        JButton cartButton = new JButton("Add to Cart");
        JButton buyButton = new JButton("Buy");

        bookPanel.add(cartButton);
        bookPanel.add(buyButton);
        cMenuFrame.add(bookPanel, BorderLayout.CENTER); //TODO: fix layout.
        String[] option = new String[1];
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == returnButton) {
                    option[0] = "RETURN";
                    cMenuFrame.setVisible(false);
                }
                if (e.getSource() == searchButton) {
                        //TODO: getText from searchText
                    String searchText = searchField.getText();
                    ArrayList<Book> matches = searchProducts(searchText);
                    String matchString = "";
                    for (Book book : matches) {
                        matchString += book.toCSVFormat() + ",";
                    }
                    cMenuFrame.setVisible(false);
                    option[0] = "SEARCH" + "," + searchText + "," + matchString;
                }
                if (e.getSource() == buyButton) {
                    boolean invalidInput = false;
                    String amount;
                    Book selection = bookDisplay.selectedBook();
                    if (selection != null) {
                        do {
                            amount = JOptionPane.showInputDialog("How much would you like to buy?");
                            if (amount == null) {
                                break;
                            } else if (!Util.isNumeric(amount)) {
                                JOptionPane.showMessageDialog(null, "Please input a numeric value", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                invalidInput = true;
                            } else {
                                option[0] = "BUY," + amount + "," + selection.toCSVFormat();
                                cMenuFrame.removeAll();
                                cMenuFrame.setVisible(false);
                            }
                        } while (invalidInput);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an item to purchase");
                        option[0] = "";
                    }
                }
                if (e.getSource() == cartButton) {
                    Book selection = bookDisplay.selectedBook();
                    String amount = "";
                    if (selection != null) {
                        amount = JOptionPane.showInputDialog(null, "How much would you like to add to your " +
                                "cart?");
                        if (!Util.isNumeric(amount) || Integer.parseInt(amount) < 0 || Integer.parseInt(amount) > selection.getQuantity()) {
                            JOptionPane.showMessageDialog(null, "Please input a valid quantity to purchase.");
                        } else {
                            System.out.println("Adding " + amount + " to cart.");
                            option[0]= "ADD TO CART," + amount + "," + selection.toCSVFormat();
                            cMenuFrame.setVisible(false);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an item from the list.");
                    }
                }
            }
        };

        searchButton.addActionListener(al);
        buyButton.addActionListener(al);
        cartButton.addActionListener(al);
        returnButton.addActionListener(al);

        cMenuFrame.setVisible(true);
        while (option[0] == null || option[0].isEmpty()) {
            System.out.println();
        }
        return option[0];
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
    public static String searchFrame (String keyword, ArrayList<Book> matches) {
        JFrame searchFrame = new JFrame();
        JButton searchResults = new JButton("Search Results from " + keyword);

        JPanel topPanel = new JPanel();
        topPanel.add(searchResults);


        searchFrame.add(topPanel, BorderLayout.NORTH);
        BookPanel bookDisplay = new BookPanel(matches);
        JPanel bookPanel = bookDisplay.getBookPanel(); //calls method to set up panel for books
        JButton cartButton = new JButton("Add to Cart");
        JButton buyButton = new JButton("Buy");
        JButton returnButton = new JButton("Return");

        bookPanel.add(cartButton);
        bookPanel.add(buyButton);
        bookPanel.add(returnButton);
        searchFrame.add(bookPanel, BorderLayout.CENTER); //TODO: fix layout.
        String[] option = new String[1];
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == returnButton) {
                    //TODO: getText from searchText
                    option[0] = "RETURN";
                    searchFrame.setVisible(false);
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

        buyButton.addActionListener(al);
        cartButton.addActionListener(al);
        returnButton.addActionListener(al);
        searchFrame.setSize(new Dimension(500, 350));
        searchFrame.setVisible(true);
        searchFrame.setLocation(new Point(1000, 1000));

        while(option[0] == null || option[0].isEmpty()) {
            System.out.println((""));
        }

        return option[0];
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
    public String viewCartMenu(Customer user) throws IOException {
        ArrayList<Book> cartList = ((Customer) user).getCart();
        if (cartList == null) {
            cartList = new ArrayList<Book>();
        }
        JFrame cartFrame = new JFrame();
        cartFrame.setSize(new Dimension(500, 400));
        BookPanel holder = new BookPanel(cartList);
        JPanel cartPanel = holder.getBookPanel();

        JPanel buttonPanel = new JPanel();
        JButton allButton = new JButton("Purchase All");
        JButton oneButton = new JButton("Purchase Selection");
        JButton returnButton = new JButton("Return");
        buttonPanel.add(allButton);
        buttonPanel.add(oneButton);
        String[] option = new String[1];

        JPanel topPane = new JPanel();
        topPane.add(new JLabel("Your Cart"), BorderLayout.NORTH);
        topPane.add(returnButton);

        cartFrame.add(cartPanel, BorderLayout.CENTER);
        cartFrame.add(buttonPanel, BorderLayout.SOUTH);
        cartFrame.add(topPane, BorderLayout.NORTH);
        cartFrame.setSize(new Dimension(500, 300));
        //read cart of this user - method in Customer?

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == allButton) {
                    //buy all.
                    option[0] = "ALL";
                    cartFrame.setVisible(false);
                }
                if (e.getSource() == oneButton) {
                    if (holder.selectedBook() == null) {
                        JOptionPane.showMessageDialog(null, "Please select a book first.");
                    } else {
                        //purchases amount in cart automatically
                        option[0] = "ONE," + holder.selectedBook().getQuantity() + "," + holder.selectedBook().toCSVFormat();
                        cartFrame.setVisible(false);
                    }
                }
                if (e.getSource() == returnButton) {
                    cartFrame.setVisible(false);
                    option[0] = "RETURN";
                }
            }
        };
        returnButton.addActionListener(al);
        allButton.addActionListener(al);
        oneButton.addActionListener(al);
        cartFrame.setVisible(true);

        while (option[0] == null || option[0].isEmpty()) {
            System.out.println("");
        }
        return option[0];
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
    public String pastPurchasesMenu(User user) throws IOException {
        JFrame pastFrame = new JFrame();
        pastFrame.setSize(new Dimension(500, 300));
        JPanel topPanel = new JPanel();
        JButton returnButton = new JButton("Return");
        JButton exportButton = new JButton("Export as File");
        BookPanel purchasesPanel = new BookPanel(((Customer) user).getPastPurchases());
        ArrayList<Book> pastPurchases = ((Customer) user).getPastPurchases();
        pastFrame.add(topPanel, BorderLayout.NORTH);
        pastFrame.add(purchasesPanel.getBookPanel(), BorderLayout.CENTER);
        String[] result = new String[1];
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == returnButton) {
                    result[0] = "RETURN";
                    pastFrame.setVisible(false);
                }
                if (e.getSource() == exportButton) {
                    boolean invalidInput = false;
                    do {
                        String file = JOptionPane.showInputDialog("Input a filename: ");
                        File f = new File(file);
                        try {
                            if (f.createNewFile()) {
                                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                                for (Book book : pastPurchases) {
                                    bw.write(book.toCSVFormat() + "\n");
                                    bw.flush();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "That's already an existing file!");
                                invalidInput = true;
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Whoops! Something went wrong.");
                            invalidInput = true;
                        }
                    } while(invalidInput);
                }
            }
        };
        returnButton.addActionListener(al);
        exportButton.addActionListener(al);
        topPanel.add(returnButton);
        topPanel.add(exportButton);
        pastFrame.setVisible(true);
        while (result[0] == null || result[0].isEmpty()) {
            System.out.println("");
        }
        return result[0];
    }

    /**
     * Seller - displays sales by store.
     */
    public void viewSalesByStore() {
        //use boughtProducts list.
        ArrayList<String> stores = ((Seller) user).getStoreNames();
        ArrayList<Book> sold = new ArrayList<>();
        boolean unique = true;
        for (String store: stores) {
            for (Book book : boughtProducts) { //iterates through all bought products
                if (book.getStore().equals(store)) { //if the store name of the book matches the display store
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
        }
    }

    public String viewSellerProducts(Seller seller) {
            BookPanel sellerProducts = new BookPanel(seller.getProducts());
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
            String[] input = new String[1];
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == returnButton) {
                        productsFrame.setVisible(false);
                        input[0] = "RETURN";
                    }
                    if (e.getSource() == editButton) {
                        boolean unsatisfied = false;
                        Book selection = sellerProducts.selectedBook();
                        Book placeholder = selection;
                        if (selection != null) {
                            do {
                                unsatisfied = false;
                                String[] options = new String[]{"Title", "Author", "Genre", "Description",
                                        "Store",
                                        "Quantity",
                                        "Price"}; //return what was changed eventually
                                String toChange = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                                        "Edit " + sellerProducts.selectedBook().getName(),
                                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                String newInput = "";
                                if (toChange.equals(options[0])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new title to be?");
                                    placeholder.setName(newInput);
                                } else if (toChange.equals(options[1])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new author to be?");
                                    placeholder.setAuthor(newInput);
                                } else if (toChange.equals(options[2])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new genre to be?");
                                    placeholder.setGenre(newInput);
                                } else if (toChange.equals(options[3])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new description to be?");
                                    placeholder.setDescription(newInput);
                                } else if (toChange.equals(options[4])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new store to be?");
                                    placeholder.setStore(newInput);
                                } else if (toChange.equals(options[5])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new quantity to be?");
                                    placeholder.setQuantity(Integer.parseInt(newInput));
                                } else if (toChange.equals(options[6])) {
                                    newInput = JOptionPane.showInputDialog("What would you like the new price to be?");
                                    placeholder.setPrice(Double.parseDouble(newInput));
                                }
                                int confirmation = JOptionPane.showConfirmDialog(null,
                                        "Title: " + placeholder.getName() + "\n"
                                                + "Author: " + placeholder.getAuthor() + "\n" +
                                                "Genre: " + placeholder.getGenre() + "\n" +
                                                "Store: " + placeholder.getStore() + "\n" +
                                                "Quantity: " + placeholder.getQuantity() + "\n" +
                                                "Price: " + placeholder.getStore() + "\n" +
                                                "Is this what you want?", null,
                                        JOptionPane.YES_NO_OPTION);
                                if (confirmation == JOptionPane.YES_OPTION) {
                                    unsatisfied = false;
                                } else {
                                    //repeat
                                    unsatisfied = true;
                                }
                            } while(unsatisfied);
                            input[0] = "EDIT," + selection.toCSVFormat() + "," + placeholder.toCSVFormat();
                            productsFrame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select an item to edit.");
                        }
                    }
                    if (e.getSource() == addButton) {
                        Book newBook = Book.createBookFromUserInput("");
                        input[0] = "ADD PRODUCT," + newBook.toCSVFormat();
                        productsFrame.setVisible(false);
                    }
                    if (e.getSource() == exportButton) {
                        //write to server to export to file
                        input[0] = "EXPORT";
                    }
                }
            };
            editButton.addActionListener(al);
            returnButton.addActionListener(al);
            addButton.addActionListener(al);
            productsFrame.setVisible(true);
            while (input[0] == null || input[0].isEmpty()) {
                System.out.println("");
            }
            return input[0];
    }
}

import java.io.*;
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
                ArrayList<Book> bookList = Util.readCSV(name + ".csv"); //reads all products from
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
                    ArrayList<Book> purchases = Util.readCSV(line); //one long line. assume no repeats of products.
                    this.boughtProducts.addAll(purchases);
                }
            }
        }
    }

    public ArrayList<Book> getListedProducts() {
        return this.listedProducts;
    }

    public void updateListedProducts(Book oldProduct, Book updatedProduct) {
        ArrayList<Book> iterator = new ArrayList<>();
        for (Book book : this.listedProducts) {
            if (!book.equals(oldProduct)) {
                iterator.add(book); //adds everything not the original product
            }
        }
        iterator.add(updatedProduct); //adds updated product to list
        this.listedProducts = iterator; //sets listedProducts to list with new item
    }

    public void exportToFiles() throws IOException { //update seller's list of products
        for (Book book : this.listedProducts) {
            for (String seller : sellerNames) { //for every seller
                String file = seller + ".csv";
                ArrayList<Book> sellerBooks = new ArrayList<>(); //arraylist of all books in one seller
                for (Book bk : Util.readCSV(file)) { //if book matches title and author. this is mostly for customer's
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

    public static void userInitialization() throws IOException {
        Scanner scan = new Scanner(System.in);
        String username = "";
        String email = "";
        String password = "";

        boolean incorrectInput;
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
    }


    public ArrayList<Book> displayMarket() {
        //iterate through listedProducts
        for (Book book : this.listedProducts) {
            book.displayProduct(); //is it an issue with this
        }
        return this.listedProducts;
    }

    public boolean displayProductsMenu() throws IOException {
        Scanner scan = new Scanner(System.in);
        boolean repeat;
        do {
            repeat = false;
            System.out.println("1 - Select a Book You are Interested in");
            System.out.println("2 - Search for a Book by Keyword");
            System.out.println("3 - Return to the Main Menu");
            String option = scan.nextLine();
            if (option.equals("1")) {
                System.out.println("Input the product name: ");
                String selection = scan.nextLine();
                for (Book product : this.listedProducts) {
                    if (product.getName().equals(selection)) { //if the name of the product matches
                        Book viewing = product;
                        System.out.println(viewing.getName());
                        System.out.println("Store: " + viewing.getStore());
                        System.out.println("Description: " + viewing.getDescription());
                        System.out.println("Amount in Stock: " + viewing.getQuantity());
                        System.out.println("$" + viewing.getPrice());

                        //mini little menu within
                        boolean looping;
                        do {
                            looping = false;
                            System.out.println("1 - Purchase");
                            System.out.println("2 - Add To Cart");
                            System.out.println("3 - Cancel");
                            String buy = scan.nextLine();
                            boolean incorrectInput;
                            if (buy.equals("1")) {
                                do {
                                    incorrectInput = false;
                                    System.out.println("How much would you like to purchase? Type CANCEL to exit.");
                                    String amt = scan.nextLine();
                                    if (amt.equals("CANCEL")) {
                                        return true;
                                    } else if (!Util.isNumeric(amt)) {
                                        System.out.println("Please input a number.");
                                        incorrectInput = true;
                                    } else if (Integer.parseInt(amt) > viewing.getQuantity()) {
                                        System.out.println("Please input a valid quantity.");
                                        incorrectInput = true;
                                    } else {
                                        System.out.println("Purchasing...");
                                        viewing.removeQuantity(Integer.parseInt(amt));
                                        //removes inputted amount from the product
                                        this.updateListedProducts(product, viewing); //updates the list
                                        ((Customer) user).addToPastPurchases(product, Integer.parseInt(amt));
                                        ((Customer) user).exportToFile();
                                        System.out.println("You have bought " + amt + " " + viewing.getName());
                                        //add to purchase history as well
                                        return true;
                                    }
                                } while (incorrectInput);
                            } else if (buy.equals("2")) {
                                System.out.println("How much of this item would you like to add to your cart?");
                                String amt = scan.nextLine();
                                if (!Util.isNumeric(amt)) {
                                    System.out.println("Please input a number.");
                                } else if (Integer.parseInt(amt) > viewing.getQuantity() || Integer.parseInt(amt) <= 0) {
                                    System.out.println("Please input a valid quantity to add to cart.");
                                } else {
                                    System.out.println(Integer.parseInt(amt));
                                    ((Customer) user).addToCart(product, Integer.parseInt(amt));
                                    ((Customer) user).exportToFile();
                                    //add this quantity to cart
                                    //added to customer cart
                                    System.out.println("Successfully added to cart!");
                                    System.out.println("Returning to market...");
                                }
                                return true;
                            } else if (buy.equals("3")) {
                                looping = false;
                            } else {
                                System.out.println("Please select a valid option.");
                                looping = true;
                            }
                        } while (looping);
                    }
                }
                System.out.println("No matching books found.");
            } else if (option.equals("2")) {
                System.out.println("What would you like to search for?");
                String search = scan.nextLine();
                ArrayList<Book> results = this.searchProducts(search);
                if (results.isEmpty()) {
                    System.out.println("No matching products found.");
                } else {
                    System.out.println("Your Search Results: ");
                    for (Book book : results) {
                        book.displayProduct();
                    }
                }
                repeat = true;
            } else if (option.equals("3")) {
                System.out.println("Exiting market...");
                return false;
            }
        } while (repeat);
        return true;
    }

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
                boolean delete = Util.yesNo();
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
                repeat = false;
            }
        } while (repeat);
        return true;
    }

    public void viewCartMenu(Customer user) throws IOException {
        //read cart of this user - method in Customer?
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

    //menu to give seller options to add, edit, or delete their books
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
                Book product = Book.createBookFromUserInput();
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
                        System.out.println("Book not found. Try again?");
                        repeat = Util.yesNo();
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

    // the menu for changing an existing product
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
}

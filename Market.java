import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/*
 * Useful functions for displaying the marketplace.
 */
public class Market {
    private ArrayList<Book> listedProducts;
    private ArrayList<Book> boughtProducts;
    private ArrayList<String> sellerNames;
    private User user;

    public Market(User user) throws IOException {
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        this.sellerNames = new ArrayList<>();
        this.listedProducts = new ArrayList<>();
        this.user = user;
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (User.accountType(loginDetails[0]).equals("SELLER")) {
                this.sellerNames.add(loginDetails[0]); //adds username
            }
            line = bfr.readLine();
        }
        bfr.close();

        this.listedProducts = new ArrayList<>();
        for (String name : sellerNames) {
            File f = new File(name + ".csv");
            if (f.exists()) {
                ArrayList<Book> bookList = Util.readCSV(user.getUsername() + ".csv");
                this.listedProducts.addAll(bookList);
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
                iterator.add(book);
            }
        }
        iterator.add(updatedProduct); //adds updated product
        this.listedProducts = iterator;
    }

    public void exportToFiles() {

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
                String user = scan.nextLine();
                if (User.isExistingUser(username)) {
                    System.out.println("That username is taken already. Try again, or type CANCEL to exit.");
                    String cancel = scan.nextLine();
                    if (cancel.equals("CANCEL")) {
                        return;
                    }
                    incorrectInput = true;
                }
                username = user;
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


    public ArrayList<Book> listProducts() {
        //iterate through listedProducts
        for (int i = 0; i < this.listedProducts.size(); i++) {
            Book product = this.listedProducts.get(i);
            String name = product.getName();
            ;
            String storeName = product.getStore();
            double price = product.getPrice();
            System.out.println(name + " - " + storeName + " - $" + price);
        }
        return this.listedProducts;
    }

    public boolean displayProductsMenu() {
        Scanner scan = new Scanner(System.in);
        boolean repeat;
        do {
            repeat = false;
            System.out.println("1 - Select a Book You are Interested in");
            System.out.println("2 - Search for a Book by Keyword");
            System.out.println("3 - Return to the Main Menu");
            String option = scan.nextLine();
            if (option.equals("1")) {
                System.out.println("Input the product name:");
                String selection = scan.nextLine();
                for (Book product : this.listedProducts) {
                    if (product.getName().equals(selection)) {
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
                                    System.out.println("How much would you like to purchase? Type CANCEL at any " +
                                            "point to exit.");
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
                                        this.updateListedProducts(product, viewing); //old, new
                                        ((Customer) user).addToPastPurchases(product, Integer.parseInt(amt));
                                        System.out.println("You have bought " + amt + " " + viewing.getName());
                                        //add to purchase history as well
                                        return true;
                                    }
                                } while(incorrectInput);
                            } else if (buy.equals("2")) {
                                    System.out.println("How much of this item would you like to add to your cart?");
                                    String amt = scan.nextLine();
                                    if (!Util.isNumeric(amt)) {
                                        System.out.println("Please input a number.");
                                    } else if (Integer.parseInt(amt) > viewing.getQuantity()) {
                                        System.out.println("Please input a valid quantity to add to cart.");
                                    } else {
                                        System.out.println(Integer.parseInt(amt));
                                        ((Customer) user).addToCart(product, Integer.parseInt(amt));
                                        //add this quantity to cart
                                        //added to customer cart
                                        System.out.println("Successfully added to cart!");
                                        System.out.println("Returning to market...");
                                    }
                                    return true;
                                } else if (buy.equals("3")) {
                                    looping = false;
                                    break;
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
        } while(repeat);
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
                String sure = scan.nextLine();
                if (!Util.yesNo(sure)) {
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
    public void viewCartMenu(Customer user) {
        //read cart of this user - method in Customer?
        Scanner scan = new Scanner(System.in);
        user.viewCart();
        System.out.println("1 - Purchase an Item from Cart");
        System.out.println("2 - Remove an Item From Cart");
        System.out.println("3 - Purchase All Items in Cart");
        System.out.println("4 - Return to Main Menu");
        String input = scan.nextLine();

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
            } while(incorrectInput);
        } else if (input.equals("2")) {

        } else if (input.equals("3")) {

        } else if (input.equals("4")) {
            System.out.println("Exiting cart...");
        } else {
            System.out.println("Please input a valid option.");
        }
    }

    public void editProductsMenu(Seller user) {
        boolean loop;
        Scanner scan = new Scanner(System.in);
        do {
            loop = false;
            System.out.println("1 - Add a New Product");
            System.out.println("2 - Edit an Existing Product");
            System.out.println("3 - Delete a Product");
            System.out.println("4 - Return");
            String input = scan.nextLine();

            if (input.equals("1")) {
                System.out.println("What store would you like to add to?");
                String storeName = scan.nextLine();
                Book product = Book.createBookFromUserInput();

                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("2")) {
                System.out.println("What product would you like to edit?");
                String productName = scan.nextLine();
                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("3")) {
                System.out.println("What product would you like to edit?");
                String productName = scan.nextLine();
                //search for product with matching name
                System.out.println("Sorry! This function isn't currently implemented.");
                loop = true;
            } else if (input.equals("4")) {
                System.out.println("Returning to main menu...");
                break;
            } else {
                System.out.println("Please select a valid option.");
                loop = true;
            }
        } while (loop);
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
}

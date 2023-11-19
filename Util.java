/*
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Util {
    /**
     *
     * @param input
     * @return if input is a number or not
     * @throws NumberFormatException
     */
    public static boolean isNumeric(String input) throws NumberFormatException {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * @return Checks if input is yes or no, and returns a boolean based on that. Returns true if yes, returns false if
     * no.
     */
    public static boolean yesNo() {
        boolean repeat;
        boolean checker = false;
        do {
            System.out.println("Y/N: ");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            repeat = false;
            if (input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("Y")) {
                checker = true;
            } else if (input.equalsIgnoreCase("NO") || input.equalsIgnoreCase("N")) {
                checker = false;
            } else {
                System.out.println("This is a yes or no question.");
                repeat = true;
            }
        } while(repeat);
        return checker;
    }

    /**
     * @param email
     * @return Checks if input is a valid email or not.
     */
    public static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        } else if (email.contains(",")) {
            System.out.println("Commas are not allowed in emails.");
            return false;
        }
        return true;
    }

    /**
     *
     * @param email
     * @return Returns the username from an email.
     * @throws IOException
     */
    public static String getUserFromEmail(String email) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        while (line != null && !line.equals("")) {
            String[] loginDetails = line.split(",");
            if (email.equals(loginDetails[1])) {
                return loginDetails[0];
            }
            line = bfr.readLine();
        }
        return "Username not found.";
    }
    public static boolean isExistingStore(String storeName, ArrayList<Store> stores) {
        for (Store store : stores) {
            if (storeName.equals(store.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param input
     * @return Takes an input String, and if that string is a filename, reads from the file. If not, reads the string
     * into a book.
     * @throws IOException
     */
    public static ArrayList<Book> readCSV (String input) throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        if (input.contains(".csv")) { //if it's a filename
            BufferedReader bfr = new BufferedReader(new FileReader(input));
            String line = bfr.readLine();
            while (line != null && !line.isEmpty()) {
                String[] productDetails = line.split(",");
                if (productDetails.length != 7) {
                    System.out.println("Please input a properly formatted file.");
                } else {
                    for (int i = 0; i < productDetails.length; i += 7) {
                        String name = productDetails[i];
                        String author = productDetails[i + 1];
                        String genre = productDetails[i + 2];
                        String description = productDetails[i + 3];
                        String storeName = productDetails[i + 4];
                        int quantity = Integer.parseInt(productDetails[i + 5]);
                        double price = Double.parseDouble(productDetails[i + 6]);
                        books.add(new Book(name, author, genre, description, storeName, quantity, price));
                    }
                }
                line = bfr.readLine();
            }
        } else { //if it's not a filename
            if (!input.isEmpty()) {
                String[] productDetails = input.split(",");
                if (productDetails.length < 7) {
                    System.out.println("Please input a properly formatted file.");
                } else {
                    for (int i = 0; i < productDetails.length; i += 7) {
                        String name = productDetails[i];
                        String author = productDetails[i + 1];
                        String genre = productDetails[i + 2];
                        String description = productDetails[i + 3];
                        String storeName = productDetails[i + 4];
                        int quantity = Integer.parseInt(productDetails[i + 5]);
                        double price = Double.parseDouble(productDetails[i + 6]);
                        books.add(new Book(name, author, genre, description, storeName, quantity, price));
                    }
                }
            }
        }
        return books;
    }
}

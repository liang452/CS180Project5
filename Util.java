/*
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Util {
    /*
     * Checks if a String is a number
     */
    public static boolean isNumeric(String input) throws NumberFormatException {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /*
     * Checks if input is yes or no, and returns a boolean based on that. Returns true if yes, returns false if no.
     * Throws an error if neither.
     */
    public static boolean yesNo(String input) throws InvalidInputError {
        if (input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("Y")) {
            return true;
        } else if (input.equalsIgnoreCase("NO") || input.equalsIgnoreCase("N")) {
            return false;
        } else {
            throw new InvalidInputError("This is a yes or no question.");
        }
    }
    public static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        } else if (email.contains(",")) {
            System.out.println("Commas are not allowed in emails.");
            return false;
        }
        return true;
    }

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

    public static ArrayList<Product> readCSV (String filename) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        String line = bfr.readLine();
        ArrayList<Product> products = new ArrayList<>();
        while (line != null && !line.isEmpty()) {
            String[] productDetails =  line.split(",");
            if (productDetails.length != 5) {
                System.out.println("Please input a properly formatted file.");
            } else {
                for (int i = 0; i < productDetails.length; i += 5) {
                    String name = productDetails[i];
                    String storeName = productDetails[i + 1];
                    String description = productDetails[i + 2];
                    int quantity = Integer.parseInt(productDetails[i + 3]);
                    double price = Double.parseDouble(productDetails[i + 4]);
                    products.add(new Product(name, storeName, description, quantity, price));
                }
            }
            line = bfr.readLine();
        }
        return products;
    }
}

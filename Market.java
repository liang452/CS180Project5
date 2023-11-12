import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Useful functions for displaying the marketplace.
 */
public abstract class Market {
    public static void displayAllStores() throws IOException {
        //iterate through logins, find all sellers and their usernames
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        String line = bfr.readLine();
        ArrayList<String> sellers = new ArrayList<>();
        while (line != null) {
            String[] loginDetails = line.split(",");
            if (loginDetails[3].equals("SELLER")) {
                sellers.add(loginDetails[3]);
            }
            line = bfr.readLine();
        }
        bfr.close();
        //iterate through seller username list
        for (int i = 0; i < sellers.size(); i++) {
            File f = new File(sellers.get(i) + ".csv");
            if (f.exists()) {
                //read from user file, display as we go?
                bfr = new BufferedReader(new FileReader(f));
                line = bfr.readLine();
                while (line != null) {
                    String[] productDetails = line.split(",");
                    String storeName = productDetails[0];
                    for (int i = 1; i < productDetails.length; i += 4) {
                        String name = productDetails[i];
                        String description = productDetails[i + 1];
                        int quantity = productDetails[];
                        double price;
                        //and print
                    }
                }
            }
        }
    }
}

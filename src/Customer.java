/*
 */

import java.io.IOException;

public class Customer extends User {
    private int userID;
    public Customer(String username, String password) throws IOException {
        super(username, password);
        this.userID = super.getUserID();
    }
    public void purchase() {

    }
    public void addToCart() {

    }
    public void pastPurchases() {

    }
    public void viewCart() {

    }
    public void viewDashBord() {
        //dashboard
    }

}

/*
 */

import java.io.IOException;

public class TestCases {
    public static void main (String[] args) throws IOException {
        Customer customer = new Customer("anon", "a@gmail.com", "123");
        Seller seller = new Seller("default", "b@gmail.com", "111");

        if (customer.getPassword().equals("123")
                    && customer.getUsername().equals("anon")
                    && customer.getEmail().equals("a@gmail.com")) {
                System.out.println("Customer passed checks.");
        } else {
            System.out.println("Customer failed checks.");
        }

        if (seller.getPassword().equals("111")
                && seller.getUsername().equals("default")
                && seller.getEmail().equals("b@gmail.com")) {
            System.out.println("Seller passed checks.");
        } else {
            System.out.println("Seller failed checks.");
        }
    }
}

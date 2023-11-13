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

        Store store = new Store("Jane's Knickknacks", "mystore.csv");
        if (store.getName().equals("Jane's Knickknacks")) {
            System.out.println("Store passed checks.");
        } else {
            System.out.println("Store failed checks.");
        }
        if (store.getProducts().get(0).getName().equals("Pride and Prejudice")
        && store.getProducts().get(0).getAuthor().equals("Jane Austen")) {
            System.out.println("Book passed checks.");
        } else {
            System.out.println("Book failed checks.");
        }
    }
}

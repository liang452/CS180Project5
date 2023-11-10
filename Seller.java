import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Product> products;
    private List<Sale> sales;
    private List<CustomerShoppingCartEntry> customerShoppingCarts;

    public Seller(String email, String password) {
        super(email, password);
        this.products = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.customerShoppingCarts = new ArrayList<>();
    }

    // Method to view the number of products in customer shopping carts
    public void viewCustomerShoppingCarts() {
        System.out.println("Customer Shopping Carts:");
        for (CustomerShoppingCartEntry entry : customerShoppingCarts) {
            Customer customer = entry.getCustomer();
            Product product = entry.getProduct();
            System.out.println("Customer: " + customer.getEmail());
            System.out.println("  Product: " + product.getName());
            System.out.println("  Quantity: " + product.getQuantity());
            System.out.println("  Price: $" + product.getPrice());
            System.out.println("  -------------");
        }
    }

    // Method to update customer shopping carts after a sale
    private void updateCustomerShoppingCarts(Sale sale) {
        Customer customer = sale.getCustomer();
        Product product = sale.getProduct();
        customerShoppingCarts.add(new CustomerShoppingCartEntry(customer, product));
    }

    // Inner class to represent a Sale
    private static class Sale {
        private static int nextSaleId = 1;

        private int saleId;
        private Customer customer;
        private Product product;
        private int quantity;
        private double revenue;

        public Sale(Customer customer, Product product, int quantity) {
            this.saleId = nextSaleId++;
            this.customer = customer;
            this.product = product;
            this.quantity = quantity;
            this.revenue = product.getPrice() * quantity;
        }

        public int getSaleId() {
            return saleId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getRevenue() {
            return revenue;
        }
    }

    // Method to make a sale
    public void makeSale(Customer customer, Product product, int quantity) {
        if (products.contains(product) && product.getQuantity() >= quantity) {
            product.updateQuantity(quantity); // Update the quantity of the product
            Sale sale = new Sale(customer, product, quantity);
            sales.add(sale); // Add the sale to the list
            updateCustomerShoppingCarts(sale);
            System.out.println("Sale successful!");
        } else {
            System.out.println("Product not found or insufficient quantity available.");
        }
    }

    // Inner class to represent a customer shopping cart entry
    private static class CustomerShoppingCartEntry {
        private Customer customer;
        private Product product;

        public CustomerShoppingCartEntry(Customer customer, Product product) {
            this.customer = customer;
            this.product = product;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Product getProduct() {
            return product;
        }
    }
}

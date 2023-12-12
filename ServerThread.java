/*
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    Socket socket;
    private ArrayList<Book> allBooks;
    private ArrayList<String> sellerNames;
    private ArrayList<String> customerNames;
    ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.allBooks = new ArrayList<Book>();
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));

        this.sellerNames = new ArrayList<>();
        this.customerNames = new ArrayList<>();

        String line = bfr.readLine();
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
        for (String name : sellerNames) {
            File f = new File(name + ".csv");
            if (f.exists()) {
                ArrayList<Book> bookList = Util.readCSVToBook(name + ".csv"); //reads all products from seller
                this.allBooks.addAll(bookList);
            }
        }
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            BufferedWriter loginWriter = new BufferedWriter(new FileWriter("login.csv"));
            BufferedReader loginReader = new BufferedReader(new FileReader("login.csv"));

            String input = (String) ois.readObject();
            System.out.println("Received " + input);
            System.out.println("Doing things!");
            while ((input != null  && !input.isEmpty())) {
                switch (input) {
                    case "LOGIN": {
                        System.out.println("Logging in...");
                        String email = (String) ois.readObject(); //reads in email. checks logins.csv for email.
                        System.out.println("Received " + email);
                        if (!User.isExistingEmail(email)) { //if this is false, loop
                            if (email.equals("CANCEL")) {
                                System.out.println("Cancelled!");
                                break;
                            }
                            oos.writeObject("INVALID");
                            System.out.println("Invalid email!");
                            oos.flush();
                            break;
                        }
                        oos.writeObject("VALID\n");
                        System.out.println("Valid email!");
                        oos.flush();

                        String password = (String) ois.readObject();
                        if (!User.checkPassword(email, password)) {
                            if (password.equals("CANCEL")) {
                                break;
                            }
                            oos.writeObject("INVALID");
                            oos.flush();
                            break;
                        }
                        oos.writeObject("VALID");
                        oos.flush();
                        oos.writeObject(User.getUserFromEmail(email)); //writes out username
                        oos.flush();
                        oos.writeObject(User.accountType(email)); //writes out account type
                        oos.flush();
                        break;
                    }
                    case "CREATE ACCOUNT": {
                        System.out.println("Initializing account");
                        String[] accountDetails = (String[]) ois.readObject();
                        if (User.isExistingUser(accountDetails[0]) || User.isExistingEmail(accountDetails[1])) {
                            oos.writeBoolean(false);
                            oos.flush();
                        } else {
                            oos.writeBoolean(true);
                            oos.flush();
                            loginWriter.write(User.toCSV(accountDetails));
                            (new File(accountDetails[0])).createNewFile();
                        }
                        break;
                    }
                    case "MARKET": {
                        boolean repeat;
                            repeat = false;
                            String newInput = (String) ois.readObject();
                            switch (newInput) {
                                case "BUY":
                                    int amount = (int) ois.readObject();
                                    System.out.println("Amount: " + amount);
                                    Book selection = (Book) ois.readObject();
                                    String username = (String) ois.readObject();
                                    System.out.println(username + " is buying " + amount + " of " + selection.getName());
                                    BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
                                    BufferedWriter bw = new BufferedWriter(new FileWriter(username + ".csv"));
                                    Book holder = null;
                                    boolean found = false;
                                    for (int i = 0; i < this.allBooks.size(); i++) {
                                        holder = this.allBooks.get(i);
                                        if (holder.equals(selection)) {
                                            this.allBooks.remove(i);
                                            int newQuantity = holder.getQuantity() - amount;
                                            System.out.println("New quantity: " + newQuantity);
                                            holder.setQuantity(amount); //quantity purchased
                                            String cart = bfr.readLine(); //cart is first line of customer file
                                            String pastPurchases = bfr.readLine(); //pastpurchases is second line
                                            if (pastPurchases != null && !pastPurchases.equals("")) {
                                                pastPurchases += holder.toCSVFormat();
                                            } else {
                                                pastPurchases = "";
                                            }
                                            bw.write(cart + "\n");
                                            bw.flush();
                                            bw.write(pastPurchases + "\n");
                                            bw.flush();
                                            found = true;
                                            holder.setQuantity(newQuantity);
                                            break;
                                        }
                                    }
                                    if (found) {
                                        this.allBooks.add(holder);
                                    }
                                    String updatedBooks = ""; //arraylist of all books
                                    for (Book book : this.allBooks) {
                                        updatedBooks += book.toCSVFormat();
                                    }
                                    oos.writeObject(updatedBooks + "\n"); //updated list of all books in csv format
                                    oos.flush();
                                    System.out.println("Wrote: " + updatedBooks);
                                    //update seller.
                                    System.out.println("Book is sold by: " + selection.getBookSeller(sellerNames));
                                    System.out.println("old book: " + selection.getName() + " new book: " + holder.getName());
                                    BufferedReader bfr2 =
                                            new BufferedReader(new FileReader(selection.getBookSeller(sellerNames) +
                                                    ".csv"));
                                    System.out.println("What's in the seller file before calling updateProduct: " + bfr2.readLine());
                                    System.out.println("Changed successfully? " + Seller.updateProduct(selection, holder, selection.getBookSeller(sellerNames)));
                                    break;
                                case "ADD TO CART":
                                    break;
                            }
                            break;
                    }
                    case "RETURN": {
                        break;
                    }
                }
                input = (String) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

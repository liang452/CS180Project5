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
            while (input != null  && !input.isEmpty()) {
                switch (input) {
                    case "LOGIN": {
                        System.out.println("Logging in...");
                        String email = (String) ois.readObject(); //reads in email. checks logins.csv for email.
                        System.out.println("Received " + email);
                        if (!User.isExistingEmail(email)) { //while this is false, loop
                            if (email.equals("CANCEL")) {
                                System.out.println("Cancelled!");
                                break;
                            }
                            oos.writeObject("INVALID\n");
                            System.out.println("Invalid email!");
                            oos.flush();
                            email = (String) ois.readObject();
                        }
                        oos.writeObject("VALID\n");
                        System.out.println("Valid email!");
                        oos.flush();

                        String password = (String) ois.readObject();
                        while (!User.checkPassword(email, password)) {
                            if (password.equals("CANCEL")) {
                                break;
                            }
                            oos.writeObject("INVALID\n");
                            oos.flush();
                        }
                        oos.writeObject("VALID\n");
                        oos.flush();
                        oos.writeObject(User.getUserFromEmail(email) + "\n"); //writes out username
                        oos.flush();
                        oos.writeObject(User.accountType(email) + "\n"); //writes out account type
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
                    }
                    case "MARKET": {
                        String newInput = (String) ois.readObject();
                        switch (newInput) {
                            case "BUY":
                                String amount = (String) ois.readObject();
                                Book selection = (Book) ois.readObject();
                                String username = (String) ois.readObject();
                                System.out.println(username + " is buying " + amount + " of " + selection.getName());
                                BufferedReader bfr = new BufferedReader(new FileReader(username + ".csv"));
                                BufferedWriter bw = new BufferedWriter(new FileWriter(username + ".csv"));
                                Book holder;
                                for (Book book : this.allBooks) {
                                    if (book.equals(selection)) {
                                        holder = book;
                                        holder.removeQuantity(Integer.parseInt(amount));
                                        this.allBooks.remove(book);
                                        this.allBooks.add(holder);
                                        holder.setQuantity(Integer.parseInt(amount));
                                        String cart = bfr.readLine();
                                        String pastPurchases = bfr.readLine();
                                        if (pastPurchases != null && !pastPurchases.equals("")) {
                                            pastPurchases += holder.toCSVFormat();
                                        }
                                        bw.write(cart + "\n");
                                        bw.flush();
                                       bw.write(pastPurchases);
                                       bw.flush();
                                       break;
                                    }
                                }
                                String updatedBooks = ""; //arraylist of all books
                                for (Book book : this.allBooks) {
                                    updatedBooks += book.toCSVFormat();
                                }
                                oos.writeObject(updatedBooks); //updated list of all books in csv format
                                oos.flush();
                        }
                    }
                }
                input = (String) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

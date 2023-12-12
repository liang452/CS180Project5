/*
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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
            try {
                Double.parseDouble(input);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
    }
    /**
     * @return Checks if input is yes or no, and returns a boolean based on that. Returns true if yes, returns false if
     * no.
     */
    public static boolean yesNo(String message, String title) {
        boolean repeat;
        boolean checker = false;
        do {
            int input = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
            repeat = false;
            if (input == JOptionPane.YES_OPTION) {
                checker = true;
            } else if (input == JOptionPane.NO_OPTION) {
                checker = false;
            } else {
                checker = false;
                repeat = false;
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
     * @param input
     * @return Takes an input String, and if that string is a filename, reads from the file. If not, reads the string
     * into a book.
     * @throws IOException
     */
    public static ArrayList<Book> readCSVToBook(String input) throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        if (input.contains(".csv")) { //if it's a filename
            System.out.println("Filename: " + input);
            BufferedReader bfr = new BufferedReader(new FileReader(input));
            String line = bfr.readLine();
            if (line == null || line.isEmpty()) {
                System.out.println("Empty file!");
            } else {
                while (line != null && !line.isEmpty()) {
                    String[] productDetails = line.split(",");
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
                    line = bfr.readLine();
                }
            }
        } else { //if it's not a filename
            if (!input.isEmpty()) {
                String[] productDetails = input.split(",", 0);
                if (productDetails.length < 7) {
                    System.out.println("Please input a properly formatted string.");
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

    public static String toCSV(String[] input) {
        String csv = "";
        for (String string : input) {
            csv += (string + ",");
        }
        csv = csv.substring(0, csv.length() - 1); //removes comma at the very end
        return csv;
    }

    //take list of books. convert to panel with scroller. return panel.
    //TODO: test.
    public static JPanel bookPanel(ArrayList<Book> booksList) {
        Book[] books = new Book[booksList.size()];
        for (int i = 0; i < booksList.size(); i++) {
            books[i] = booksList.get(i);
        }

        JPanel bookPanel = new JPanel(); //entire panel
        JPanel listPanel = new JPanel(); //panel for list and listScroller
        JPanel infoPanel = new JPanel(); //panel for the information

        //need list with only brief info; list used in bookList
        Object[] bookShort = new Object[books.length];
        for (int i = 0; i < booksList.size(); i++) {
            bookShort[i] = (booksList.get(i).getName() + " - " + booksList.get(i).getAuthor());
        }

        JList bookList = new JList(bookShort);
        bookList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        bookList.setSize(new Dimension(200, 200));
        bookList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        bookList.setLayoutOrientation(JList.VERTICAL_WRAP);
        bookList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(bookList); //scrollPane with productList
        listScroller.setPreferredSize(new Dimension(200, 200));
        listPanel.add(listScroller);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(200, 200));
        JLabel name = new JLabel();
        JLabel author = new JLabel();
        JLabel genre = new JLabel();
        JLabel desc = new JLabel();
        JLabel quantity = new JLabel();
        JLabel price = new JLabel();
        JLabel store = new JLabel();

        infoPanel.add(name);
        infoPanel.add(author);
        infoPanel.add(genre);
        infoPanel.add(desc);
        infoPanel.add(quantity);
        infoPanel.add(price);
        infoPanel.add(store);

        ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = bookList.getSelectedIndex();
                    if (index != -1) {
                        //selection, display extended information
                        name.setText(books[index].getName());
                        author.setText("Author: " + books[index].getAuthor());
                        genre.setText("Genre: " + books[index].getGenre().name());
                        desc.setText("Summary: " + books[index].getDescription());
                        quantity.setText("Quantity Available: " + books[index].getQuantity());
                        price.setText("$" + (books[index].getPrice()));
                        store.setText("Sold by: " + (books[index].getStore()));
                    }
                }
            }
        };

        bookList.addListSelectionListener(listener);

        bookPanel.add(listPanel);
        bookPanel.add(infoPanel);

        return bookPanel;
    }
}

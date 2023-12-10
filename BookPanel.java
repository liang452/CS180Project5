/*
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class BookPanel {
    private JPanel bookPanel = new JPanel(); //entire panel
    private JPanel listPanel = new JPanel(); //panel for list and listScroller
    private JPanel infoPanel = new JPanel(); //panel for the information
    private int selectedIndex = -1;
    private Book[] books;
    public BookPanel(ArrayList<Book> booksList) {
        books = new Book[booksList.size()];
        for (int i = 0; i < booksList.size(); i++) {
            books[i] = booksList.get(i);
        }

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
                    selectedIndex = bookList.getSelectedIndex();
                    if (index != -1) {
                        //selection, display extended information
                        name.setText(books[index].getName());
                        author.setText("Author: " + books[index].getAuthor());
                        genre.setText("Genre: " + books[index].getGenre().name());
                        desc.setText("Summary: " + books[index].getDescription());
                        quantity.setText("Quantity: " + books[index].getQuantity());
                        price.setText("$" + (books[index].getPrice()));
                        store.setText("Sold by: " + (books[index].getStore()));
                    }
                }
            }
        };

        bookList.addListSelectionListener(listener);

        bookPanel.add(listPanel);
        bookPanel.add(infoPanel);

    }

    public JPanel getBookPanel() {
        return bookPanel;
    }
    public JPanel getListPanel() {
        return listPanel;
    }
    public JPanel getInfoPanel() {
        return infoPanel;
    }
    public Book selectedBook() {
        if (selectedIndex != -1) {
            Book selectedBook = books[selectedIndex];
            return selectedBook;
        } else {
            return null;
        }
    }
}

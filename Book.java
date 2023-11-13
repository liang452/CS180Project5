import java.util.Scanner;

public class Book {
    private String name;
    private String author;
    private static Genre genre;
    private String description;
    private String store;
    private int quantity;
    private double price;

    enum Genre {
        FANTASY,
        SCIENCE_FICTION,
        HORROR,
        THRILLER,
        HISTORICAL,
        NONFICTION,
        ROMANCE,
        YOUNG_ADULT,
        INVALID_GENRE
    }
  
    public Book(String name, String author, String genre, String description, String store, int quantity,
                double price) {
        this.name = name;
        this.author = author;
        this.genre = Book.readGenre(genre);
        this.description = description;
        this.store = store;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStore() {
        return this.store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = readGenre(genre);
    }

    public static Genre readGenre(String genreString) {
        Genre readGenre = null;
        if (genreString.equalsIgnoreCase("FANTASY")) {
            readGenre = Genre.FANTASY;
        } else if (genreString.equalsIgnoreCase("SCIENCE FICTION") ||
        genreString.equalsIgnoreCase("SCIENCE_FICTION")) {
            readGenre = Genre.SCIENCE_FICTION;
        } else if (genreString.equalsIgnoreCase("HORROR")) {
            readGenre = Genre.HORROR;
        } else if (genreString.equalsIgnoreCase("THRILLER")) {
            readGenre = Genre.THRILLER;
        } else if (genreString.equalsIgnoreCase("HISTORICAL")) {
            readGenre = Genre.HISTORICAL;
        } else if (genreString.equalsIgnoreCase("NONFICTION")) {
            readGenre = Genre.NONFICTION;
        } else if (genreString.equalsIgnoreCase("ROMANCE")) {
            readGenre = Genre.ROMANCE;
        } else if (genreString.equalsIgnoreCase("YOUNG ADULT") ||
                genreString.equalsIgnoreCase("YOUNG_ADULT")) {
            readGenre = Genre.YOUNG_ADULT;
        } else {
            readGenre = Genre.INVALID_GENRE;
        }
        return readGenre;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void displayProduct() {
        System.out.println(this.name + " - " + this.getStore() + " - $" + this.getPrice());
    }

    public void displayProductInfo() {
        System.out.println("Product: " + this.name);
        System.out.println("Store: " + this.store);
        System.out.println("Description: " + this.description);
        System.out.println("Quantity: " + this.quantity);
        System.out.println("Price: $" + this.price);
    }
  
    //takes integer as input, removes that amount from the current quantity
    public boolean removeQuantity(int amount) {
        if (amount <= quantity) {
            this.quantity -= amount;
            return true;
        } else {
            return false;
        }
    }
    public boolean addQuantity(int amountToAdd) {
        if (amountToAdd <= 0) {
            return false;
        }
        this.quantity += amountToAdd;
        return true;
    }

    public static Book createBookFromUserInput() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter book name:");
        String name = scan.nextLine();

        System.out.println("Enter author: ");
        String author = scan.nextLine();

        System.out.println("Enter the genre: ");
        String genre = scan.nextLine();

        System.out.println("Enter book description:");
        String description = scan.nextLine();

        System.out.println("Enter store name:");
        String store = scan.nextLine();

        System.out.println("Enter quantity available:");
        int quantity = scan.nextInt();
        scan.nextLine();

        System.out.println("Enter price:");
        double price = scan.nextDouble();

        return new Book(name, author, genre, description, store, quantity, price);
    }
      
    public boolean equals(Book book) {
        if (this.getName().equals(book.getName())) {
            return true;
        } else {
            return false;
        }
    }
      
    public String toCSVFormat() {
        String name = this.getName();
        String author = this.getAuthor();
        String genre = this.getGenre().name();
        String description = this.getDescription();
        String storeName = this.getStore();
        String quantity = Integer.toString(this.getQuantity());
        String price = Double.toString(this.getPrice());
        String csv = name + "," +  author + "," + genre + "," + description + "," +
                storeName + "," + quantity + "," + price;
        return csv;
    }

}


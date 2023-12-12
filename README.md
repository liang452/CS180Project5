# CS 180 Project 5
## How to Run:
To run the code, first run the Server.java class, then run the Driver.java class to start the application.
### Notes on Formatting:
- Don't use commas in descriptions.
- When selecting an item from the market list, make sure to match case.
- The seller files list out all of the products they've created
- The customer class first lists out all past purchases in one line, then all cart items in one line
  
## Who Submitted What
Mrigas - Submitted Report on Brightspace.
Ruiwen - Submitted cod on Vocareum


## Documentation
### Book
The Book class is the product in our marketplace. 
### Store
The Store class contains data for a store. It stores a store name and a list of books.
### Seller
Seller declares the Seller object.
### Customer
Declares the Customer object.
### Util
Useful methods that didn't belong in any particular class.
### Market
Where the information from all the Sellers and Customers is loaded into, and processed through various methods to 
then be presented.
### BookPanel
Where the graphical component for displaying the books in the bookstore is created.
### User
Where a user in the system is stored and managed, for things like passwords, usernames, and emails. 
### Server
The Server class creates the server for port 8484 to which the client connects.
### Driver
The Driver class gives users access to the application and its features.



## Description of Classes

### Book Class:

Functionality: Represents a book with various attributes such as name, author, genre, description, store, quantity, and price. 
Provides methods to get and set these attributes, display product information, and manage quantities. 
Includes an enumeration Genre for different book genres. 
Provides a method createBookFromUserInput() to create a book by taking input from the user.

Testing: The class has methods for setting and getting attributes, and these can be tested by creating instances of the Book class and manipulating its properties.

### Store Class:

Functionality: Represents a store for a seller to sell their books from. 
Handles and manages a store's information including products available, quantity, price, and more. 
Has constructors to create new stores without any initial products or through a CSV file input. 

Testing: The class can be tested by creating a Store object, adding books to your store, and performing operations like viewing products available and modifying the details of those products.

### Customer Class:

Functionality: Represents a customer user, extending the User class. 
Manages a shopping cart, past purchases, and includes methods to add, remove, and view items in the cart. 
Provides methods to export user data to a file.

Testing: The class can be tested by creating a Customer object, adding items to the cart, and performing operations like viewing the cart and past purchases.

### Seller Class:

Functionality: Represents a seller user, extending the User class. 
Manages stores, including adding, displaying, and exporting store information. 
Provides methods to manage products, such as adding, editing, and exporting them.

Testing: Testing involves creating a Seller object, adding a store, and performing operations like adding and editing products.

### Market Class:

Functionality: Represents the market and serves as an interface for users (customers and sellers). 
Manages the overall flow of the application, including listing products, displaying menus, and handling user input.

Testing: Testing involves creating a Market object and simulating user interactions, such as listing products and navigating through menus.

### Util Class:

Functionality: Provides utility methods used across the project, such as reading CSV data and checking user input.

Testing: The utility methods can be tested by creating instances of the Util class and calling these methods with various inputs.

### BookPanel Class:

Functionality: Graphical component that was created to display the different books available in the marketplace as well as 
any corresponding information about those books. 

Testing: The class can be tested by logging in as a customer and viewing the marketplace for the available books and their details for each one.

### User Class:

Functionality: Contains the main method and serves as the entry point for the application. 
Handles user authentication, account creation, and interaction with the market.

Testing: Testing involves running the entire application and verifying that user authentication, account creation, and market interactions work as expected.

### Server Class:

Functionality: Contains the main method and serves as the entry point for the application. 
Handles user authentication, account creation, and interaction with the market.

Testing: Testing involves running the entire application and verifying that user authentication, account creation, and market interactions work as expected.

### Driver Class:

Functionality: Contains the main method and serves as the entry point for the application. 
Handles user authentication, account creation, and interaction with the market.

Testing: Testing involves running the entire application and verifying that user authentication, account creation, and market interactions work as expected.




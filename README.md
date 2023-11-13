# cs180project4
## How to Run:
Run Main.
### Notes on Formatting:
Don't use commas in descriptions.
## Who Submitted What
Ashish - Submitted Report on Brightspace.



## Documentation
### Book
The Book class is the product in our marketplace. 
#### readGenre()
Takes in a String, and checks if it matches a value in the Genre enum, and returns that value.
#### displayProduct()
Displays the book that calls this method in the format Customers will see it in, in the marketplace.
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
### Main
Where everything is implemented.


## Description of Classes

### Book Class:

Functionality: Represents a book with various attributes such as name, author, genre, description, store, quantity, and price. 
Provides methods to get and set these attributes, display product information, and manage quantities. 
Includes an enumeration Genre for different book genres. 
Provides a method createBookFromUserInput() to create a book by taking input from the user.

Testing: The class has methods for setting and getting attributes, and these can be tested by creating instances of the Book class and manipulating its properties.

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

### Main Class:

Functionality: Contains the main method and serves as the entry point for the application. 
Handles user authentication, account creation, and interaction with the market.

Testing: Testing involves running the entire application and verifying that user authentication, account creation, and market interactions work as expected.

### InvalidInputError and InvalidQuantityError Classes:

Functionality: Represent custom error classes for handling invalid input and quantity errors. 
Extend the Error class to provide more specific error messages.

Testing: Testing involves intentionally triggering these errors and verifying that they are caught and handled appropriately.


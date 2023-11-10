/*
 * An exception that occues when the user tries to input an invalid quantity of item.
 */

public class InvalidQuantityError extends Error {
    public InvalidQuantityError() {
        super("Please input a valid quantity.");
    }
}

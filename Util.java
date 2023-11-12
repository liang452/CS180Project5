/*
 */

public abstract class Util {
    /*
     * Checks if a String is a number
     */
    public static boolean isNumeric(String input) throws NumberFormatException {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /*
     * Checks if input is yes or no, and returns a boolean based on that. Returns true if yes, returns false if no.
     * Throws an error if neither.
     */
    public static boolean yesNo(String input) throws InvalidInputError {
        if (input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("Y")) {
            return true;
        } else if (input.equalsIgnoreCase("NO") || input.equalsIgnoreCase("N")) {
            return false;
        } else {
            throw new InvalidInputError("This is a yes or no question.");
        }
    }
}

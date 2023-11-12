/*
 */

public abstract class Util {
    public static boolean isNumeric(String input) throws NumberFormatException {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
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

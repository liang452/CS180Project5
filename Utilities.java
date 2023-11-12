/*
 */

public abstract class Utilities {
    public static boolean isNumeric(String number) throws NumberFormatException {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

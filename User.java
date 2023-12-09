import java.io.*;
import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String password;
    public User() {
        username = "";
        email = "";
        password = "";
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        //check if duplicate email
        //need a file of emails?
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public static boolean isExistingUser(String username) throws IOException {
        //check file for if username already exists
        try (BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                //splits line into array
                String[] loginInfo = line.split(",");
                //first item is username
                String existingUsername = loginInfo[0];
                if (username.equals(existingUsername)) {
                    return true;
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        //if not found, return false
        return false;
    }

    public static boolean isExistingEmail(String email) throws IOException {
        //check file of emails and passwords for if email already exists
        try (BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"))) {
            String line = bfr.readLine();
            while (line != null && !line.isEmpty()) {
                //splits line into array
                String[] loginInfo = line.split(",");
                //second item is email
                String existingEmail = loginInfo[1];
                if (email.equals(existingEmail)) {
                    return true;
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        //if not found, return false
        return false;
    }
    public static boolean checkPassword(String email, String password) throws IOException {
        //check file of emails and passwords for if email already exists
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        while (line != null && !line.isEmpty()) {
            //splits line into array
            String[] loginInfo = line.split(",");
            //second item is email
            String fileUser = loginInfo[1];
            //third item is password
            String filePassword = loginInfo[2];
            //if the given email matches the line in the file
            if (email.equals(fileUser)) {
                if (password.equals(filePassword)) {
                    return true;
                }
            }
            line = bfr.readLine();
        }
        //if not found, return false. if no email matches or password matches.
        return false;
    }
    public static void editUsername(String oldUsername, String newUsername) throws IOException {
        if (!User.isExistingEmail(newUsername)) {
            BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
            ArrayList<String> loginsList = new ArrayList<>();

            String newLogin = "";
            String line = bfr.readLine();
            while (line != null && !line.isEmpty()) {
                String[] loginDetails = line.split(",");
                if (!loginDetails[0].equals(oldUsername)) { //if not equal to the old username, just add
                    loginsList.add(toCSV(loginDetails));
                } else if (loginDetails[0].equals(oldUsername)) { //if this is the old username, alter the login a
                    // little
                    newLogin = newUsername + "," + loginDetails[1] + "," +
                            loginDetails[2] + "," + loginDetails[3];
                }
                line = bfr.readLine();
            }
            bfr.close();
            //loop through array
            loginsList.add(newLogin); //add login that was changed
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv"));
            for (String string : loginsList) {
                bw.write(string);
                bw.write("\n");
            }
            bw.close();
            File f = new File(oldUsername + ".csv");
            File g = new File(newUsername + ".csv");
            g.createNewFile();
            f.renameTo(g);
            f.delete();
        } else {
            System.out.println("This username already exists.");
        }
    }
    public static void editPassword(String email, String newPassword) throws IOException {
            BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
            ArrayList<String> loginsList = new ArrayList<>();
            String line = bfr.readLine();
            String newLogin = "";
            while (line != null && !line.isEmpty()) {
                String[] loginDetails = line.split(",");
                if (!loginDetails[1].equals(email)) {
                    loginsList.add(toCSV(loginDetails));
                } else if (loginDetails[1].equals(email)) {
                    newLogin = loginDetails[0] + "," + loginDetails[1] + "," +
                            newPassword + "," + loginDetails[3];
                }
                line = bfr.readLine();
            }
            bfr.close();
            //loop through array
            loginsList.add(newLogin); //add login that was changed
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv"));
            for (String string : loginsList) {
                bw.write(string);
                bw.write("\n");
            }
            bw.close();
    }
    public static void editEmail(String oldEmail, String newEmail) throws IOException {
        if (!User.isExistingEmail(newEmail)) {
            BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
            ArrayList<String> loginsList = new ArrayList<>();
            String line = bfr.readLine();
            String newLogin = "";
            while (line != null && !line.isEmpty()) {
                String[] loginDetails = line.split(",");
                if (!loginDetails[1].equals(oldEmail)) {
                    loginsList.add(toCSV(loginDetails));
                } else if (loginDetails[1].equals(oldEmail)) {
                    newLogin = loginDetails[0] + "," + newEmail + "," +
                            loginDetails[2] + "," + loginDetails[3];
                }
                line = bfr.readLine();
            }
            bfr.close();
            //loop through array
            loginsList.add(newLogin); //add login that was changed
            BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv"));
            for (String string : loginsList) {
                bw.write(string);
                bw.write("\n");
            }

            bw.close();
        } else {
            System.out.println("This email already exists.");
        }
    }
    public static void deleteAccount(String username, String email, String password) throws IOException {
        //delete the logins
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        ArrayList<String> loginDetails = new ArrayList<>();
        while (line != null && !line.isEmpty()) {
            loginDetails.add(line);
            line = bfr.readLine();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv")); //overwrite old logins.csv file
        String combined = username + "," + email + "," + password;
        for (String details : loginDetails) {
            if (!details.contains(combined)) { //if line does not match
                bw.write(details);
            }
        }
        File f = new File(username + ".csv");
        f.delete();
    }

    /**
     *
     * @param input
     * @return Checks if the String input matches either email or username, and returns the account type as a String.
     * @throws IOException
     */
    public static String accountType(String input) throws IOException {
        //returns 0 if customer, 1 if seller
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        while (line != null && !line.equals("")) { //while it's not empty
            String[] loginDetails = line.split(",");
            if (loginDetails[0].equals(input) || loginDetails[1].equals(input)) {
                return loginDetails[3];
            }
            line = bfr.readLine();
        }
        bfr.close();
        return "";
    }
    public static String toCSV(String[] loginDetails) {
        String combined = "";
        for (String string : loginDetails) {
            combined += string + ",";
        }
        return combined;
    }

    /**
     *
     * @param email
     * @return Returns the username from an email.
     * @throws IOException
     */
    public static String getUserFromEmail(String email) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        while (line != null && !line.equals("")) {
            String[] loginDetails = line.split(",");
            if (email.equals(loginDetails[1])) {
                return loginDetails[0];
            }
            line = bfr.readLine();
        }
        return "Username not found.";
    }


}

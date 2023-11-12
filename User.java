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
            while (line != null) {
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
        while (line != null) {
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
    public static boolean editUsername() {
        return true;
    }
    public static void editPassword(String newPassword) {
        //TODO
    }
    public static void deleteAccount(String username, String email, String password) throws IOException {
        //delete the logins
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        ArrayList<String> loginDetails = new ArrayList<>();
        while (line != null) {
            loginDetails.add(line);
            line = bfr.readLine();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv")); //overwrite old logins.csv file
        String combined = username + "," + email + "," + password;
        for (String details : loginDetails) {
            if (!details.equals(combined)) { //if line does not match
                bw.write(line);
            }
        }
        File f = new File(username + ".csv");
        f.delete();
    }
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


}

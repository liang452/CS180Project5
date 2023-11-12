import java.io.*;

public class User {
    private String email;
    private String password;
    private String username;

    public User(String username, String email, String password) throws IOException {
        this.email = email;
        this.password = password;
        this.username = username;
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
        //check file of emails and passwords for if email already exists
        try (BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                //splits line into array
                String[] loginInfo = line.split(",", 0);
                //first item is email
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
        BufferedWriter bw = new BufferedWriter(new FileWriter("logins.csv"));
        String combined = username + "," + email + "," + password;
        String line = bfr.readLine();
        while (line != null) {
            if (!line.equals(combined)) {
                bw.write(line);
            }
        }
    }
    public static int accountType(String username) throws IOException {
        //returns 0 if customer, 1 if seller
        BufferedReader bfr = new BufferedReader(new FileReader("logins.csv"));
        String line = bfr.readLine();
        while (line != null && !line.equals("")) {
            String[] loginDetails = line.split(",");
            if (loginDetails[0].equals(username)) {
                if (loginDetails[3].equals("SELLER")) {
                    return 1;
                } else {
                    //if customer, return 1
                    return 0;
                }
            }
            line = bfr.readLine();
        }
        return -1;
    }
}

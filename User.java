import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public abstract class User {
    private String email;
    private String password;
    private String username;
    //hashmap of userids

    public User(String username, String email, String password) throws IOException {
        this.email = email;
        this.password = password;
        this.username = username;
        //export to file
        File f = new File("logins.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter bfr = new BufferedWriter(new FileWriter("logins.txt", true));
        bfr.write(this.username + "," + this.email + "," + this.password);
        bfr.write("\n");
        bfr.close();
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
        try (BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"))) {
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
        try (BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"))) {
            String line = bfr.readLine();
            while (line != null && !line.equals("")) {
                //splits line into array
                String[] loginInfo = line.split(",", 0);
                //first item is email
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
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        String line = bfr.readLine();
        while (line != null) {
            //splits line into array
            String[] loginInfo = line.split(",", 0);
            //first item is email
            String fileUser = loginInfo[0];
            //second item is password
            String filePassword = loginInfo[1];
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
    public boolean editAccount() {
        return true;
    }
    public void deleteAccount(String email, String password) throws IOException {
        //delete the logins
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("logins.txt"));
        String combined = email + "," + password;
        String line = bfr.readLine();
        while (line != null) {
            if (!line.equals(combined)) {
                bw.write(line);
            }
        }
    }
}

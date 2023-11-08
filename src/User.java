/*
 */

import java.io.*;
import java.util.Random;

public abstract class User {
    private String username;
    private String password;
    private int userID;

    public User(String username, String password) throws IOException {
        Random random = new Random();
        //random userID
        this.userID = random.nextInt(100000000);

        this.username = username;
        this.password = password;
        //export to file
        BufferedWriter bfr = new BufferedWriter(new FileWriter("logins.txt", true));
        bfr.write(this.username + "," + this.password);
        bfr.write("\n");
        bfr.flush();
        bfr.close();
    }

    public void setUsername(String username) {
        //check if duplicate username
        //need a file of usernames?
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public static boolean isExistingUser(String username) throws IOException {
        //check file of usernames and passwords for if username already exists
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        String line = bfr.readLine();
        while (line != null) {
            //splits line into array
            String[] loginInfo = line.split(",", 0);
            //first item is username
            String fileUser = loginInfo[0];
            if (username.equals(fileUser)) {
                return true;
            }
            line = bfr.readLine();
        }
        //if not found, return false
        return false;
    }
    public static boolean checkPassword(String username, String password) throws IOException {
        //check file of usernames and passwords for if username already exists
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        String line = bfr.readLine();
        while (line != null) {
            //splits line into array
            String[] loginInfo = line.split(",", 0);
            //first item is username
            String fileUser = loginInfo[0];
            //second item is password
            String filePassword = loginInfo[1];
            //if the given username matches the line in the file
            if (username.equals(fileUser)) {
                if (password.equals(filePassword)) {
                    return true;
                }
            }
            line = bfr.readLine();
        }
        //if not found, return false. if no username matches or password matches.
        return false;
    }
    public void deleteAccount(String username, String password) throws IOException {
        //delete the logins
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("logins.txt"));
        String combined = username + "," + password;
        String line = bfr.readLine();
        while (line != null) {
            if (!line.equals(combined)) {
                bw.write(line);
            }
        }
    }
}

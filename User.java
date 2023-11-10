package src;/*
 */

import java.io.*;
import java.util.Random;

public abstract class User {
    private String email;
    private String password;

    public User(String email, String password) throws IOException {

        this.email = email;
        this.password = password;
        //export to file
        BufferedWriter bfr = new BufferedWriter(new FileWriter("logins.txt", true));
        bfr.write(this.email + "," + this.password);
        bfr.write("\n");
        bfr.flush();
        bfr.close();
    }

    public void Email(String email) {
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
    public static boolean isExistingUser(String email) throws IOException {
        //check file of emails and passwords for if email already exists
        BufferedReader bfr = new BufferedReader(new FileReader("logins.txt"));
        String line = bfr.readLine();
        while (line != null) {
            //splits line into array
            String[] loginInfo = line.split(",", 0);
            //first item is email
            String fileUser = loginInfo[0];
            if (email.equals(fileUser)) {
                return true;
            }
            line = bfr.readLine();
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

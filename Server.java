/*
 */

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8484);
        Socket socket = serverSocket.accept();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter outputWriter = new PrintWriter(socket.getOutputStream());
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("logins.csv", true));
        System.out.println("Server started!");

        while (true) {
            //receives account information
            String line = inputReader.readLine();
            System.out.println("Received " + line);
            switch (line.toUpperCase()) {
                case "LOGIN": {
                    System.out.println("Logging in...");
                    String email = inputReader.readLine();
                    System.out.println("Received " + email);
                    if (!User.isExistingEmail(email)) {
                        outputWriter.write("INVALID\n");
                        outputWriter.flush();
                        break;
                    } else {
                        outputWriter.write("VALID\n");
                        outputWriter.flush();
                        String password = inputReader.readLine();
                        if (!User.checkPassword(email, password)) {
                            outputWriter.write("INVALID\n");
                            outputWriter.flush();
                            break;
                        } else {
                            outputWriter.write("VALID\n");
                            outputWriter.flush();
                        }
                    }
                    break;
                }
                case "CREATE ACCOUNT": {
                    String username = inputReader.readLine();
                    if (username.equals("CANCEL")) {
                        break;
                    } else if (User.isExistingUser(username)) {
                        outputWriter.write("EXISTING\n");
                        outputWriter.flush();
                        System.out.println("Existing username.");
                        break;
                    } else {
                        outputWriter.write("VALID\n");
                        outputWriter.flush();
                    }
                    String email = inputReader.readLine();
                    System.out.println("Email: " + email);
                    if (User.isExistingEmail(email)) {
                        outputWriter.write("EXISTING\n");
                        outputWriter.flush();
                        System.out.println("Existing email.");
                        break;
                    } else {
                        outputWriter.write("VALID\n");
                        outputWriter.flush();
                    }
                    String password = inputReader.readLine();
                    String accountType = inputReader.readLine();
                    fileWriter.write(User.toCSV(new String[]{email, username, password, accountType}));
                    System.out.println("Account created successfully!");
                }
                case "MARKET": {
                    System.out.println("Incomplete.");
                }

            }
        }

    }


}


/*
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
            ServerSocket serverSocket = new ServerSocket(8484);
            Socket socket = serverSocket.accept();
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream());
            //receives account information
            System.out.println("Server started!");

            String line = inputReader.readLine();
            System.out.println("Received " + line);
            while (line != null && !line.equals("")) {
                switch (line.toUpperCase()) {
                    case "LOGIN": {
                        System.out.println("Logging in...");
                        String email = inputReader.readLine(); //reads in email. checks logins.csv for email.
                        System.out.println("Received " + email);
                        while (!User.isExistingEmail(email)) { //while this is false, loop
                            if (email.equals("CANCEL")) {
                                System.out.println("Cancelled!");
                                break;
                            }
                            outputWriter.write("INVALID\n");
                            System.out.println("Invalid email!");
                            outputWriter.flush();
                            email = inputReader.readLine();
                        }
                        outputWriter.write("VALID\n");
                        System.out.println("Valid email!");
                        outputWriter.flush();

                        String password = inputReader.readLine();
                        if (password.equals("CANCEL")) {
                            break;
                        }
                        while (!User.checkPassword(email, password)) {
                            outputWriter.write("INVALID\n");
                            outputWriter.flush();
                        }
                        outputWriter.write("VALID\n");
                        outputWriter.flush();
                        outputWriter.write(User.getUserFromEmail(email) + "\n"); //writes out username
                        outputWriter.flush();
                        outputWriter.write(User.accountType(email) + "\n"); //writes out account type
                        outputWriter.flush();
                    }
                    case "CREATE": {

                    }
                    case "MARKET": {

                    }
                }
            }

    }


}


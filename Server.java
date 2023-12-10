/*
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
            ServerSocket serverSocket = new ServerSocket(8484);
            while(true) { //continuously accept connections
                Socket socket = serverSocket.accept();
                System.out.println("Server started!");
                ServerThread serverThread = new ServerThread(socket); // create new thread to handle client
                serverThread.start(); // spawn new thread and continue loop
            }
    }
}


/*

My code was able to go into marketplace with this below

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8484);
        System.out.println("Server started!");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outputWriter = new PrintWriter(clientSocket.getOutputStream());
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter("logins.csv", true))
        ) {
            // Handle client requests here
            System.out.println("Client handler started!");

            while (true) {
                // Receives account information
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
                        break;
                    }
                    case "MARKET": {
                        System.out.println("Incomplete.");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

*/


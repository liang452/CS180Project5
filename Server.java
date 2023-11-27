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
        BufferedReader loginReader = new BufferedReader(new FileReader("logins.csv"));
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("logins.csv", true));
        //receives account information

        String line = inputReader.readLine();
        while (line != null) {
            switch (line.toUpperCase()) {
                case "LOGIN": {
                    String email = inputReader.readLine(); //reads in email. checks logins.csv for email.
                    if (email.equals("CANCEL")) {
                        break;
                    }
                    while (!User.isExistingEmail(email)) { //while this is false, loop
                        outputWriter.write("");
                    }
                    outputWriter.write("VALID");

                    String password = inputReader.readLine();
                    if (password.equals("CANCEL")) {
                        break;
                    }
                    while (!User.checkPassword(email, password)) {
                        outputWriter.write("");
                    }
                    outputWriter.write("VALID");
                    outputWriter.write(User.getUserFromEmail(email)); //writes out username
                    outputWriter.write(User.accountType(email)); //writes out account type
                    outputWriter.flush();
                }
                case "CREATE": {

                }
                case "MARKET": {

                }
            }
        }

        //loops through logins?


        } //writes it in if it doesn't exist/is a new account
        //ideally this should also return if it's an already existing account, and that input should be taken in and
        // used to check in the logins method. but that's a TODO.


}


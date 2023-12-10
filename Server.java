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


package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        // same thing as server

        final Socket socket; // send and receive responses.
        final BufferedReader in; // read from socket (std in)
        final PrintWriter out; // write to socket. Everything in Unix is a file and this is extended through
                               // sockets (std out)
        final String LOCAL_HOST = "127.0.0.1";
        String inputString = "";
        String response;
        String clientName = "";

        final Scanner sc = new Scanner(System.in); // get input from keyboard (as usual)

        try {
            socket = new Socket(LOCAL_HOST, 5000);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();
            while (!inputString.equals("quit") && !socket.isClosed()) {
                if (clientName.equals("")) {
                    System.out.println("Enter your Username");
                    clientName = sc.nextLine();
                    if (clientName.equals("quit")) {
                        break;
                    }
                    System.out.println("Hi " + clientName);
                }
                String message = "[" + clientName + "]" + ": ";
                System.out.print(message);
                inputString = sc.nextLine();
                if (inputString.equals("quit")) {
                    break;
                }
                System.out.println(socket.isClosed());
                if (socket.isClosed()) {
                    break;
                }
                out.println(message + inputString);
                out.flush();

            }
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("Error in Client");

        }

    }
}
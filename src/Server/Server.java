package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    /*
     * ServerSocket : this class is used by the server to declare a ServerSocket
     * object which the server needs to listen to connection requests from the
     * clients
     * 
     * Socket : this class is used by the server to declare a Socket object,
     * which the server uses to send and recieve data from the client
     */

    // so we need these things

    // final because a server must be permanent right?
    public static void main(String[] args) {
        final ServerSocket server; // listen to requests of client on a socket
        Scanner sc = new Scanner(System.in); // get input from keyboard (as usual)

        // list of clients threads

        List<ServerThread> threadList = new ArrayList<ServerThread>();

        try {
            // server listen on port 5000
            server = new ServerSocket(5000);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Server Created at " + inetAddress.getHostAddress());
            // wait for reponses and accept it

            while (true) {
                System.out.println("Waiting for Clients");

                final BufferedReader in; // read from socket (std in)
                final PrintWriter out; // write to socket. Everything in Unix is a file and this is extended through
                // sockets (std out)

                final Socket socket; // send and receive responses.
                socket = server.accept();

                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();

                System.out.println("Client Connected");
                System.out.println("Connection from " + socket.getInetAddress());

            }

        } catch (Exception e) {
            System.out.println("Error in Server");
        }

    }
}
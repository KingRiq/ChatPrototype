import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Server.ServerThread;

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
                clientSockets.add(socket); // add socket to the list of clients

                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();

                System.out.println("Client Connected");
                System.out.println("Connection from " + socket.getInetAddress());
                out = new PrintWriter(socket.getOutputStream());

                // so a socket returns a byte stream
                // 1) we get the byte stream from the socket
                // 2) we convert InputStream (bytes) to char
                // 3) BufferedReader converts this character stream into a string
                // (documentation/ stack overflow)
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // create Sender thread
                Thread receive = new Thread(new Runnable() {
                    String msg; // message from written by user

                    @Override // we need the threads to run different code so we need to override each
                    public void run() {
                        try {
                            msg = in.readLine(); // read from client

                            // while the client is still sending messages
                            while (msg != null) {
                                System.out.println("Client : " + msg);
                                out.println(msg);
                                out.flush();
                                msg = in.readLine();
                            }

                            // client disconnected!
                            System.out.println("Client Disconnected! :(");

                            // close everything up
                            out.close();
                            socket.close();
                            server.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                receive.start();
                Thread sender = new Thread(new Runnable() {
                    String msg;

                    @Override // we need the threads to run different code so we need to override each
                    public void run() {

                        // run infinitely
                        while (true) {
                            msg = sc.nextLine(); // get message from keyboard
                            out.println(msg); // write data into socket
                            out.flush(); // send
                        }
                    }
                });
                sender.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace(); // lazy
        }

    }
}
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
        final Socket socket; // send and receive responses.
        final BufferedReader in; // read from socket (std in)
        final PrintWriter out; // write to socket. Everything in Unix is a file and this is extended through
                               // sockets (std out)

        final Scanner sc = new Scanner(System.in); // get input from keyboard (as usual)

        try {
            // server listen on port 5000
            server = new ServerSocket(5000);
            // wait for reponses and accept it

            System.out.println("Waiting for Client");
            socket = server.accept();
            System.out.println("Client Connected");
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

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace(); // lazy
        }

    }
}
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

        final Scanner sc = new Scanner(System.in); // get input from keyboard (as usual)

        try {
            socket = new Socket(LOCAL_HOST, 5000);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread receive = new Thread(new Runnable() {
                String msg; // message from written by user

                @Override // we need the threads to run different code so we need to override each
                public void run() {
                    try {
                        msg = in.readLine(); // read from server

                        // while the client is still sending messages
                        while (msg != null) {
                            System.out.println("Server : " + msg);
                            msg = in.readLine();
                        }

                        // client disconnected!
                        System.out.println("Server... out of... service :(");

                        // close everything up
                        out.close();
                        socket.close();

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
            e.printStackTrace();
        }

    }
}
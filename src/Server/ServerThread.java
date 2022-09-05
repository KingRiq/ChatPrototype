package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    private Socket socket;
    private List<ServerThread> threadList = new ArrayList<ServerThread>();
    private PrintWriter output;

    public ServerThread(Socket socket, List<ServerThread> threadList) {
        this.socket = socket;
        this.threadList = threadList;

    }

    @Override // we need the threads to run different code so we need to override each
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());

            // while the client is still sending messages
            while (true) {
                String msg;
                msg = input.readLine();
                if (msg.equals("quit"))
                    break;
                System.out.println("Heard " + msg);
                sendToClients(msg);

            }

            // client disconnected!
            System.out.println("Client Disconnected! :(");

            // close everything up
            /*
             * out.close();
             * socket.close();
             * server.close();
             */
        } catch (Exception e) {
            System.out.println("Error occured " + e.getStackTrace());
        }
    }

    public void sendToClients(String output) {

        // print to all clients in the client list
        for (ServerThread client : threadList) {
            if (client.socket.isClosed()) {
                threadList.remove(client);
            }
            client.output.println(output);
            client.output.flush();
        }
        System.out.println("Printed to " + threadList.size() + "clients");
    }

}

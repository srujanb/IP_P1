package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT_NUMBER = 7734;
    private static final String HOST_IP = "localhost";
    static Scanner scanner = new Scanner(System.in);

    static Socket socket;
    static DataOutputStream dOutToServer;
    static DataInputStream dInFromServer;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST_IP,PORT_NUMBER);
            dOutToServer = new DataOutputStream(socket.getOutputStream());
            dInFromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (scanner.hasNext()) {
            String userRequest = scanner.nextLine();
            System.out.println("User request: " + userRequest);
            try {
                dOutToServer.writeUTF(userRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

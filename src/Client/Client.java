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

    private static Socket socket;
    private static DataOutputStream dOutToServer;
    private static DataInputStream dInFromServer;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST_IP,PORT_NUMBER);
            dOutToServer = new DataOutputStream(socket.getOutputStream());
            dInFromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            String userRequest = scanner.nextLine();
            handleUserRequest(userRequest);
        }
    }

    private static void handleUserRequest(String userRequest) {
        StringBuilder builder = new StringBuilder(userRequest).append("\n");
        String nextLine;
        while ((nextLine = scanner.nextLine()) != null){
            builder.append(nextLine).append("\n");
            if (nextLine.equals("")) break;
        }
        try {
            sendRequestToServer(builder.toString());
        } catch (IOException e) {
            //TODO remove stack trace
            e.printStackTrace();
            System.out.println("ERROR SENDING REQUEST TO SERVER.");
        }
    }

    private static void sendRequestToServer(String userRequest) throws IOException {
        System.out.println("Sending request to server:\n" + userRequest);
        dOutToServer.writeUTF(userRequest);
    }


}

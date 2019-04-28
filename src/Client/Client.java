package Client;

import Client.Modals.RFC;
import Utils.CommonConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private static final int PORT_NUMBER = 7734;
    private static final String HOST_IP = "localhost";
    private static final String VERSION = "P2P-CI/1.0";
    private Scanner scanner = new Scanner(System.in);

    private DataOutputStream dOutToServer;
    private DataInputStream dInFromServer;

    public Map<Integer, RFC> rfcs = new HashMap<>();

    private int clientUploadPort;

    public static void main(String[] args) {
        new Client();
    }

    private Client() {
        for (int i = 0; i < 5; i++){
            RFC rfc = new RFC(i);
            rfc.setTitle(CommonConstants.rfcTitle[i]);
            rfcs.put(i,rfc);
        }
        try {
            Socket socket = new Socket(HOST_IP, PORT_NUMBER);
            dOutToServer = new DataOutputStream(socket.getOutputStream());
            dInFromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        clientUploadPort = getClientUploadPort();
        try {
            startUploadServer(clientUploadPort);
            sendClientInfoToServer(clientUploadPort);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("1");

        while (true) {
            String userRequest = scanner.nextLine();
            if (userRequest.equals("exit")
                    || userRequest.equals("quit")
                    || userRequest.equals("q")) break;
            handleUserRequest(userRequest);

        }
    }

    private void sendClientInfoToServer(int clientUploadPort) throws Exception {
        StringBuilder builder = new StringBuilder("");
        builder.append("UPLOAD_SERVER_PORT").append(" ").append(clientUploadPort).append("\n");
        builder = addCurrentRfcAddDetailsToBuilder(builder);
        if (null != builder) sendRequestToServer(builder.toString());
    }

    private StringBuilder addCurrentRfcAddDetailsToBuilder(StringBuilder builder) {
        for (Integer key : rfcs.keySet()){
            RFC rfc = rfcs.get(key);
            builder.append("ADD ").append("RFC ").append(key).append(" ").append(VERSION).append("\n");
            builder.append("Host: ").append(HOST_IP).append("\n");
            builder.append("Port: ").append(clientUploadPort).append("\n");
            builder.append("Title: ").append(rfc.getTitle()).append("\n");
        }
        return builder;
    }

    private void startUploadServer(int clientUploadPort) throws Exception{
        new UploadServer(clientUploadPort, this);
    }


    private void handleUserRequest(String userRequest) {
        StringBuilder builder = new StringBuilder(userRequest).append("\n");
        String nextLine;
        while ((nextLine = scanner.nextLine()) != null){
            builder.append(nextLine).append("\n");
            if (nextLine.equals("")) break;
        }
        try {
            sendRequestToServer(builder.toString());
            String result = dInFromServer.readUTF();
            System.out.println("\n" + result + "\n");
        } catch (IOException e) {
            //TODO remove stack trace
            e.printStackTrace();
            System.out.println("ERROR SENDING REQUEST TO SERVER.");
        }
    }

    private void sendRequestToServer(String userRequest) throws IOException {
        System.out.println("Sending request to server:\n" + userRequest);
        dOutToServer.writeUTF(userRequest);
    }


    private int getClientUploadPort() {
        try {
            dOutToServer.writeUTF("GET_NEXT_AVAILABLE_PORT");
            return Integer.parseInt(dInFromServer.readUTF());
        } catch (IOException e) {
            System.out.println("Error in getClientUploadPort");
            e.printStackTrace();
            return -1;
        }
    }
}

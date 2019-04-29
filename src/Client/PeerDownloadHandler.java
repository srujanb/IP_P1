package Client;


import Client.Modals.RFC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerDownloadHandler extends Thread {


    private final String userRequest;
    DataOutputStream dOutToPeer;
    DataInputStream dInFromPeer;

    private int RFCNumber;

    public PeerDownloadHandler(String userRequest) {
        this.userRequest = userRequest;
        start();
    }

    @Override
    public void run() {
        super.run();
        String[] lines =  userRequest.split("\n");
        String[] firstLineTokens =  lines[0].split(" ");
        String[] secondLineTokens =  lines[1].split(" ");
        String[] fourthLineTokens =  lines[3].split(" ");

        String hostIp = secondLineTokens[1];
        int hostPort = Integer.parseInt(fourthLineTokens[1]);
        RFCNumber = Integer.parseInt(firstLineTokens[2]);

        try {
            Socket socket = new Socket(hostIp, hostPort);
            dOutToPeer = new DataOutputStream(socket.getOutputStream());
            dInFromPeer = new DataInputStream(socket.getInputStream());

            dOutToPeer.writeUTF(userRequest);
            String response = dInFromPeer.readUTF();
            handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(String response) {
        //TODO for loop starting point appropriate.
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder("");
        for (int i = 6; i < lines.length;  i++){
            builder.append(lines[i]).append("\n");
        }
        RFC  rfc =  new RFC(RFCNumber);
        rfc.setFileContent(builder.toString());
    }
}

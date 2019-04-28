package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerUploadHandler extends Thread {

    private DataInputStream dIn;
    private DataOutputStream dOut;

    private Client callingClient;

    PeerUploadHandler(Socket socket, Client callingClient) {
        this.callingClient = callingClient;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            dOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        start();
    }

    @Override
    public void run() {
        super.run();
        try {
            String request = dIn.readUTF();
            String[] lines = request.split("\n");
            String firstLine = lines[0];
            String[] firstLineTokens = firstLine.split(" ");
            String requestType = firstLineTokens[0];
            String requestItem = firstLineTokens[1];

            String result = null;
            if (requestType.equals("GET"))
                if (requestItem.equals("RFC"))
                    result = callingClient.rfcs.get(Integer.parseInt(firstLineTokens[2])).getFileContent();

            //TODO handle request to send file.
            dOut.writeUTF("Here is the file : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

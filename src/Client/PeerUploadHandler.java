package Client;

import Utils.CommonConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

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

            int rfcNumber = Integer.parseInt(firstLineTokens[2]);
            String result = null;
            if (requestType.equals("GET")) {
                if (requestItem.equals("RFC")) {
                    result = callingClient.rfcs.get(rfcNumber).getFileContent();
                } else {
                    badRequestError();
                    return;
                }
            } else {
                badRequestError();
                return;
            }

            StringBuilder builder = new StringBuilder("");
            builder.append(CommonConstants.VERSION)
                    .append(" ")
                    .append("200 OK")
                    .append("\n")
                    .append("Date: ")
                    .append(new Date())
                    .append("\n")
                    .append("OS: ")
                    .append(System.getProperty("os.name"))
                    .append("\n")
                    .append("Last-Modified: ")
                    .append(callingClient.rfcs.get(rfcNumber).getLastModified())
                    .append("\n")
                    .append("Content-Length: ")
                    .append(String.valueOf(callingClient.rfcs.get(rfcNumber).getFileSize()))
                    .append("\n")
                    .append("Content-type: text/text \n" )
                    .append(result);
            dOut.writeUTF(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void badRequestError() {
        try {
            StringBuilder builder = new StringBuilder("");
            builder.append(CommonConstants.VERSION)
                    .append(" ")
                    .append("200 OK")
                    .append("\n")
                    .append("Date: ")
                    .append(new Date())
                    .append("\n")
                    .append("OS: ")
                    .append(System.getProperty("os.name"))
                    .append("\n");
            dOut.writeUTF(builder.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}

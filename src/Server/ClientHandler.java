package Server;

import Server.Modals.RFC;
import Utils.CommonConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket socket;
    DataOutputStream dOutToClient;
    DataInputStream dInFromClient;

    public ClientHandler(Socket socket) throws Exception {
        this.socket = socket;
        dOutToClient = new DataOutputStream(socket.getOutputStream());
        dInFromClient = new DataInputStream(socket.getInputStream());

        start();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                String clientRequest = dInFromClient.readUTF();
                handleRequest(clientRequest);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Couldn't handle client request.");

            }
        }
    }

    private void handleRequest(String clientRequestBundle) throws Exception {
        System.out.println("Client request: \n" + clientRequestBundle);
        String[] lines = clientRequestBundle.split("\n");
        String[] firstLinesTokens = lines[0].split(" ");

        int linesForThisRequest = 1;
        if (firstLinesTokens[0].equals("GET_NEXT_AVAILABLE_PORT")) {
            linesForThisRequest = 1;
            sendMessage(String.valueOf(++MyServer.NEXT_AVAILABLE_PORT));
        } else if (firstLinesTokens[0].equals("ADD")) {
            if (firstLinesTokens[1].equals("RFC")) {
                linesForThisRequest = 4;
                RFC rfc = new RFC();
                int rfcNumber = Integer.valueOf(firstLinesTokens[2]);
                rfc.setNumber(rfcNumber);
                rfc.setTitle(CommonConstants.rfcTitle[rfcNumber]);

                String[] secondLinesTokens = lines[1].split(" ");
                rfc.setHost(secondLinesTokens[1]);

                String[] thirdLineTokens = lines[2].split(" ");
                rfc.setUploadPort(Integer.parseInt(thirdLineTokens[1]));

                MyServer.rfcs.add(rfc);

                System.out.println("rfcs size: " + MyServer.rfcs.size());
            }
        } else if (firstLinesTokens[0].equals("LOOKUP")) {
            linesForThisRequest = 4;
            if (!firstLinesTokens[3].equals(CommonConstants.VERSION)){
                sendMessage(CommonConstants.VERSION + " 505 P2P-CI Version Not Supported ");
                return;
            }
            int rfcNumber = Integer.parseInt(firstLinesTokens[2]);
            String title = lines[3].split(" ")[1];
            StringBuilder builder = new StringBuilder("");
            System.out.println("RFCs: " + MyServer.rfcs.size());
            for (RFC rfc : MyServer.rfcs) {
                if (rfc.getNumber() == rfcNumber && rfc.getTitle().equals(title)) {
                    builder.append("RFC")
                            .append(" ")
                            .append(rfcNumber)
                            .append(" ")
                            .append(rfc.getHost())
                            .append(" ")
                            .append(rfc.getUploadPort())
                            .append("\n");
                }
            }
            if (!builder.toString().equals("")) {
                sendMessage(CommonConstants.VERSION + " 200 OK\n" + builder.toString());
            } else {
                sendMessage("");
            }
        } else if (firstLinesTokens[0].equals("LIST")) {
            if (firstLinesTokens[1].equals("ALL")) {
                linesForThisRequest = 3;
                StringBuilder builder = new StringBuilder("");
                for (RFC rfc : MyServer.rfcs) {
                    builder.append("RFC")
                            .append(" ")
                            .append(rfc.getNumber())
                            .append(" ")
                            .append(rfc.getHost())
                            .append(" ")
                            .append(rfc.getUploadPort())
                            .append("\n");
                }
                if (!builder.toString().equals("")){
                    sendMessage(CommonConstants.VERSION + " 200 OK\n"  + builder.toString());
                } else {
                    sendMessage("");
                }
            }
        }
//        else {
//            sendMessage(CommonConstants.VERSION + " 400 Bad Request ");
//        }

        //call handleRequest again with the remaining command
        StringBuilder remainingCommand = new StringBuilder("");
        for (int i = linesForThisRequest; i < lines.length; i++) {
            remainingCommand.append(lines[i]).append("\n");
        }
        if (!remainingCommand.toString().equals("") && !remainingCommand.toString().equals("\n"))
            handleRequest(remainingCommand.toString());
    }

    private void sendMessage(String s) {
        if (s.equals("")) {
            try {
                dOutToClient.writeUTF(CommonConstants.VERSION + " 404 Not Found ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//            System.out.println("Sending message to client:\n" + s);
            try {
                dOutToClient.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

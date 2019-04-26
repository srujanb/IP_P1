package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket socket;
    DataOutputStream dOutToClient;
    DataInputStream dInFromClient;

    public ClientHandler(Socket socket) throws Exception{
        this.socket = socket;
        dOutToClient = new DataOutputStream(socket.getOutputStream());
        dInFromClient = new DataInputStream(socket.getInputStream());

        start();
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                String clientRequest = dInFromClient.readUTF();
                handleRequest(clientRequest);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(String clientRequest) {
        System.out.println("Client request: " + clientRequest);
    }
}

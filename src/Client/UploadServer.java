package Client;


import java.net.ServerSocket;

public class UploadServer extends Thread {

    private int PORT_NUMBER;
    private Client callingClient;

    public UploadServer(int clientUploadPort, Client  callingClient) throws Exception{
        PORT_NUMBER = clientUploadPort;
        this.callingClient = callingClient;
        start();
    }

    @Override
    public void run() {
        super.run();
        try {
            try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
                System.out.println("Upload server started.\n");
                while (true) {
                    new PeerUploadHandler(serverSocket.accept(), callingClient);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

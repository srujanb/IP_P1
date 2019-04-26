package Server;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private static final int PORT_NUMBER = 7734;
    static List<ClientHandler> peers;

    public static void main(String[] args) throws Exception{
        peers = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            while (true) {
                peers.add(new ClientHandler(serverSocket.accept()));
            }
        }
    }
}

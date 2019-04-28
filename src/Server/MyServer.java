package Server;

import Server.Modals.RFC;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private static final int PORT_NUMBER = 7734;
    public static int NEXT_AVAILABLE_PORT = PORT_NUMBER;
    static List<ClientHandler> peers;

    static List<RFC> rfcs;

    public static void main(String[] args) throws Exception{
        peers = new ArrayList<>();
        rfcs = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started.\n");
            while (true) {
                peers.add(new ClientHandler(serverSocket.accept()));
                System.out.println("Client connected");
            }
        }
    }

//    public class CliManager extends Thread {}
}

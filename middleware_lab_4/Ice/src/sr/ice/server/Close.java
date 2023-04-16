package sr.ice.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Close {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10000);
        serverSocket.close();
    }
}

package server;

import client.Client;
import text.TextColoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class TcpServer implements Runnable{
    private final List<Socket> allClientsSockets;
    private final Socket socket;
    private final int id;

    public TcpServer(List<Socket> allClientsSockets, Socket socket) {
        this.allClientsSockets = allClientsSockets;
        this.socket = socket;
        this.id = socket.getPort();
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String msg = in.readLine();

                if (msg == null || msg.equals(Client.CLOSE_CONNECTION)){
//                    System.out.println("CLIENT DISCONNECTED: " + id);
                    TextColoring.printClientDisconnected("CLIENT DISCONNECTED: " + id);
                    for (Socket client : allClientsSockets){
                        if (!client.equals(socket)){
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                            out.println(id + ": " + "DISCONNECTED");
                        }
                    }
                    allClientsSockets.remove(socket);
                    in.close();
                    break;
                }

//                System.out.println(id + ": " + msg);
                TextColoring.printMsg("RECEIVED MSG [" + id + "]: \n" + msg);
                for (Socket client : allClientsSockets){
                    if (!client.equals(socket)){
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.println("[" + id + "]: " + msg);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
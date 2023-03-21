package client;


import text.TextColoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TcpClient implements Runnable{
    private final Socket socket;

    private ClientCommunication communication;


    public TcpClient(Socket socket, ClientCommunication communication) {
        this.socket = socket;
        this.communication = communication;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String response = in.readLine();

                if (response == null) {
                    communication.close();
                    socket.close();
                    break;
                }

//                System.out.println("[RECEIVED MESSAGE] " + response);
                TextColoring.printMsg("RECEIVED MSG " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
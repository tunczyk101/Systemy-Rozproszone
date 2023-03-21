package client;

import text.TextColoring;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;


public class MulticastClient implements Runnable{
    private final MulticastSocket multicastSocket;
    private final int clientPort;

    public MulticastClient(MulticastSocket multicastSocket, int clientPort) {
        this.multicastSocket = multicastSocket;
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        try{
            while (true){
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                multicastSocket.receive(receivePacket);

                // if from the current client then not outputting it
                if (receivePacket.getPort() == clientPort)
                    continue;

                // outputting the received message
                String msg = new String(receiveBuffer);

                TextColoring.printMsg("RECEIVED MSG " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (multicastSocket != null) {
                multicastSocket.close();
            }
        }
    }


    public void finish(){
        multicastSocket.close();

    }
}


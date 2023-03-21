package server;


import text.TextColoring;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;


public class MulticastServer implements Runnable{
    private final DatagramSocket multicastSocket;

    public MulticastServer(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }


    @Override
    public void run() {
        try{
            while (true){
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                multicastSocket.receive(receivePacket);
//                int id = receivePacket.getPort() - 1;

                String msg = new String(receivePacket.getData());

                TextColoring.printMsg("RECEIVED MSG " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

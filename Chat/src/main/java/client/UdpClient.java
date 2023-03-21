package client;

import text.TextColoring;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UdpClient implements Runnable{
    DatagramSocket socket;

    public UdpClient(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while(true) {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String udpMsg = new String(receiveBuffer);

                TextColoring.printMsg("RECEIVED MSG " + "\n" + udpMsg);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    public void finish(){
        socket.close();
    }
}

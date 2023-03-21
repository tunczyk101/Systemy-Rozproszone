package server;

import text.TextColoring;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;


public class UdpServer implements Runnable{
    private final List<Socket> allClientsSockets;
    private final DatagramSocket socket;

    public UdpServer(List<Socket> allClientsSockets, DatagramSocket socket) {
        this.allClientsSockets = allClientsSockets;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName("localhost");

            while (true) {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                int id = receivePacket.getPort() - 1;
                String msg = new String(receivePacket.getData());

                TextColoring.printMsg("RECEIVED MSG [" + (id) + "]:\n" + msg);
                msg = "[" + id + "]: \n" + msg;

                byte[] sendBuffer = msg.getBytes();
                for (Socket client : allClientsSockets){
                    if (client.getPort() != id){
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, client.getPort() + 1);
                        socket.send(sendPacket);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package client;

import text.TextColoring;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class Client {
    public static String CLOSE_CONNECTION = "__Close Connection__";

    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 12345;
        int multicastPortNumber = 1234;
        Socket tcpSocket;


        try {
            // create tcp socket
            tcpSocket = new Socket(hostName, portNumber);
            int id = tcpSocket.getLocalPort();

            TextColoring.printNewClient("CLIENT " + id);

            // create udp socket
            InetAddress address = InetAddress.getByName(hostName);
            DatagramSocket udpSocket = new DatagramSocket(id + 1);
            UdpClient udpClient = new UdpClient(udpSocket);

            MulticastSocket multicastSocket = new MulticastSocket(multicastPortNumber);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            multicastSocket.joinGroup(group);
            MulticastClient multicastClient = new MulticastClient(multicastSocket, id + 1);

            // communication from client to send
            ClientCommunication communication = new ClientCommunication(id, portNumber, tcpSocket, udpClient, udpSocket,
                    address, multicastClient, group, multicastPortNumber);

            TcpClient tcpClient = new TcpClient(tcpSocket, communication);

            new Thread(communication).start();
            new Thread(tcpClient).start();
            new Thread(multicastClient).start();
            new Thread(udpClient).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
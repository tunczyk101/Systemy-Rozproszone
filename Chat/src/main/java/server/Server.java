package server;


import text.TextColoring;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class Server {
        public static void main(String[] args) {
        System.out.println("JAVA SERVER");

        int portNumber = 12345;
        int multicastPortNumber = 1234;

        ServerSocket tcpServerSocket =null;
        DatagramSocket udpServerSocket = null;
        MulticastSocket multicastSocket = null;



            try {
            // create tcpsocket
            tcpServerSocket = new ServerSocket(portNumber);

            // clients' list
            List<Socket> clients = new LinkedList<>();

            // create udpsocket
            udpServerSocket = new DatagramSocket(portNumber);
            UdpServer udpServer = new UdpServer(clients, udpServerSocket);
            new Thread(udpServer).start();

            // create multicastsocket
            multicastSocket = new MulticastSocket(multicastPortNumber);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            multicastSocket.joinGroup(group);
            MulticastServer multicastServer = new MulticastServer(multicastSocket);
            new Thread(multicastServer).start();

            while (true) {

                // accept client
                Socket clientSocket = tcpServerSocket.accept();
                clients.add(clientSocket);
    //                System.out.println("NEW CLIENT CONNECTED: " + clientSocket.getPort());
                TextColoring.printNewClient("NEW CLIENT CONNECTED: " + clientSocket.getPort());

                TcpServer tcpServer = new TcpServer(clients, clientSocket);
                new Thread(tcpServer).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            finally {
                try {
                    if (tcpServerSocket != null)
                        tcpServerSocket.close();
                    if (udpServerSocket != null)
                        udpServerSocket.close();
                    if (multicastSocket != null)
                        multicastSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}

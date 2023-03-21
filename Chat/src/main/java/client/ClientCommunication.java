package client;


import text.TextColoring;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * The client runnable sending messages to the server or to the clients using multicast messages.
 */
public class ClientCommunication implements Runnable{
    private final int id;
    private final int portNumber;

    private final Socket tcpSocket;

    private final UdpClient udpClient;
    private final DatagramSocket udpSocket;
    private final InetAddress address;

    private final MulticastClient multicastClient;
    private final InetAddress multicastAddress;
    private final int multicastPortNumber;

    private final Scanner scanner = new Scanner(System.in);

    public ClientCommunication(int id, int portNumber, Socket tcpSocket, UdpClient udpClient, DatagramSocket udpSocket, InetAddress address, MulticastClient multicastClient, InetAddress multicastAddress, int multicastPortNumber) {
        this.id = id;
        this.portNumber = portNumber;

        this.tcpSocket = tcpSocket;

        this.udpClient = udpClient;
        this.udpSocket = udpSocket;
        this.address = address;
        this.multicastClient = multicastClient;
        this.multicastAddress = multicastAddress;
        this.multicastPortNumber = multicastPortNumber;
    }

    @Override
    public void run() {
        try {

            PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
            while (true) {
                System.out.println("Write the message (u - udp, m - multicast):");
                String msg = scanner.nextLine();
                if (msg.equals("q")) {
                    out.println(Client.CLOSE_CONNECTION);
                    System.out.println("Chat closed");
                    this.close();
                    break;
                }
                else if (msg.equals("u")){
                    System.out.println("blank line - end of msg");
                    String udpMsg = getMsg();
                    if (!udpMsg.isEmpty()){
                        byte[] sendBuffer = udpMsg.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
                        udpSocket.send(sendPacket);
                    }
                }
                else if (msg.equals("m")){
                    System.out.println("blank line - end of msg");
                    String mMsg = getMsg();
                    if (!mMsg.isEmpty()){
                        mMsg = "[" + id + "]:\n" + mMsg;
                        byte[] sendBuffer = mMsg.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, multicastAddress, multicastPortNumber);
                        udpSocket.send(sendPacket);
                    }
                }
                else if (!msg.isEmpty())
                    out.println(msg);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalStateException e){
            TextColoring.printClientDisconnected("[SERVER DISCONNECTED]");
        }
    }

    private String getMsg() {
        LinkedList<String> msg = new LinkedList<>();
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            if (s.isEmpty())
                break;
            msg.add(s);
        }
        return String.join("\n", msg);
    }

    public void close() {

//        this.udpClient.finish();
//        System.out.println("here");
//        this.multicastClient.finish();
        this.scanner.close();
//        this.udpClient.finish();
    }

}


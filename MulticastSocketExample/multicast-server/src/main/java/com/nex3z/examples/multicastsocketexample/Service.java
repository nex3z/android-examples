package com.nex3z.examples.multicastsocketexample;

import java.io.IOException;
import java.net.*;

public class Service {
    private static final String ADDR = "225.0.0.3";
    private static final int PORT = 8888;
    private static final String ACK = "ACK ";
    private static final int BUF_SIZE = 256;

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        InetAddress address = InetAddress.getByName(ADDR);
        try {
            MulticastSocket socket = new MulticastSocket(PORT);
            socket.joinGroup(address);

            byte[] buf = new byte[BUF_SIZE];
            while (true) {
                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
                socket.receive(receivedPacket);
                String received = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                if (!received.startsWith(ACK)) {
                    System.out.println("Service received: " + received);

                    String send = ACK + received;
                    DatagramPacket sendPacket = new DatagramPacket(send.getBytes(), send.getBytes().length, address, PORT);
                    socket.send(sendPacket);
                    System.out.println("Service sent: " + send);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

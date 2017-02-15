package com.nex3z.examples.multicastsocketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {
    final static String ADDR = "225.0.0.3";
    final static int PORT = 8888;

    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getByName(INET_ADDR);

        byte[] buf = new byte[256];

        try (MulticastSocket socket = new MulticastSocket(PORT)){
            socket.joinGroup(address);

            String sendMsg = "Hello";
            DatagramPacket sendPacket = new DatagramPacket(sendMsg.getBytes(),
                    sendMsg.getBytes().length, address, PORT);
            socket.send(sendPacket);

            while (true) {
                System.out.println("Client waiting");
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                socket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Client received msg: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

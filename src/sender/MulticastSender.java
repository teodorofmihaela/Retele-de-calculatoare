package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;

public class MulticastSender {
    static volatile boolean finished = false;

    public static void main(String[] args) {
        try {
            String hostStartString = "234.0.0.";
            int numberOfHosts = 7;
            HashMap<Integer, String> hosts = new HashMap<>();

            for (int i = 1; i <= numberOfHosts; i++) {
                String newHost = hostStartString + i;
                hosts.put(i, newHost);
            }

            String host;

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Introdu numarul camerei pe care vrei sa o accesezi:");

                int camera = scanner.nextInt();
                host = hosts.get(camera);
                System.out.println("Camera " + camera);

                MulticastSocket socket = new MulticastSocket();
                socket.setTimeToLive(0);
                InetAddress group = InetAddress.getByName(host);

                socket.joinGroup(group);

//                Thread t = new Thread(new ReadThread(socket, group, 8080));
//                t.start();

                while (true) {
                    String command = scanner.nextLine();
                    byte[] buffer = command.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 8080);
                    socket.send(packet);
                    if (command == null || "exit".equalsIgnoreCase(command)) {
                        finished = true;
                        socket.leaveGroup(group);
                        socket.close();
                        break;
                    }
                }

            }
        } catch (SocketException se) {
            System.out.println("Error creating socket");
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }
}
//
//class ReadThread implements Runnable {
//    private MulticastSocket socket;
//    private InetAddress group;
//    private int port;
//    private static final int MAX_LEN = 512;
//
//    ReadThread(MulticastSocket socket, InetAddress group, int port) {
//        this.socket = socket;
//        this.group = group;
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        while (!MulticastSender.finished) {
//            byte[] buffer = new byte[ReadThread.MAX_LEN];
//            DatagramPacket datagram = new
//                    DatagramPacket(buffer, buffer.length, group, port);
//            String message;
//            try {
//                socket.receive(datagram);
//                message = new
//                        String(buffer, 0, datagram.getLength(), "UTF-8");
//                System.out.println(message);
//            } catch (IOException e) {
//                System.out.println("Socket closed!");
//            }
//        }
//    }
//}
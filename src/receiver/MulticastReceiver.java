package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Scanner;

public class MulticastReceiver {

    public static void main(String[] args) throws IOException {
        int numberOfHosts = 7;
        HashMap<Integer, String> hosts = new HashMap<>();

        for (int i = 1; i <= numberOfHosts; i++) {
            String newHost = "234.0.0." + i;
            hosts.put(i, newHost);
        }

        String host;
        MulticastSocket socket = new MulticastSocket(8080);
        byte[] buffer = new byte[512];

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Introdu numarul camerei pe care vrei sa o accesezi:");
            int camera = scanner.nextInt();
            host = hosts.get(camera);
            System.out.println("Camera " + camera);
        }

            InetAddress group = InetAddress.getByName(host);
        socket.joinGroup(group);

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(buffer, 0, packet.getLength());
            System.out.println(message);
            System.out.println("Message Camera ?" );
            if ("exit".equals(message)) {
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
    }

}
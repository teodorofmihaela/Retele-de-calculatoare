package receiver;

import java.net.*;
import java.io.*;
import java.util.*;
public class AplicatieChat23
{
    private static final String comandaIesire = "exit";
    static String name;
    static volatile boolean finished = false;
//    Map<Integer, String> mapNumeCamere=new HashMap<>();

//    public void numeCamera() {
//        mapNumeCamere.put(1, "1 girafe");
//        mapNumeCamere.put(2, "2 zebre");
//        mapNumeCamere.put(3, "3 SERVER ACCESS FORBIDDEN");
//        mapNumeCamere.put(4, "4 space");
//        mapNumeCamere.put(5, "5 wonders");
//        mapNumeCamere.put(6, "6 binary");
//        mapNumeCamere.put(7, "7 parkour");
//        System.out.println("Lista camerelor disponibile: ");
//        for (var camera : mapNumeCamere.entrySet()) {
//            System.out.println(camera.getValue());
//        }
//    }
//    public Map creareIpCamere(int nrHosts){
//        String hostStartString = "234.0.0.";
//        int numberOfHosts = nrHosts;
//        HashMap<Integer, String> hosts = new HashMap<>();
//
//        for (int i = 1; i <= numberOfHosts; i++) {
//            String newHost = hostStartString + i;
//            hosts.put(i, newHost);
//        }
//        return hosts;
//    }

    public static void main(String[] args)
    {
        AplicatieChat23 app= new AplicatieChat23();
        List <Integer> listaCamere=new ArrayList<>();

            try
            {
//                String hostStartString = "234.0.0.";
//                int numberOfHosts = 7;
//                HashMap<Integer, String> hosts = new HashMap<>();
//
//                for (int i = 1; i <= numberOfHosts; i++) {
//                    String newHost = hostStartString + i;
//                    hosts.put(i, newHost);
//                }
                int nrCamere=7;
                String hostStartString = "234.0.0.";
                int numberOfHosts = nrCamere;
                HashMap<Integer, String> hosts = new HashMap<>();

                for (int i = 1; i <= numberOfHosts; i++) {
                    String newHost = hostStartString + i;
                    hosts.put(i, newHost);
                }
//                var creareCamere=app.creareIpCamere(nrCamere);
                String host;

                Map<Integer, String> mapNumeCamere=new HashMap<>();
                mapNumeCamere.put(1, "1 girafe");
                mapNumeCamere.put(2, "2 zebre");
                mapNumeCamere.put(3, "3 SERVER ACCESS FORBIDDEN");
                mapNumeCamere.put(4, "4 space");
                mapNumeCamere.put(5, "5 wonders");
                mapNumeCamere.put(6, "6 binary");
                mapNumeCamere.put(7, "7 parkour");




                Scanner sc = new Scanner(System.in);
                System.out.print("Introdu numele tau: ");
                name = sc.next();
//                app.numeCamera();
                System.out.println("Lista camerelor disponibile: ");
                for (var camera : mapNumeCamere.entrySet()) {
                    System.out.println(camera.getValue());
                }

                System.out.println("Introdu numarul camerei pe care vrei sa o accesezi:");
                int camera = sc.nextInt();
                host =  hosts.get(camera);
                System.out.println("Camera " + mapNumeCamere.getOrDefault(camera,"NU EXISTA CAMERA SELECTATA"));
                System.out.println("IP HOST: " + host);

                int port = 8080;
                MulticastSocket socket = new MulticastSocket(port);
                InetAddress group = InetAddress.getByName(host);
                socket.joinGroup(group);



                // Since we are deploying
                socket.setTimeToLive(0);
                //this on localhost only (For a subnet set it as 1)

                Thread t = new Thread(new
                        ReadThread(socket,group,port));

                // Spawn a thread for reading messages
                t.start();

                // sent to the current group
                System.out.println("Scrie mesajul tau aici...\n");
                while(true)
                {
                    System.out.println("Meniu:\n 1 Creeaza o noua camera\n 2 Sterge o camera\n 3 Afisare lista camere \n 4 Intra intr-o camera\n " +
                            "5 Parasire camera\n");
                    int identificator= sc.nextInt();
//                    System.out.println(identificator);
//                    switch (identificator){
//                        case 1:
//                            System.out.println("Creaza camera noua");
//                            int CameraNoua=nrCamere++;
////                            app.creareIpCamere(CameraNoua);
//                            break;
//                        case 2:
//                            System.out.println("Sterge o camera");
//                            break;
//                        case 3:
//                            System.out.println("Afisare lista camere");
//                            break;
//                        case 4:
//                            System.out.println("Intra intr-o camera noua");
//                            break;
//                        case 5:
//                            System.out.println("Parasire camera ");
//                            break;
//                    }
                    String message;
                    message = sc.nextLine();
                    System.out.println("Mesaj trimis:"+name+":"+message);
                    if(message.equalsIgnoreCase(AplicatieChat23.comandaIesire))
                    {
                        finished = true;
                        socket.leaveGroup(group);
                        socket.close();
                        break;
                    }
                    message = name + ": " + message;
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port);
                    socket.send(datagram);
                }
            }
            catch(SocketException se)
            {
                System.out.println("Error creating socket");
                se.printStackTrace();
            }
            catch(IOException ie)
            {
                System.out.println("Error reading/writing from/to socket");
                ie.printStackTrace();
            }

    }
}
class ReadThread implements Runnable
{
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;
    ReadThread(MulticastSocket socket,InetAddress group,int port)
    {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run()
    {
        while(!AplicatieChat23.finished)
        {
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port);
            String message;
            try
            {
                socket.receive(datagram);
                message = new
                        String(buffer,0,datagram.getLength(),"UTF-8");
                if(!message.startsWith(AplicatieChat23.name))
                    System.out.println(message);
            }
            catch(IOException e)
            {
                System.out.println("Socket closed!");
            }
        }
    }
}

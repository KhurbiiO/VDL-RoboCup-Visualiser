package refebox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args)  {
       try{
        InetAddress multiAddr1 = InetAddress.getByName("239.255.0.1");
        InetAddress multiAddr2 = InetAddress.getByName("239.255.0.2");

        int port = 80;

        DataStream dataStream1 = new DataStream("src/main/resources/20180618_085311.A.msl",multiAddr1, 1025);
        DataStream dataStream2 = new DataStream("src/main/resources/20180618_085311.B.msl",multiAddr2, 80);

        dataStream1.start();
       // dataStream2.start();
       }
       catch(Exception e)
       {
        System.out.println(e);
       }
        // server.start();

        // Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        // while (networkInterfaces.hasMoreElements()) {
        //     NetworkInterface networkInterface = networkInterfaces.nextElement();

        //     System.out.println("Name: " + networkInterface.getName());
        //     System.out.println("Display Name: " + networkInterface.getDisplayName());
        //     System.out.println("Is up? " + networkInterface.isUp());
        //     System.out.println("Is loopback? " + networkInterface.isLoopback());
        //     System.out.println("Is virtual? " + networkInterface.isVirtual());

        //     Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        //     while (inetAddresses.hasMoreElements()) {
        //         InetAddress inetAddress = inetAddresses.nextElement();
        //         System.out.println("  InetAddress: " + inetAddress.getHostAddress());
        //     }

        //     System.out.println("--------------------------");
        // }
    }
}
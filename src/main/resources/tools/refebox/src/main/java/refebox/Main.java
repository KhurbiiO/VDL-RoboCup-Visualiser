package refebox;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args)  {
       try{
        String muticastAddress = "239.255.0.0";

        RefexBoxServer server = new RefexBoxServer(muticastAddress, 80);
        server.start();
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
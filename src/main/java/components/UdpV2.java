package visualiser;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONObject;

public class Udp {

    // public interface StreamIO {
    // void PushData(String data);

    // String PullData();
    // }

    // public class HeartbeatTracker extends Threads {

    // }

    public class UdpStream extends Thread {
        ////////////// property ////////////////////
        // udp unicast and multicast sockets
        DatagramSocket ds;
        MulticastSocket ms;

        // refe information
        InetAddress refeIp;
        int refePort;

        // keeping track on heartbeat data
        long startTime;
        long currentTime;
        long interval;

        // receiving data from the refe box
        Queue<String> dataQueue;

        /////////////// constructors ////////////////
        public UdpStream(int dsPort, String refeIp, int refePort)
                throws IOException, InterruptedException {
            this.refeIp = InetAddress.getByName(refeIp);
            this.refePort = refePort;

            this.dataQueue = new ConcurrentLinkedQueue<String>();

            ds = new DatagramSocket(dsPort);
            ms = new MulticastSocket();

            Thread.sleep(2000);
            OpenRequest("team-1");

        }

        ///////////////// methods /////////////////////
        public void OpenRequest(String requestData) throws IOException {

            // general message
            JSONObject message = new JSONObject();
            message.put("subject", "visualiser");
            message.put("type", "open-stream");
            message.put("request-data", requestData);

            // serialise the message and send to the refbox
            byte[] buf = message.toString().getBytes();

            DatagramPacket dpSend = new DatagramPacket(buf, buf.length, refeIp, refePort);
            ms.send(dpSend);
        }

        public JSONObject RecieveData() throws IOException {
            DatagramPacket dpReceive = null;
            byte[] receive = new byte[65535];

            // receiving the incoming data/message
            dpReceive = new DatagramPacket(receive, receive.length);
            ds.receive(dpReceive);

            String sData = new String(receive, 0, receive.length);
            System.out.println(sData);
            JSONObject jsData = new JSONObject(sData);

            return jsData;
        }

        public void PushData(String data) throws InterruptedException {
            dataQueue.add(data);
        }

        public String PullData() throws InterruptedException {
            // mutex
            String sData = new String("none");
            if (dataQueue.size() > 0) {
                sData = dataQueue.poll();
            }
            return sData;
        }

        // TODO send json open and handle disconneted error
        /*
         * 
         */
        @Override
        public void run() {
            System.out.println("HELLO FROM UDP VISUALISER!");

            while (true) {
                try {
                    JSONObject data = RecieveData();
                    // add check data
                    System.out.println("receive data");

                    System.out.println(data.toString());
                    PushData(data.toString());

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }

        StringBuilder ret = new StringBuilder();
        int i = 0;

        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }

        return ret;
    }
}

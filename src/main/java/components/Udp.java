package components;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class Udp {

    public class UdpStream extends Thread implements AutoCloseable {
        ////////////// property ////////////////////
        // udp unicast and multicast sockets
        MulticastSocket ms;
        boolean isOpen;

        // refe information
        InetAddress multiIp;
        int multiPort;

        // receiving data from the refe box
        Queue<String> dataQueue;

        /////////////// constructors ////////////////
        public UdpStream(int dsPort, String multiIp, int multiPort) {
            try {
                this.multiPort = multiPort;

                this.dataQueue = new ConcurrentLinkedQueue<String>();
                this.isOpen = false;

                ms = new MulticastSocket(multiPort);
                ms.joinGroup(InetAddress.getByName(multiIp));

            } catch (IOException e) {
                System.err.println("open socket fail!");
            }

        }

        @Override
        public void close() {
            if (ms != null || ms.isClosed()) {
                try {
                    ms.leaveGroup(multiIp);
                    ms.close();
                } catch (IOException e) {

                }
            }
        }

        ///////////////// methods /////////////////////

        public String RecieveData() throws IOException {
            DatagramPacket dpReceive = null;
            byte[] receive = new byte[65535];

            // receiving the incoming data/message
            dpReceive = new DatagramPacket(receive, receive.length);
            ms.receive(dpReceive);

            // convert to json object
            String sData = new String(receive, 0, receive.length);

            // only return to the recieving result if data is a valid json object
            if (IsValidJson(sData)) {
                return sData;
            }

            return null;
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

        @Override
        public void run() {
            System.out.println("HELLO FROM UDP VISUALISER!");

            while (true) {
                try {
                    String data = RecieveData(); // block thread

                    // only push the data into stream if the data is a valid json object
                    if (data != null) {
                        PushData(data);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

    }

    private static boolean IsValidJson(String data) {
        try {
            new JSONObject(data);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}

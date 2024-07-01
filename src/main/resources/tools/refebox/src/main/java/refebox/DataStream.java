package refebox;

import java.io.IOException;
import java.net.*;
import org.json.JSONObject;

public class DataStream extends Thread {
    /////////////////// fields ///////////////////////
    private DataObserver observer;
    private DatagramSocket ds;

    private InetAddress ip;
    private int port;

    ////////////////// constructor ////////////////
    public DataStream(String ip, int port, String dataSource) throws IOException {
        this.observer = new DataObserver(dataSource);
        this.ds = new DatagramSocket();
        this.ip = InetAddress.getByName(ip);
        this.port = port;
    }

    ///////////////// methods ////////////////////
    public boolean IsIdentical(String ip, int port) {
        return ip.toString().equals(ip) && this.port == port;
    }

    public void SendData(JSONObject data) throws IOException {
        // serialising the json object data into bytes
        String s_data = data.toString();
        byte buf[] = s_data.getBytes();
        System.out.println("send data: ");

        System.out.println(s_data);
        // packaging and sending the message buffer
        DatagramPacket dpSend = new DatagramPacket(buf, buf.length, ip, port);
        ds.send(dpSend);
    }

    // run thread
    @Override
    public void run()
    // stream send data for every 1 second
    {
        System.out.println("HELLO FROM DATA STREAM");
        while (true) {
            try {

                JSONObject data = observer.GetData();
                SendData(data);
                Thread.sleep(1000);
            } catch (IOException e) {

            } catch (InterruptedException e) {

            }
        }

    }

}

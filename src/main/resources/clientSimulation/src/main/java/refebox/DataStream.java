package refebox;

import java.io.IOException;
import java.net.*;
import org.json.JSONObject;

public class DataStream extends Thread {
    /////////////////// fields ///////////////////////
    private DataObserver observer;
    private MulticastSocket ms;

    // multicast destination
    InetAddress multiIp;
    int multiPort;

    ////////////////// constructor ////////////////
    public DataStream(String dataSource, InetAddress multiIp, int multiPort) throws IOException {
        this.observer = new DataObserver(dataSource);
        this.ms = new MulticastSocket();

        this.multiIp = multiIp;
        this.multiPort = multiPort;
    }

    ///////////////// methods ////////////////////
    public void SendData(JSONObject data) throws IOException {
        // serialising the json object into bytes
        String s_data = data.toString();
        byte buff[] = s_data.getBytes();
       
        //debugging - print send data
        System.out.println(s_data);
        // packaging and multicast the message
        DatagramPacket dpSend = new DatagramPacket(buff, buff.length, multiIp, multiPort);
        ms.send(dpSend);
       

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
                Thread.sleep(100);
            }
            catch (InterruptedException e) {

            }
            catch(IOException e)
            {

            }
        }

    }

}

package refebox;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import java.io.IOException;

/*
 *using multicast server-client architecture 
 */
public class RefexBoxServer extends Thread {
    ////////////// fields //////////////////////
    private MulticastSocket ms;
    private List<DataStream> dataStreams;

    ////////////// constructors ////////////////
    public RefexBoxServer(String listenGroupAddress, int port) throws IOException {
        // create the multicast socket and join the multicast group
        dataStreams = new ArrayList<DataStream>();
        ms = new MulticastSocket(port);
        ms.joinGroup(InetAddress.getByName(listenGroupAddress));

    }

    ////////////// methods ////////////////////
    // TODO improve the get data source logic -currently hardcodes
    private String GetDataSource(String dataType) {
        String filePath = null;
        if (dataType.equals("team-1")) {
            filePath = "src/main/resources/20180618_085311.A.msl";
        } else if (dataType.equals("team-2")) {
            filePath = "src/main/resources/20180618_085311.B.msl";
        }

        return filePath;
    }

    public boolean IsStreamExist(String ip, int port) {
        for (DataStream dataStream : dataStreams) {
            // if (dataStream.IsIdentical(ip, port)) {
            //     return true;
            // }
        }
        return false;
    }

    public JSONObject ReceiveMessage() throws IOException {
        byte[] buffer = new byte[1024];

        // receive the multicst message through the receiving package
        DatagramPacket dpReceive = new DatagramPacket(buffer, buffer.length);
        ms.receive(dpReceive);
        
        // convert to json object
        String sMessage = new String(dpReceive.getData(), 0, dpReceive.getLength());
        JSONObject message = new JSONObject(sMessage);

        message.put("ip", dpReceive.getAddress().toString().substring(1));

        System.out.println(message.toString());

        return message;
    }

    /*
     * !OPEN STREAM INFO FORMAT:
     * - ip:
     * - port:
     * - request-data:
     */
    public boolean OpenNewStream(String desIp, int desPort, String requestData) throws IOException {

        // // get the ip and port of the connected destination address
        // String ip = info.getString("ip");
        // int port = info.getInt("port");
        // String requestData = info.getString("request-data");

        // check if the stream is already open
        if (IsStreamExist(desIp, desPort)) {
            return false;
        }

        // create the new stream and start to transfer the data
        //DataStream stream = new DataStream(desIp, desPort, GetDataSource(requestData));
        // dataStreams.add(stream);
        // stream.start();
        
        

        return true;
    }

    public boolean HandleRequest(JSONObject request) throws JSONException, IOException {


        if (!request.has("type") || !request.has("ip")) {
            return false;
        }

        String type = request.getString("type");
        String ip = request.getString("ip");

        String requestData = request.getString("request-data");
        System.out.println(type);
        if (type.equals("open-stream")) {
            System.out.println(" ============ call the open new stream function ===========");
            OpenNewStream(ip, 70, requestData);
        }

        return true;
    }

    /*
     * !REQUEST MESSAGE FORMAT
     * - subject: visualiser
     * - type:
     * - data:
     */
    @Override
    public void run()
    // receive the receive request through udp multicast message
    {
        System.out.println("HELLO FROM SERVER CLASS!");
        while (true) {
            try {
                JSONObject message = ReceiveMessage();

                // only handle the message from the visualiser
                if (message.has("subject")) {
                    if (message.getString("subject").equals("visualiser")) {
                        HandleRequest(message);
                    }
                }
            } catch (JSONException e) {

            } catch (IOException e) {

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

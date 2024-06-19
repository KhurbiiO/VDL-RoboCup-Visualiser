package components;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.StandardSocketOptions;

/**
 * Create and manage unicast, broadcast, or multicast socket to send and receive
 * datagram packets over the network.
 *
 * The socket type is defined at its initialization by the passed IP address.
 * To reach a host (interface) within a network, specify the kind of address:
 * - Unicast: A unique host within a subnet.
 * - Broadcast: Calls every host within a subnet.
 * - Multicast: Calls a specific group of hosts within the subnet.
 *
 * @version 0.1
 */
public abstract class UDPClient implements Runnable {

    private DatagramSocket ucSocket = null;
    private MulticastSocket mcSocket = null;
    private boolean log = false;
    private boolean listen = false;
    private int timeout = 0;
    private int size = 65507;
    private InetAddress group = null;
    private Thread thread = null;
    private String header = "";

    /**
     * The default socket buffer length in bytes.
     */
    public static final int BUFFER_SIZE = 65507;

    /**
     * Create a new datagram socket and bind it to an available port and every 
     * address on the local host machine.
     *
     * @param port local port to bind
     */
    public UDPClient(int port) {
        this(port, null);
    }

    /**
     * Create a new datagram socket and bind it to the specified port on the 
     * specified local address or multicast group address.
     *
     * @param port local port to bind
     * @param ip   host address or group address
     */
    public UDPClient(int port, String ip) {
        // Open a new socket to the specified port/address and join the group if required
        try {
            InetAddress addr = (ip == null) ? null : InetAddress.getByName(ip);
            InetAddress host = (ip == null) ? null : addr;

            if (addr == null || !addr.isMulticastAddress()) {
                ucSocket = new DatagramSocket(port, host); // Unicast or Broadcast
                log("Bound socket to host: " + getAddress() + ", port: " + getPort());
            } else {
                mcSocket = new MulticastSocket(port); // Multicast
                mcSocket.joinGroup(addr);
                this.group = addr;
                log("Bound multicast socket to host: " + getAddress() + ", port: " + getPort() + ", group: " + group);
            }
        } catch (IOException | IllegalArgumentException | SecurityException e) {
            error("Opening socket failed! Address: " + ip + ", port: " + port + " [group: " + group + "] " + e.getMessage());
        }
    }

    /**
     * Close the current datagram socket and all associated resources.
     */
    public void close() {
        if (isClosed()) return;

        int port = getPort();
        String ip = getAddress();

        try {
            if (isMulticast()) {
                if (group != null) {
                    mcSocket.leaveGroup(group);
                    log("Left group: " + group);
                }
                mcSocket.close();
                mcSocket = null;
            } else {
                ucSocket.close();
                ucSocket = null;
            }
        } catch (IOException e) {
            error("Error while closing the socket! " + e.getMessage());
        } finally {
            log("Closed socket < port: " + port + ", address: " + ip + " >");
        }
    }

    /**
     * Check if the socket is closed.
     * @return boolean indicating if the socket is closed
     */
    public boolean isClosed() {
        if (isMulticast()) return mcSocket == null || mcSocket.isClosed();
        return ucSocket == null || ucSocket.isClosed();
    }

    /**
     * Get the current socket's local port, or -1 if the socket is closed.
     * @return the local port
     */
    public int getPort() {
        if (isClosed()) return -1;
        return isMulticast() ? mcSocket.getLocalPort() : ucSocket.getLocalPort();
    }

    /**
     * Get the current socket's local address, or null if the address corresponds to any local address.
     * @return the local address
     */
    public String getAddress() {
        if (isClosed()) return null;

        InetAddress localAddress = isMulticast() ? mcSocket.getLocalAddress() : ucSocket.getLocalAddress();
        return localAddress.isAnyLocalAddress() ? null : localAddress.getHostAddress();
    }

    /**
     * Send a message to the current socket. For multicast, it sends to the multicast group/port; otherwise, it sends to itself.
     * @param message the message to send
     * @return boolean indicating if the message was sent successfully
     */
    public boolean send(String message) {
        return send(message.getBytes());
    }

    /**
     * Send data to the current socket. For multicast, it sends to the multicast group/port; otherwise, it sends to itself.
     * @param data the data to send
     * @return boolean indicating if the data was sent successfully
     */
    public boolean send(byte[] data) {
        if (isMulticast() && group == null) return false;

        String ip = isMulticast() ? group.getHostAddress() : getAddress();
        return send(data, ip, getPort());
    }

    /**
     * Send a message to the specified IP address at the current socket port.
     * @param message the message to send
     * @param ip the destination IP address
     * @return boolean indicating if the message was sent successfully
     */
    public boolean send(String message, String ip) {
        return send(message.getBytes(), ip);
    }

    /**
     * Send data to the specified IP address at the current socket port.
     * @param data the data to send
     * @param ip the destination IP address
     * @return boolean indicating if the data was sent successfully
     */
    public boolean send(byte[] data, String ip) {
        return send(data, ip, getPort());
    }

    /**
     * Send a message to the specified IP address and port.
     * @param message the message to send
     * @param ip the destination IP address
     * @param port the destination port
     * @return boolean indicating if the message was sent successfully
     */
    public boolean send(String message, String ip, int port) {
        return send(message.getBytes(), ip, port);
    }

    /**
     * Send data to the specified IP address and port.
     * @param data the data to send
     * @param ip the destination IP address
     * @param port the destination port
     * @return boolean indicating if the data was sent successfully
     */
    public boolean send(byte[] data, String ip, int port) {
        boolean success = false;
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
            if (isMulticast()) {
                mcSocket.send(packet);
            } else {
                ucSocket.send(packet);
            }
            success = true;
            log("Sent packet to address: " + packet.getAddress() + ", port: " + packet.getPort() + ", length: " + packet.getLength());
        } catch (IOException e) {
            error("Could not send message! Address: " + ip + ", port: " + port + ", buffer size: " + size + ", packet length: " + data.length + ". " + e.getMessage());
        }
        return success;
    }

    /**
     * Set the socket buffer size.
     * @param size the buffer size in bytes
     * @return boolean indicating if the buffer size was set successfully
     */
    public boolean setBufferSize(int size) {
        if (isListening()) return false;

        try {
            int bufferSize = size > 0 ? size : BUFFER_SIZE;
            if (isMulticast()) {
                mcSocket.setSendBufferSize(bufferSize);
                mcSocket.setReceiveBufferSize(bufferSize);
            } else {
                ucSocket.setSendBufferSize(bufferSize);
                ucSocket.setReceiveBufferSize(bufferSize);
            }
            this.size = bufferSize;
            return true;
        } catch (SocketException e) {
            error("Could not set the buffer size! " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the current socket buffer size.
     * @return the buffer size
     */
    public int getBufferSize() {
        return size;
    }

    /**
     * Check if the socket is listening for incoming data.
     * @return boolean indicating if the socket is listening
     */
    public boolean isListening() {
        return listen;
    }

    /**
     * Start or stop listening for incoming data.
     * @param on true to start listening, false to stop
     */
    public void listen(boolean on) {
        listen = on;
        timeout = 0;

        if (on && thread == null && !isClosed()) {
            thread = new Thread(this);
            thread.start();
        } else if (!on && thread != null) {
            send(new byte[0]); // Unblock the thread with a dummy message
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * Set the socket reception timeout and wait once for incoming data. Calls the owner's timeout() method on timeout.
     * @param millis the timeout in milliseconds
     */
    public void listen(int millis) {
        if (isClosed()) return;

        listen = true;
        timeout = millis;

        if (thread != null) {
            send(new byte[0]); // Unblock the thread with a dummy message
        } else {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Wait for incoming data and call the appropriate handlers for received messages or timeout events.
     */
    public void listen() {
        try {
            byte[] buffer = new byte[size];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            if (isMulticast()) {
                mcSocket.setSoTimeout(timeout);
                mcSocket.receive(packet); // Block the thread
            } else {
                ucSocket.setSoTimeout(timeout);
                ucSocket.receive(packet); // Block the thread
            }

            log("Received packet from " + packet.getAddress() + ", port: " + packet.getPort() + ", length: " + packet.getLength());

            if (packet.getLength() != 0) {
                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, data, 0, data.length);
                onReceive(data, packet.getAddress().getHostAddress(), packet.getPort());
            }
        } catch (NullPointerException e) {
            listen = false;
            thread = null;
        } catch (IOException e) {
            listen = false;
            thread = null;

            if (e instanceof SocketTimeoutException) {
                onTimeout();
            } else if (ucSocket != null && mcSocket != null) {
                error("Listen failed! " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        while (listen) listen();
    }

    /**
     * @param data the received data
     * @param ip the sender IP address
     * @param port the sender port
     */
    protected abstract void onReceive(byte[] data, String ip, int port);

    /**
     * A method to handle timeout events.
     */
    protected abstract void onTimeout();

    /**
     * Check if the current socket is a multicast socket.
     * @return boolean indicating if the socket is multicast
     */
    public boolean isMulticast() {
        return mcSocket != null;
    }

    /**
     * Check if the multicast socket is joined to a group address.
     * @return boolean indicating if the socket is joined to a group
     */
    public boolean isJoined() {
        return group != null;
    }

    /**
     * Check if the socket sends broadcast messages.
     * @return boolean indicating if the socket is broadcast
     */
    public boolean isBroadcast() {
        try {
            return ucSocket != null && ucSocket.getBroadcast();
        } catch (SocketException e) {
            error(e.getMessage());
            return false;
        }
    }

    /**
     * Enable or disable the ability to send broadcast messages.
     * @param on true to enable broadcast, false to disable
     * @return boolean indicating if the broadcast setting was applied successfully
     */
    public boolean setBroadcast(boolean on) {
        try {
            if (ucSocket != null) {
                ucSocket.setBroadcast(on);
                return isBroadcast();
            }
        } catch (SocketException e) {
            error(e.getMessage());
        }
        return false;
    }

    /**
     * Enable or disable the multicast socket loopback mode.
     * @param on true to enable loopback, false to disable
     */
    public void setLoopback(boolean on) {
        try {
            if (isMulticast()) {
                mcSocket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, on);
            }
        } catch (IOException e) {
            error("Could not set the loopback mode! " + e.getMessage());
        }
    }

    /**
     * Check if the multicast socket loopback mode is enabled.
     * @return boolean indicating if the loopback mode is enabled
     */
    public boolean isLoopbackEnabled() {
        try {
            return isMulticast() && mcSocket.getOption(StandardSocketOptions.IP_MULTICAST_LOOP);
        } catch (IOException e) {
            error("Could not get the loopback mode! " + e.getMessage());
            return false;
        }
    }

    /**
     * Control the life-time of a datagram in the network for multicast packets 
     * in order to indicates the scope of the multicasts (ie how far the packet 
     * will travel).
     * <p>
     * The TTL value must be in range of 0 to 255 inclusive. The larger the 
     * number, the farther the multicast packets will travel (by convention):
     * <blockquote><pre>
     * 0    -> restricted to the same host
     * 1    -> restricted to the same subnet (default)
     * &lt;32   -> restricted to the same site
     * &lt;64   -> restricted to the same region
     * &lt;128  -> restricted to the same continent
     * &lt;255  -> no restriction
     * </blockquote></pre>
     * The default value is 1, meaning that the datagram will not go beyond the 
     * local subnet.
     * <p>
     * return <code>true</code> if no error occured.
     *
     * @param ttl the "Time to Live" value
     * @return boolean
     * @see UDPClient#getTimeToLive()
     */
    public boolean setTimeToLive(int ttl) {
        try {
            if (isMulticast() && !isClosed()) mcSocket.setTimeToLive(ttl);
            return true;
        } catch (IOException e) { 
            error("setting the default \"Time to Live\" value failed!"+
            "\n\t> "+e.getMessage()); 
        } catch (IllegalArgumentException e) {
            error("\"Time to Live\" value must be in the range of 0-255"); 
        }
        return false;
    }

    /**
     * Get the "Time to Live" (TTL) value for multicast packets.
     * @return the TTL value, or -1 if an error occurred
     */
    public int getTimeToLive() {
        try {
            if (isMulticast() && !isClosed()) {
                return mcSocket.getTimeToLive();
            }
        } catch (IOException e) {
            error("Could not retrieve the TTL value! " + e.getMessage());
        }
        return -1;
    }

    /**
     * Enable or disable output process logging.
     * @param on true to enable logging, false to disable
     */
    public void enableLogging(boolean on) {
        log = on;
    }

    /**
     * Output a log message to the standard output stream.
     * @param message the log message
     */
    private void log(String message) {
        Date date = new Date();
        if (!log && header.equals("")) {
            header = "-- UDP session started at " + date + " --\n-- " + message + " --\n";
        }

        if (log) {
            String pattern = "yy-MM-dd HH:mm:ss.S Z";
            String formattedDate = new SimpleDateFormat(pattern).format(date);
            System.out.println(header + "[" + formattedDate + "] " + message);
            header = "";
        }
    }

    /**
     * Output an error message to the standard error stream.
     * @param message the error message
     */
    private void error(String message) {
        System.err.println(message);
    }
}

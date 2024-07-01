package components.communication;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Create and manage a TCP client socket to send and receive
 * data over the network.
 *
 * @version 0.1
 */
public abstract class TCPClient implements Runnable {

    private Socket socket = null;
    private BufferedReader in = null;
    // private PrintWriter out = null;
    private boolean log = false;
    private boolean listen = false;
    private int timeout = 0;
    private Thread thread = null;
    private String header = "";

    /**
     * Create a new TCP client socket and connect to the specified server address and port.
     *
     * @param ip   server address
     * @param port server port
     */
    public TCPClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log("Connected to server: " + ip + ", port: " + port);
        } catch (IOException e) {
            error("Opening socket failed! Address: " + ip + ", port: " + port + " " + e.getMessage());
        }
    }

    /**
     * Close the current TCP socket and all associated resources.
     */
    public void close() {
        if (isClosed()) return;

        try {
            socket.close();
            socket = null;
            in.close();
        } catch (IOException e) {
            error("Error while closing the socket! " + e.getMessage());
        } finally {
            log("Closed socket");
        }
    }

    /**
     * Check if the socket is closed.
     * @return boolean indicating if the socket is closed
     */
    public boolean isClosed() {
        return socket == null || socket.isClosed();
    }

    /**
     * Send a message to the server.
     * @param message the message to send
     * @return boolean indicating if the message was sent successfully
     */
    public boolean send(String message) {
        if (isClosed()) return false;
        log("Sent message: " + message);
        return true;
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
            thread.interrupt();
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
            while (listen) {
                if (in.ready()) {
                    StringBuilder messageBuilder = new StringBuilder();
                    int charRead;
                    while ((charRead = in.read()) != -1 && charRead != '\0') {
                        messageBuilder.append((char) charRead);
                    }
                    
                    if (charRead == '\0') {
                        String message = messageBuilder.toString();
                        log("Received message: " + message);
                        onReceive(message);
                    }
                    
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            listen = false;
            thread = null;
            if (e instanceof InterruptedException) {
                onTimeout();
            } else {
                error("Listen failed! " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        listen();
    }

    /**
     * @param message the received message
     */
    protected abstract void onReceive(String message);

    /**
     * A method to handle timeout events.
     */
    protected abstract void onTimeout();

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
            header = "-- TCP session started at " + date + " --\n-- " + message + " --\n";
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

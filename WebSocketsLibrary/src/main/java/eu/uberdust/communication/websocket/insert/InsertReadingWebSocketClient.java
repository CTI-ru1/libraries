package eu.uberdust.communication.websocket.insert;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.rest.UberdustRestClient;
import eu.uberdust.communication.websocket.task.InsertPingTask;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Observable;
import java.util.Timer;

/**
 * Insert New Reading Web Socket Client.
 */
public final class InsertReadingWebSocketClient extends Observable {

    /**
     * static instance(ourInstance) initialized as null.
     */
    private static InsertReadingWebSocketClient ourInstance = null;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(InsertReadingWebSocketClient.class);


    /**
     * Web Socket Protocol.
     */
    private static final String PROTOCOL = "INSERTREADING";
    /**
     * Websocket url prefix.
     */
    private static final String WS_PREFIX = "ws://";
    /**
     * Http url prefix.
     */
    private static final String HTTP_PREFIX = "http://";

    /**
     * Timer.
     */
    private Timer timer;

    /**
     * The WebSocketClient.
     */
    private WebSocketClient client;

    /**
     * The WebSocket Connection.
     */
    private WebSocket.Connection connection;

    /**
     * WebSocketClientFactory.
     */
    private WebSocketClientFactory factory;
    private String webSocketUrl;


    /**
     * WSocketClient is loaded on the first execution of WSocketClient.getInstance()
     * or the first access to WSocketClient.ourInstance, not before.
     *
     * @return ourInstance
     */
    public static InsertReadingWebSocketClient getInstance() {
        synchronized (InsertReadingWebSocketClient.class) {
            if (ourInstance == null) {
                ourInstance = new InsertReadingWebSocketClient();
            }
        }
        return ourInstance;
    }

    /**
     * Private constructor suppresses generation of a (public) default constructor.
     */
    private InsertReadingWebSocketClient() {
        factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        try {
            factory.start();
            client = factory.newWebSocketClient();
            client.setMaxIdleTime(-1);
            client.setProtocol(PROTOCOL);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * Connects to the WebSocket.
     */
    public void connect() throws Exception {
        if (webSocketUrl != null) {
            connect(webSocketUrl);
        }
    }

    /**
     * Connects to the WebSocket.
     *
     * @param webSocketUrl WebSocket URL.
     */
    public void connect(final String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
        try {
            factory = new WebSocketClientFactory();
            factory.setBufferSize(4096);
            factory.start();
            client = factory.newWebSocketClient();
            client.setMaxIdleTime(-1);
            client.setProtocol(PROTOCOL);


            LOGGER.info("Connecting to " + webSocketUrl);
            // open connection
            connection = client.open(new URI(webSocketUrl), new InsertReadingWebSocketIMPL()).get();

            try {
                startPingingTask();
            } catch (Exception e) {
                LOGGER.error(e);
            }

        } catch (final Exception e) {

            // in case of exception keep trying to make connection after 2 seconds
            LOGGER.error(e);
            e.printStackTrace();

            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e1) {
                LOGGER.error(e1);
            }
            connect(webSocketUrl);
        }
    }

    /**
     * Send Node Reading.
     *
     * @param nodeReadings a @Message.NodeReadings instance.
     * @throws java.io.IOException an IOException exception.
     */
    public void sendNodeReading(final Message.NodeReadings nodeReadings) throws IOException {
        connection.sendMessage(nodeReadings.toByteArray(), 0, nodeReadings.toByteArray().length);

    }

    /**
     * Send Link Reading.
     *
     * @param linkReadings a NodeReading instance.
     * @throws java.io.IOException an IOException exception.
     */
    public void sendLinkReading(final Message.LinkReadings linkReadings) throws IOException {
        connection.sendMessage(linkReadings.toByteArray(), 0, linkReadings.toByteArray().length);
    }

//    /**
//     * Send message over the WebSocket as binary data.
//     *
//     * @param message a string message.
//     * @throws java.io.IOException an IOException.
//     */
//    private void sendMessage(final String message) throws IOException {
//        // if connection is not opened do nothing
//        if (!connection.isOpen()) {
//            return;
//        }
//
//        byte[] bytes = message.getBytes();
//        connection.sendMessage(bytes, 0, bytes.length);
//    }

    /**
     * Start pinging task.
     */
    private void startPingingTask() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new InsertPingTask(), InsertPingTask.DELAY, InsertPingTask.DELAY);
    }

    /**
     * Stop pinging task.
     */
    private void stopPingingTask() {
        timer.cancel();
    }

    /**
     * Send PING message as string data to keep connection alive.
     */
    public void ping() {

        // if connection is not opened do nothing
        if (!connection.isOpen()) {
            return;
        }

        try {
            connection.sendMessage("PING");
        } catch (final IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Disconnect method.
     */
    public void disconnect() {
        try {
            connection.disconnect();
            if (factory.isRunning()) {
                factory.destroy();
            }
            factory.stop();
        } catch (final Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * Checks the Rest Interface to see if connection is available.
     */
    public void restPing() {
        UberdustRestClient.getInstance().callRestfulWebService(webSocketUrl.replace(WS_PREFIX, HTTP_PREFIX));
    }

    protected void update(final String data) {
        this.setChanged();
        this.notifyObservers(data);
    }

    protected void update(final byte[] data, final int offset, final int length) {
        this.setChanged();
        this.notifyObservers(data);
    }
}

package eu.uberdust.communication.websocket.readings;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.rest.UberdustRestClient;
import eu.uberdust.communication.websocket.listener.WebSocketIMPL;
import eu.uberdust.communication.websocket.readings.util.PingTask;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/17/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSReadingsClient extends Observable {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WSReadingsClient.class);

    /**
     * static instance(ourInstance) initialized as null.
     */
    private static WSReadingsClient ourInstance = null;

    /**
     * Websocket url prefix.
     */
    private static final String WS_PREFIX = "ws://";

    /**
     * Http url prefix.
     */
    private static final String HTTP_PREFIX = "http://";

    /**
     * Insert protocol.
     */
    private static final String INSERT_PROTOCOL = "INSERTREADING";

    /**
     * Static WebSocket URI.
     */
    private URI WS_URI;

    /**
     * The WebSocketClientFactory.
     */
    private WebSocketClientFactory factory;

    /**
     * The HashMap which contains all the connections.
     */
    private HashMap<String, WebSocket.Connection> protocols;

    private final WebSocketIMPL webSocketIMPL = new WebSocketIMPL();

    /**
     * Server's Web Socket URL.
     */
    private String webSocketUrl;

    /**
     * WSocketClient is loaded on the first execution of WSocketClient.getInstance()
     * or the first access to WSocketClient.ourInstance, not before.
     *
     * @return ourInstance
     */
    public static WSReadingsClient getInstance() {
        synchronized (WSReadingsClient.class) {
            if (ourInstance == null) {
                ourInstance = new WSReadingsClient();
            }
        }
        return ourInstance;
    }

    /**
     * Private constructor suppresses generation of a (public) default constructor.
     */
    private WSReadingsClient() {
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
        protocols = new HashMap<String, WebSocket.Connection>();
        startPingTask();
        LOGGER.info("WSocketClient initialized");
    }

    /**
     * Initialize Ping Task to keep alive websocket connections.
     */
    private void startPingTask() {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new PingTask(timer), PingTask.DELAY, PingTask.DELAY);
    }


    public void setServerUrl(final String serverUrl) {
        webSocketUrl = serverUrl;
        createWebSocketFactory();

    }

    private void createWebSocketFactory() {
        try {
            WS_URI = new URI(webSocketUrl);
            factory = new WebSocketClientFactory();
            factory.setBufferSize(4096);
            factory.start();
        } catch (final Exception e) {
            LOGGER.error("Unable to create WebSocket Factory ", e);
        }
    }

    private void createNewConnection(final String protocol) {
        WebSocket.Connection connection = null;
        try {
            final WebSocketClient client = factory.newWebSocketClient();
            client.setMaxIdleTime(-1);
            client.setProtocol(protocol);
            connection = client.open(WS_URI, webSocketIMPL).get();
            LOGGER.info("New Web Socket Connection. Protocol: " + client.getProtocol());
        } catch (final Exception e) {
            LOGGER.error("Unable to Create new WebSocket connection", e);
            if (e.getMessage().contains("ProtocolException")) {
                LOGGER.fatal("Wrong Protocol Definition: " + protocol);
                throw new RuntimeException("Wrong Protocol Definition: " + protocol);
            }
            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e1) {
                LOGGER.error(e1);
            }
            createNewConnection(protocol);
        }
        if (connection != null) {
            protocols.put(protocol, connection);
        }
    }

    public void ping() {
        for (WebSocket.Connection connection : protocols.values()) {
            if (connection.isOpen()) {
                try {
                    connection.sendMessage("ping");
                } catch (final IOException e) {
                    LOGGER.error("Unable to Ping ", e);
                }
            }
        }
    }

    public void disconnect() {

        if (factory.isRunning()) {
            factory.destroy();
        }

        for (WebSocket.Connection connection : protocols.values()) {
            connection.disconnect();
        }

        try {
            factory.stop();
        } catch (final Exception e) {
            LOGGER.error("Unable to stop WebSockset Factory ", e);
        }
    }


    protected void restPing() {
        UberdustRestClient.getInstance().callRestfulWebService(webSocketUrl.replace(WS_PREFIX, HTTP_PREFIX));
    }

    protected void update(String data) {
        this.setChanged();
        this.notifyObservers(data);
    }


    /**
     * Send Node Reading.
     *
     * @param nodeReadings a @Message.NodeReadings instance.
     * @throws java.io.IOException an IOException exception.
     */
    public void sendNodeReading(final Message.NodeReadings nodeReadings) throws IOException {
        final Message.Envelope envelope = Message.Envelope.newBuilder()
                .setType(Message.Envelope.Type.NODE_READINGS)
                .setNodeReadings(nodeReadings).build();
        sendEnvelope(envelope);
    }

    /**
     * Send Link Reading.
     *
     * @param linkReadings a NodeReading instance.
     * @throws java.io.IOException an IOException exception.
     */
    public void sendLinkReading(final Message.LinkReadings linkReadings) throws IOException {
        final Message.Envelope envelope = Message.Envelope.newBuilder()
                .setType(Message.Envelope.Type.LINK_READINGS)
                .setLinkReadings(linkReadings).build();
        sendEnvelope(envelope);
    }

    /**
     * Send Envelope to server.
     *
     * @param envelope a @Message.Envelope instance
     * @throws java.io.IOException an IOException exception.
     */
    private void sendEnvelope(final Message.Envelope envelope) throws IOException{
        if (!protocols.containsKey(INSERT_PROTOCOL)) {
            createNewConnection(INSERT_PROTOCOL);
        }

        final byte[] byteArray = envelope.toByteArray();

        protocols.get(INSERT_PROTOCOL).sendMessage(byteArray, 0, byteArray.length);

    }
}


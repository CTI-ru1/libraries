package eu.uberdust.network;

import eu.uberdust.communication.websocket.command.WSCommandReceiverClient;
import eu.uberdust.communication.websocket.insert.InsertReadingWebSocketClient;
import eu.uberdust.reading.LinkReading;
import eu.uberdust.reading.NodeReading;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/6/12
 * Time: 10:14 AM
 */
public class NetworkManager extends Observable implements Observer {


    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(NetworkManager.class);
    private static NetworkManager instance = null;

    public NetworkManager() {

    }

    public void start(final String server, final int testbedId) {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));

        WSCommandReceiverClient.getInstance().setTestbedId(testbedId);

        try {
            WSCommandReceiverClient.getInstance().connect("ws://" + server + "/testbedcontroller.ws");
            LOGGER.info("connected");
        } catch (Exception e) {
            LOGGER.fatal(e);
        }

        LOGGER.info("Started Command Receiver");


        try {
            InsertReadingWebSocketClient.getInstance().connect("ws://" + server + "/insertreading.ws");
            LOGGER.info("connected");

        } catch (Exception e) {
            LOGGER.fatal(e);
        }
        LOGGER.info("Started Reading Insert Process");

    }

    public void sendNodeReading(final NodeReading nodeReading) throws IOException {
        InsertReadingWebSocketClient.getInstance().sendNodeReading(nodeReading);
    }

    public void sendLinkReading(final LinkReading linkReading) throws IOException {
        InsertReadingWebSocketClient.getInstance().sendLinkReading(linkReading);
    }

    public synchronized static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }


    public static void main(final String[] args) {
        NetworkManager.getInstance().start("192.168.1.10:8081", 2);
        NetworkManager.getInstance().addObserver(new ExampleNetworkCommandListener());
    }

    @Override
    public void update(final Observable observable, final Object o) {
        this.setChanged();
        this.notifyObservers(o);
    }
}

package eu.uberdust.network;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.command.WSCommandReceiverClient;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;
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
            WSCommandReceiverClient.getInstance().addObserver(this);
            LOGGER.info("connected");
        } catch (Exception e) {
            LOGGER.fatal(e);
        }

        LOGGER.info("Started Command Receiver");


        try {
            WSReadingsClient.getInstance().setServerUrl("ws://" + server + "/insertreading.ws");
            LOGGER.info("connected");

        } catch (Exception e) {
            LOGGER.fatal(e);
        }
        LOGGER.info("Started Reading Insert Process");

    }

    public void sendNodeReading(final Message.NodeReadings nodeReadings) throws IOException {
        WSReadingsClient.getInstance().sendNodeReading(nodeReadings);
    }

    public void sendLinkReading(final Message.LinkReadings linkReadings) throws IOException {
        WSReadingsClient.getInstance().sendLinkReading(linkReadings);
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
        LOGGER.info("calling notifyObservers");
        this.setChanged();
        this.notifyObservers(o);
        this.setChanged();
    }
}

package eu.uberdust.network;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.command.WSCommandClient;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;
import eu.uberdust.devicedriver.CeilingLightDriver;
import eu.uberdust.devicedriver.LampLightDriver;
import eu.uberdust.devicedriver.LightDriver;
import eu.uberdust.devicedriver.PayloadDriver;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/6/12
 * Time: 10:14 AM
 */
public class NetworkManager extends Observable {


    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(NetworkManager.class);
    private static NetworkManager instance = null;

    public NetworkManager() {

    }

    public void start(final String server, final int testbedId) {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));

        WSCommandClient.getInstance().setTestbedId(testbedId);

        try {
            WSCommandClient.getInstance().connect("ws://" + server + "/testbedcontroller.ws");
            WSCommandClient.getInstance().addObserver(new CeilingLightDriver());
            WSCommandClient.getInstance().addObserver(new LightDriver());
            WSCommandClient.getInstance().addObserver(new PayloadDriver());
            WSCommandClient.getInstance().addObserver(new LampLightDriver());
            LOGGER.info("connected");
        } catch (Exception e) {
            LOGGER.fatal(e);
        }

        LOGGER.info("Started Command Receiver");


        try {
            WSReadingsClient.getInstance().setServerUrl("ws://" + server + "/readings.ws");
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
        NetworkManager.getInstance().addObserver(new GrowlNetworkCommandListener());
        NetworkManager.getInstance().start("uberdust.cti.gr:80/", 1);
//        NetworkManager.getInstance().start("192.168.1.10:8081", 2);
//        Thread nm = new Thread(new ExampleNetworkCommandListener());
//        nm.start();
    }


    public void update(final Object o) {
        LOGGER.info("calling notifyObservers");
        this.setChanged();
        this.notifyObservers(o);
        this.setChanged();
    }
}


import eu.uberdust.communication.UberdustClient;
import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/2/13
 * Time: 8:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class UberdustSSSPConnector implements Observer {
    private static UberdustSSSPConnector ourInstance = new UberdustSSSPConnector();
    private Map<String, SemanticNode> allnodes;

    public static UberdustSSSPConnector getInstance() {
        return ourInstance;
    }

    private UberdustSSSPConnector() {
        allnodes = new HashMap<String, SemanticNode>();
        UberdustClient.setUberdustURL("http://uberdust.cti.gr");
        //final String webSocketUrl = "ws://carrot.cti.gr:8080/uberdust/readings.ws";
        final String webSocketUrl = "ws://uberdust.cti.gr:80/readings.ws";
        WSReadingsClient.getInstance().setServerUrl(webSocketUrl);
        WSReadingsClient.getInstance().subscribe("*", "*");
        WSReadingsClient.getInstance().addObserver(this);
    }

    public static void main(String args[]) {
        UberdustSSSPConnector.getInstance();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof WSReadingsClient)) {
            return;
        }
        if (arg instanceof Message.NodeReadings) {
            Message.NodeReadings.Reading reading = ((Message.NodeReadings) arg).getReading(0);
            if (!allnodes.containsKey(reading.getNode())) {
                allnodes.put(reading.getNode(), new SemanticNode(reading.getNode()));
            }
            System.out.println(allnodes.get(reading.getNode()));
        }
    }
}



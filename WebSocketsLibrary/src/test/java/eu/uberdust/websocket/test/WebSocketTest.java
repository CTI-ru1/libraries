package eu.uberdust.websocket.test;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;

import java.io.IOException;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 12/8/11
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketTest {

    public static void main(String[] args) {

        // sample node reading
        final String node = "urn:testbed2:n3";
        final String capability = "testcapn3";

        Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode(node)
                .setCapability(capability)
                .setTimestamp(new Date().getTime())
                .setDoubleReading(1)
                .build();


        Message.NodeReadings readings = Message.NodeReadings.newBuilder()
                .addReading(reading1)
                .build();


/*
* WebSocket Call
*/

        //final String webSocketUrl = "ws://carrot.cti.gr:8080/uberdust/readings.ws";
        final String webSocketUrl = "ws://localhost:8080/readings.ws";
        WSReadingsClient.getInstance().setServerUrl(webSocketUrl);


        // insert node reading using WebSockets

//        WSReadingsClient.getInstance().subscribe("urn:testbed1:2", "testcap");
//        WSReadingsClient.getInstance().addObserver(new Observer() {
//            @Override
//            public void update(Observable o, Object arg) {
//                System.out.println((Message.NodeReadings) arg);
//            }
//        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        System.out.println("before");
        try {
            WSReadingsClient.getInstance().sendNodeReading(readings);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("after");


    }

}

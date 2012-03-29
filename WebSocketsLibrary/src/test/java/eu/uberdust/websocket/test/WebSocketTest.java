package eu.uberdust.websocket.test;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 12/8/11
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketTest {

    public static void main(String[] args) {
/*
        // sample node reading

    */
        Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:wisebed:ctitestbed:0x494")
                .setCapability("urn:wisebed:node:capability:pir")
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
        final String webSocketUrl = "ws://uberdust.cti.gr:80/readings.ws";
        WSReadingsClient.getInstance().setServerUrl(webSocketUrl);


        // insert node reading using WebSockets

        WSReadingsClient.getInstance().subscribe("urn:wisebed:ctitestbed:0x494", "urn:wisebed:node:capability:pir");
        WSReadingsClient.getInstance().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println((Message.NodeReadings) arg);
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            System.out.println("before");
            WSReadingsClient.getInstance().sendNodeReading(readings);
            System.out.println("after");

            System.out.println("before");
            WSReadingsClient.getInstance().sendNodeReading(readings);
            System.out.println("after");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            System.out.println("before");
            WSReadingsClient.getInstance().sendNodeReading(readings);
            System.out.println("after");

            System.out.println("before");
            WSReadingsClient.getInstance().sendNodeReading(readings);
            System.out.println("after");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

/*
            int counter = 0;
            while (true) {
                System.out.println(counter);

                InsertReadingWebSocketClient.getInstance().sendNodeReading(readings);

                Thread.sleep(5);
                counter++;*//*


*/

    }
}

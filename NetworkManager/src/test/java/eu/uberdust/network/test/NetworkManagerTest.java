package eu.uberdust.network.test;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.network.NetworkManager;

import java.io.IOException;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 12/8/11
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkManagerTest {

    public static void main(String[] args) {
/*
        // sample node reading

    */
        Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:testbed1:2")
                .setCapability("testcap")
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
//        final String webSocketUrl = "ws:///readings.ws";
        NetworkManager.getInstance().start("localhost:8080", 3);


        // insert node reading using WebSockets

//        NetworkManager.getInstance().subscribe("urn:testbed1:2", "testcap");
//        NetworkManager.getInstance().addObserver(new Observer() {
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


        try {
            System.out.println("before");
            NetworkManager.getInstance().sendNodeReading(readings);
            System.out.println("after");

            System.out.println("before");
            NetworkManager.getInstance().sendNodeReading(readings);
            System.out.println("after");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        int counter = 0;
        while (true) {
            System.out.println(counter);
            reading1 = Message.NodeReadings.Reading.newBuilder()
                    .setNode("urn:testbed1:2")
                    .setCapability("testcap")
                    .setTimestamp(new Date().getTime())
                    .setDoubleReading(1)
                    .build();


            readings = Message.NodeReadings.newBuilder()
                    .addReading(reading1)
                    .build();

            try {
                System.out.println("before");
                NetworkManager.getInstance().sendNodeReading(readings);
                System.out.println("after");

                System.out.println("before");
                NetworkManager.getInstance().sendNodeReading(readings);
                System.out.println("after");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            counter++;


        }
    }
}

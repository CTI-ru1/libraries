package websocket;

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

    /*    Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:ctinetwork:carrot_delete_moi")
                .setCapability("urn:ctinetwork:node:capability:lockScreen")
                .setTimestamp(new Date().getTime())
                .setDoubleReading(1)
                .build();*/


        Message.NodeReadings.Reading reading2 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:test:0x1")
                .setCapability("temp2")
                .setTimestamp(new Date().getTime())
                .setDoubleReading(98)
                .build();


        Message.NodeReadings readings = Message.NodeReadings.newBuilder()
            //    .addReading(reading1)
                .addReading(reading2)
                .build();

        /**
         * WebSocket Call
         */

        final String webSocketUrl = "ws://carrot.cti.gr:8080/uberdust/insertreading.ws";
        //final String webSocketUrl = "ws://localhost:8080/uberdust/insertreading.ws";
        // insert node reading using WebSockets


        WSReadingsClient.getInstance().setServerUrl(webSocketUrl);
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
                counter++;*/


    }
}

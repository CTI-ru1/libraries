package websocket;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.insert.InsertReadingWebSocketClient;

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

        Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:ctinetwork:carrot_delete_moi")
                .setCapability("urn:ctinetwork:node:capability:lockScreen")
                .setTimestamp(new Date().getTime())
                .setDoubleReading(1)
                .build();


        Message.NodeReadings.Reading reading2 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:ctinetwork:carrot_delete_me")
                .setCapability("urn:ctinetwork:node:capability:lockScreen")
                .setTimestamp(new Date().getTime())
                .setDoubleReading(1)
                .build();


        Message.NodeReadings readings = Message.NodeReadings.newBuilder()
                .addReading(reading1)
                .addReading(reading2)
                .build();

        /**
         * WebSocket Call
         */

        final String webSocketUrl = "ws://localhost:8080/uberdust/insertreading.ws";

        // insert node reading using WebSockets

        try {
            InsertReadingWebSocketClient.getInstance().connect(webSocketUrl);

            int counter = 0;
            while (true) {
                System.out.println(counter);

                InsertReadingWebSocketClient.getInstance().sendNodeReading(readings);

                Thread.sleep(5);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}

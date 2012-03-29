package eu.uberdust.websocket.test;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;

import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/29/12
 * Time: 5:00 PM
 */
public class InsertReadingWebsocketExample {

    public static void main(final String[] args) {

        final String webSocketUrl = "ws://carrot.cti.gr:8080/uberdust/readings.ws";

        WSReadingsClient.getInstance().setServerUrl(webSocketUrl);

        addReading();

    }

    private static void addReading() {
        Message.NodeReadings.Reading reading1 = Message.NodeReadings.Reading.newBuilder()
                .setNode("urn:test:0x1")
                .setCapability("light")
                .setTimestamp(new Date().getTime())
                .setDoubleReading(1)
                .build();


        Message.NodeReadings readings = Message.NodeReadings.newBuilder()
                .addReading(reading1)
                .build();

        try {
            WSReadingsClient.getInstance().sendNodeReading(readings);
            System.out.println("added");
        } catch (IOException e) {

        }
    }
}

package protobuf;

import eu.uberdust.communication.protobuf.Message;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/13/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProtobufTest {


    public static void main(String[] args) {
/*
        //Usage Example of a Protobuf Control Message
        Message.Control controlMessage = Message.Control.newBuilder()
                .setDestination("destination")
                .setCapability("capability")
                .setLastValue("2").build();


        //Usage Example of a Protobuf Control Message
        Message.Control controlMessagePayload = Message.Control.newBuilder()
                .setDestination("destination")
                .setPayload("1,1,1")
                .build();


        //NodeReading Usage Examples
        final ArrayList<Message.NodeReadings.Reading> readingArrayList = new ArrayList<Message.NodeReadings.Reading>();
        readingArrayList.add(Message.NodeReadings.Reading.newBuilder()
                .setNode("nodeID")
                .setCapability("node Capability")
                .setTimestamp(System.currentTimeMillis())
                .setStringReading("222")
                .build());

        readingArrayList.add(Message.NodeReadings.Reading.newBuilder()
                .setNode("nodeID")
                .setCapability("node Capability")
                .setTimestamp(System.currentTimeMillis())
                .setDoubleReading(System.currentTimeMillis())
                .build());


        final Message.NodeReadings nodeReadingsFromList = Message.NodeReadings.newBuilder().addAllReading(readingArrayList).build();
        System.out.println(nodeReadingsFromList.getReadingCount());


        final Message.NodeReadings nodeReadings = Message.NodeReadings.newBuilder()
                //Add Double Reading
                .addReading(Message.NodeReadings.Reading.newBuilder()
                        .setNode("nodeID")
                        .setCapability("node Capability")
                        .setTimestamp(System.currentTimeMillis())
                        .setStringReading("222"))
                        //Add Node Reading
                .addReading(Message.NodeReadings.Reading.newBuilder()
                        .setNode("nodeID")
                        .setCapability("node Capability")
                        .setTimestamp(System.currentTimeMillis())
                        .setDoubleReading(System.currentTimeMillis()))
                .build();

        System.out.println(nodeReadings.getReadingCount());


        //Link Reading Usage Example
        final ArrayList<Message.LinkReadings.Reading> linkReadings = new ArrayList<Message.LinkReadings.Reading>();
        linkReadings.add(Message.LinkReadings.Reading.newBuilder()
                .setSource("nodeID")
                .setTarget("nodeID")
                .setCapability("node Capability")
                .setTimestamp(System.currentTimeMillis())
                .setStringReading("222")
                .build());


        final Message.Envelope envelope = Message.Envelope.newBuilder()
                .setType(Message.Envelope.Type.CONTROL)
                .setControl(controlMessage)
                .build();

 */   }

}

package eu.uberdust.network;

import eu.uberdust.communication.protobuf.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/9/12
 * Time: 2:01 PM
 */
public class RandomNetworkValueGenerator implements Runnable {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(RandomNetworkValueGenerator.class);

    public List<String> nodes = new ArrayList<String>();
    private Random generator;

    public RandomNetworkValueGenerator() {

        nodes.add("urn:pspace:test1@temperature");
        nodes.add("urn:pspace:test2@temperature");
        nodes.add("urn:pspace:test3@temperature");
        nodes.add("urn:pspace:test4@temperature");
        nodes.add("urn:pspace:test1@light");
        nodes.add("urn:pspace:test2@light");
        nodes.add("urn:pspace:test3@light");
        nodes.add("urn:pspace:test4@light");

        generator = new Random();

    }

    @Override
    public void run() {

        while (true) {
            Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();
            for (final String node : nodes) {
                Message.NodeReadings.Reading.Builder nr = Message.NodeReadings.Reading.newBuilder();

                nr.setNode(node.split("@")[0]);
                nr.setCapability(node.split("@")[1]);
                double value = generator.nextInt() % 40;
                nr.setDoubleReading(value > 0 ? value : -value);
                nr.setTimestamp(System.currentTimeMillis());


                readings.addReading(nr.build());
            }
            try {
                NetworkManager.getInstance().sendNodeReading(readings.build());
            } catch (IOException e) {
                LOGGER.error(e);
            }


            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }
        }

    }
}

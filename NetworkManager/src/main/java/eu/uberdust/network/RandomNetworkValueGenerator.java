//package eu.uberdust.network;
//
//import eu.uberdust.reading.NodeReading;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * Created by IntelliJ IDEA.
// * User: amaxilatis
// * Date: 3/9/12
// * Time: 2:01 PM
// */
//public class RandomNetworkValueGenerator implements Runnable {
//
//    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(RandomNetworkValueGenerator.class);
//
//    public List<String> nodes = new ArrayList<String>();
//    private Random generator;
//
//    public RandomNetworkValueGenerator() {
//
//        nodes.add("urn:pspace:test1@temperature");
//        nodes.add("urn:pspace:test2@temperature");
//        nodes.add("urn:pspace:test3@temperature");
//        nodes.add("urn:pspace:test4@temperature");
//        nodes.add("urn:pspace:test1@light");
//        nodes.add("urn:pspace:test2@light");
//        nodes.add("urn:pspace:test3@light");
//        nodes.add("urn:pspace:test4@light");
//
//        generator = new Random();
//
//    }
//
//    @Override
//    public void run() {
//
//        while (true) {
//            for (final String node : nodes) {
//                NodeReading nr = new NodeReading();
//                nr.setTestbedId(String.valueOf(2));
//                nr.setNodeId(node.split("@")[0]);
//                nr.setCapabilityName(node.split("@")[1]);
//                nr.setReading(String.valueOf(generator.nextInt() % 40));
//                nr.setTimestamp(String.valueOf(System.currentTimeMillis()));
//                nr.setStringReading(null);
//
//                try {
//                    NetworkManager.getInstance().sendNodeReading(nr);
//                } catch (IOException e) {
//                    LOGGER.error(e);
//                }
//
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    LOGGER.error(e);
//                }
//            }
//            try {
//                Thread.sleep(60000);
//            } catch (InterruptedException e) {
//                LOGGER.error(e);
//            }
//        }
//
//    }
//}

package eu.uberdust.uinterface;

import eu.uberdust.communication.UberdustClient;
import eu.wisebed.wisedb.model.Node;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class GetNodesTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(GetNodesTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetNodesTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GetNodesTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");

        JSONObject capabilities = null;
        try {
            capabilities = UberdustClient.getInstance().getNodes(1);
            LOGGER.info(capabilities);
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp2() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");


        try {
            List<Node> nodes = UberdustClient.getInstance().listNodes(1);
            for (Node node : nodes) {
                LOGGER.info(node);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


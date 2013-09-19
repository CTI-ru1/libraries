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
public class GetVirtualNodesTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(GetVirtualNodesTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetVirtualNodesTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GetVirtualNodesTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");

        try {
            List<Node> nodes = UberdustClient.getInstance().listVirtualNodes(6);
            for (Node node : nodes) {
                LOGGER.info(node);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            LOGGER.error(e,e);
        }
    }
}


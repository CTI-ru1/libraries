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
            LOGGER.error(e,e);
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
            LOGGER.error(e,e);
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testGetNodeTypes4Node() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");


        try {
            List<String> types = UberdustClient.getInstance().getNodeNodeTypes(1, "urn:wisebed:ctitestbed:0x190");
            for (String type : types) {
                LOGGER.info(type);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            LOGGER.error(e,e);
        } catch (Exception e) {
            assertTrue(false);
            LOGGER.error(e,e);
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testGetRooms4Node() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");


        try {
            List<String> rooms = UberdustClient.getInstance().getNodeRooms(1, "urn:wisebed:ctitestbed:0x190");
            for (String room : rooms) {
                LOGGER.info(room);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            LOGGER.error(e,e);
        } catch (Exception e) {
            assertTrue(false);
            LOGGER.error(e,e);
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testGetWorkstations4Node() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");


        try {
            List<String> workstations = UberdustClient.getInstance().getNodeWorkstations(1, "urn:wisebed:ctitestbed:0x190");
            for (String workstation : workstations) {
                LOGGER.info(workstation);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            LOGGER.error(e,e);
        } catch (Exception e) {
            assertTrue(false);
            LOGGER.error(e,e);
        }
    }
}


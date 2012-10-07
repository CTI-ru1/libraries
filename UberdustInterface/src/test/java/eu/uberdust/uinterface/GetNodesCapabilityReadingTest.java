package eu.uberdust.uinterface;

import eu.uberdust.communication.UberdustClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Unit test for simple App.
 */
public class GetNodesCapabilityReadingTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(GetNodesCapabilityReadingTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetNodesCapabilityReadingTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GetNodesCapabilityReadingTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");
        int count = 10;
        JSONObject capabilities = null;
        try {
            capabilities = UberdustClient.getInstance().getNodeReading(1, "urn:wisebed:ctitestbed:0x9979", "urn:wisebed:node:capability:pir", count);
            LOGGER.info(capabilities);
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


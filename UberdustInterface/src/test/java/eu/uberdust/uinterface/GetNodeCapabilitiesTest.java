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
public class GetNodeCapabilitiesTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(GetNodeCapabilitiesTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetNodeCapabilitiesTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GetNodeCapabilitiesTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");

        JSONObject capabilities = null;
        try {
            capabilities = UberdustClient.getInstance().getNodeCapabilities("urn:wisebed:ctitestbed:0x190");
            LOGGER.info(capabilities);
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


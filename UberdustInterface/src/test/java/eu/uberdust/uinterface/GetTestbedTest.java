package eu.uberdust.uinterface;

import eu.uberdust.communication.UberdustClient;
import eu.wisebed.wisedb.model.Testbed;
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
public class GetTestbedTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(GetTestbedTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GetTestbedTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GetTestbedTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException {
        UberdustClient.getInstance().setUberdustURL("http://uberdust.cti.gr");

        Testbed testbed= null;
        try {
            testbed= UberdustClient.getInstance().getTestbedById(1);
            LOGGER.info(testbed);
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

        List<Testbed> testbeds= null;
        try {
            testbeds= UberdustClient.getInstance().listTestbeds();
            for (Testbed testbed : testbeds) {
                LOGGER.info(testbed);
            }
            assertTrue(true);
        } catch (JSONException e) {
            assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


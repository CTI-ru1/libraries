package eu.uberdust.uinterface;

import eu.uberdust.communication.UberdustClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * Unit test for simple App.
 */
public class ParseUrlTest
        extends TestCase {
    private static final Logger LOGGER = Logger.getLogger(ParseUrlTest.class);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParseUrlTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParseUrlTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        final String uberdustURL = "http://uberdust.cti.gr:80";
        assertFalse(UberdustClient.getUberdustHostname(uberdustURL).contains(":"));

    }
}


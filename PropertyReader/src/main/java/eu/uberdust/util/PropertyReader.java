package eu.uberdust.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads Properties from a master property file and offers them to Applications.
 */
public final class PropertyReader {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PropertyReader.class);

    /**
     * Singleton instance.
     */
    private static PropertyReader instance = null;
    /**
     * The property file.
     */
    final transient Properties properties;

    private static final String TESTBED_PREFIX = "testbed.prefix";
    private static final String CAPABILITIES_PREFIX = "testbed.capability.prefix";
    private static final String TESTBED_ID = "wisedb.testbedid";

    /**
     * Default Constructor.
     */
    private PropertyReader() {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));

        properties = new Properties();

    }


    public static void main(String[] args) {
        new PropertyReader();
    }

    /**
     * Get Singleton instance
     *
     * @return the unique Property Reader Instance.
     */
    public static PropertyReader getInstance() {
        synchronized (PropertyReader.class) {
            if (instance == null) {
                instance = new PropertyReader();
            }
        }
        return instance;
    }

    /**
     * Retruns a the property file so that applications can access the file contents.
     *
     * @return the Property File Object.
     */
    public Properties getProperties() {
        LOGGER.debug("getProperties()");
        return properties;
    }

    public String getTestbedPrefix() {
        return properties.get(TESTBED_PREFIX).toString();
    }

    public String getTestbedCapabilitiesPrefix() {
        return properties.get(CAPABILITIES_PREFIX).toString();
    }

    public int getTestbedId() {
        return Integer.valueOf(properties.get(TESTBED_ID).toString());
    }

    public void setFile(String propertyFile) {
        try {
            properties.load(new FileInputStream(propertyFile));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("No properties file found! " + propertyFile + " not found!");
            return;
        }
        LOGGER.info("Loaded properties from file: " + propertyFile);
    }
}

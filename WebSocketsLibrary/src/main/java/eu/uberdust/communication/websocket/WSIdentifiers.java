package eu.uberdust.communication.websocket;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/20/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WSIdentifiers {

    /**
     * Websocket url prefix.
     */
    public static final String WS_PREFIX = "ws://";

    /**
     * Http url prefix.
     */
    public static final String HTTP_PREFIX = "http://";

    /**
     * Insert protocol.
     */
    public static final String INSERT_PROTOCOL = "INSERTREADING";

    /**
     * Testbed Controller.
     */
    public static final String COMMAND_PROTOCOL = "TESTBEDCONTROLLER";

    /**
     * Subscribe protocol prefix.
     */
    public static final String SUBSCRIBE_PROTOCOL_PREFIX = "SUB";

    /**
     * Subscribe protocol suffix for JSON response.
     */
    public static final String SUBSCRIBE_PROTOCOL_JSON_SUFFIX = "SUB";

    /**
     * Subscribe protocol suffix for TEXT response.
     */
    public static final String SUBSCRIBE_PROTOCOL_TEXT_SUFFIX = "SUB";

    /**
     * Listener Protocol delimiter.
     */
    public static final String DELIMITER = "@";

}

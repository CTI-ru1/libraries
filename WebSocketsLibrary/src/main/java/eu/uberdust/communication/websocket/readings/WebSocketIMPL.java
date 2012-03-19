package eu.uberdust.communication.websocket.readings;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/19/12
 * Time: 1:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketIMPL implements WebSocket.OnBinaryMessage {


    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WebSocketIMPL.class);

    /**
     * Called with a complete binary message when all fragments have been received.
     * The maximum size of binary message that may be aggregated from multiple frames is set with {@link Connection#setMaxBinaryMessageSize(int)}.
     *
     * @param data
     * @param offset
     * @param length
     */
    @Override
    public void onMessage(byte[] data, int offset, int length) {
        LOGGER.info("onMessage");
        WSReadingsClient.getInstance().notifyObservers(data);
    }

    /**
     * Called when a new websocket connection is accepted.
     *
     * @param connection The Connection object to use to send messages.
     */
    @Override
    public void onOpen(Connection connection) {
        LOGGER.info("onOpen");
    }

    /**
     * Called when an established websocket connection closes
     *
     * @param closeCode
     * @param message
     */
    @Override
    public void onClose(int closeCode, String message) {
        LOGGER.info("onClose");
        WSReadingsClient.getInstance().disconnect();
        WSReadingsClient.getInstance().restPing();
        WSReadingsClient.getInstance().createWebSocketFactory();
    }
}

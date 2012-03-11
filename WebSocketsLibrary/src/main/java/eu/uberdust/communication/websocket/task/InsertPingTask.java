package eu.uberdust.communication.websocket.task;

import eu.uberdust.communication.websocket.insert.InsertReadingWebSocketClient;

import java.util.TimerTask;

/**
 * Ping task timer class.
 */
public class InsertPingTask extends TimerTask {

    /**
     * Delay.
     */
    public static final long DELAY = 30000;

    /**
     * Constructor.
     */

    public InsertPingTask() {
        super();
    }

    @Override
    public final void run() {
        InsertReadingWebSocketClient.getInstance().ping();
    }
}

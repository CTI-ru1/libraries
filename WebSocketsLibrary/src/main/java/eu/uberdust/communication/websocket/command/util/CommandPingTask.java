package eu.uberdust.communication.websocket.command.util;

import eu.uberdust.communication.websocket.command.WSCommandReceiverClient;

import java.util.TimerTask;

/**
 * Ping task timer class.
 */
public class CommandPingTask extends TimerTask {

    /**
     * Delay.
     */
    public static final long DELAY = 30000;

    /**
     * Constructor.
     */

    public CommandPingTask() {
        super();
    }

    @Override
    public final void run() {
        WSCommandReceiverClient.getInstance().ping();
    }
}

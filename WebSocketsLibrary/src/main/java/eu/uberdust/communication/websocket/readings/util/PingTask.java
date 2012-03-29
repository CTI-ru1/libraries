package eu.uberdust.communication.websocket.readings.util;

import eu.uberdust.communication.websocket.readings.WSReadingsClient;
import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Ping Task.
 */
public class PingTask extends TimerTask {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PingTask.class);

    public static final long DELAY = 30000;

    private final Timer timer;

    public PingTask(final Timer thatTimer) {
        super();
        this.timer = thatTimer;
    }

    @Override
    public final void run() {
        WSReadingsClient.getInstance().ping();
    }
}

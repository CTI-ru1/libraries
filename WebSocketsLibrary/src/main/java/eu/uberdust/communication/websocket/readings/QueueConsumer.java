package eu.uberdust.communication.websocket.readings;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.communication.websocket.readings.WSReadingsClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Poll Envelopes from the Queue and Sends them to web App.
 */
public class QueueConsumer extends Thread {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QueueConsumer.class);

    /**
     * The blocking queue.
     */
    private final LinkedBlockingQueue<Message.Envelope> queue;

    /**
     * Defines the state of the Consumer.
     */
    private boolean isEnabled;

    public QueueConsumer(final LinkedBlockingQueue<Message.Envelope> thisQueue) {
        super();
        queue = thisQueue;
        isEnabled = true;
    }

    @Override
    public void run() {
        super.run();
        while (isEnabled) {
            try {
                WSReadingsClient.getInstance().sendEnvelope(queue.take());
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }
}

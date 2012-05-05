package eu.uberdust.devicedriver;

import eu.uberdust.DeviceCommand;
import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.network.NetworkManager;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/25/12
 * Time: 5:37 PM
 */
public class PayloadDriver extends DeviceDriver {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PayloadDriver.class);

    @Override
    public void update(final Observable observable, final Object o) {

        if (o instanceof Message.Control) {

            final Message.Control command = (Message.Control) o;

            if (command.hasPayload()) {
                final DeviceCommand result = new DeviceCommand(command.getDestination(), command.getCapability(), command.getPayload());
                NetworkManager.getInstance().update(result);
            }
        }
    }
}

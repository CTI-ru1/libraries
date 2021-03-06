package eu.uberdust.network;

import eu.uberdust.DeviceCommand;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/10/12
 * Time: 10:16 PM
 */
public class ExampleNetworkCommandListener implements Observer {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ExampleNetworkCommandListener.class);

    @Override
    public void update(Observable observable, Object o) {
        LOGGER.info("Got a command :" + o.toString());
        if (o instanceof DeviceCommand) {

            final DeviceCommand command = (DeviceCommand) o;

            LOGGER.info("sending to " + command.getDestination());
            LOGGER.info("sending bytes " + command.getPayload());

        }
    }
}
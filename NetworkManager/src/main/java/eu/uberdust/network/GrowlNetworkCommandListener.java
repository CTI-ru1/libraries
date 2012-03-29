package eu.uberdust.network;

import eu.uberdust.DeviceCommand;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/10/12
 * Time: 10:16 PM
 */
public class GrowlNetworkCommandListener implements Observer {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(GrowlNetworkCommandListener.class);

    @Override
    public void update(Observable observable, Object o) {
        LOGGER.info("Got a command :" + o.toString());
        if (o instanceof DeviceCommand) {

            final DeviceCommand command = (DeviceCommand) o;
            try {
                if (command.getDestination().contains("virtual")) {
                    Runtime.getRuntime().exec(new StringBuilder()
                            .append("gntp-send ")
                            .append(command.getDestination())
                            .append(" ")
                            .append(command.getPayload()).toString());
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }

        }
    }
}

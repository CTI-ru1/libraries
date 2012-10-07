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
public class ToyotomiDriver extends DeviceDriver {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ToyotomiDriver.class);
    static final String COMMAND_PREFIX = "7f,69,70,1,";

    @Override
    public void update(final Observable observable, final Object o) {
        if (o instanceof Message.Control) {

            final Message.Control command = (Message.Control) o;

            if (!command.hasPayload()) {

                LOGGER.info("sending to " + command.getDestination());
                LOGGER.info("capability " + command.getCapability());
                LOGGER.info("last value " + command.getLastValue());

                final StringBuilder bytes = new StringBuilder(COMMAND_PREFIX);
                if (command.getCapability().contains("aircondition:temperature")) {
                    bytes.append("1").append(",");
                    final int state = (int) Double.parseDouble(command.getLastValue());
                    bytes.append(state);
                    LOGGER.info("sending -temp");
                } else if (command.getCapability().contains("aircondition:active")) {
                    final int state = (int) Double.parseDouble(command.getLastValue());
                    if (state == 0) {
                        bytes.append("7");
                    } else if (state == 1) {
                        bytes.append("6");
                    } else {
                        return;
                    }
                    LOGGER.info("sending -active");

                } else if (command.getCapability().contains("aircondition:swing")) {
                    bytes.append("8");
                    LOGGER.info("sending -swing");
                } else if (command.getCapability().contains("aircondition:airdirection")) {
                    bytes.append("a");
                    LOGGER.info("sending -airdirection");
                } else if (command.getCapability().contains("aircondition:cleanair")) {
                    bytes.append("b");
                    LOGGER.info("sending -cleanair");
                } else if (command.getCapability().contains("aircondition:led")) {
                    bytes.append("c");
                    LOGGER.info("sending -led");
                } else if (command.getCapability().contains("aircondition:turbo")) {
                    bytes.append("d");
                    LOGGER.info("sending -turbo");
                } else {
                    return;
                }
                final DeviceCommand devCommand = new DeviceCommand(command.getDestination(), command.getCapability(), bytes.toString());
                NetworkManager.getInstance().update(devCommand);


            }
        }
    }

}

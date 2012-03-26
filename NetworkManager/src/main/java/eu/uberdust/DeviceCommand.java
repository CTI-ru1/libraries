package eu.uberdust;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/25/12
 * Time: 5:47 PM
 */
public class DeviceCommand {
    private final String destination;

    private final String command;

    public DeviceCommand(String destination, String command) {
        this.destination = destination;
        this.command = command;

    }

    public String getDestination() {
        return destination;
    }

    public String getPayload() {
        return command;
    }
}

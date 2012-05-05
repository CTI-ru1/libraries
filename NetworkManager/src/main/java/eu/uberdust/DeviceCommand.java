package eu.uberdust;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/25/12
 * Time: 5:47 PM
 */
public class DeviceCommand {
    private final String destination;

    private final String payload;
    private final String capability;

    public DeviceCommand(final String destination, final String capability, final String payload) {
        this.destination = destination;
        this.capability = capability;
        this.payload = payload;

    }

    public String getCapability() {
        return capability;
    }

    public String getDestination() {
        return destination;
    }

    public String getPayload() {
        return payload;
    }
}

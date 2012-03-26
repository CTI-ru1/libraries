package eu.uberdust.devicedriver;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/25/12
 * Time: 5:36 PM
 */
public abstract class DeviceDriver implements Observer {
    public abstract void update(Observable observable, Object o);

}

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 9/2/13
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class SemanticNode {
    String name;
    Map<String, String> values;
    Map<String, Date> times;

    public SemanticNode(String name) {
        this.name = name;
        values = new HashMap<String, String>();
        times = new HashMap<String, Date>();
    }

    @Override
    public String toString() {
        return "SemanticNode{" +
                "name='" + name + '\'' +
                '}';
    }
}

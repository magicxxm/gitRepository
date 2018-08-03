
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laptop-6 on 2017/12/15.
 */
public class TestMapGetNull {
    public static void main(String[] args) {

        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put(1, 2);
        System.out.println("map get null - - > > "+objectMap.get(null));

    }
}

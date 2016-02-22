package quara.test_login;

import android.app.Application;
import android.test.ApplicationTestCase;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void testAddToQueue() {
        Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();
        names_on_queue.put("ALBERT", true);
        boolean containsAlbert = names_on_queue.containsKey("ALBERT");
        assertEquals(containsAlbert,true);
    }

    @Test
    public void testDeleteFromQueue() {
        Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();
        names_on_queue.put("ALBERT",true);
        names_on_queue.remove("ALBERT");
        boolean containsAlbert = names_on_queue.containsKey("ALBERT");
        assertEquals(containsAlbert,false);
    }

    @Test
    public void testEditFromQueue() {
        Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();
        names_on_queue.put("ALBERT",true);
        names_on_queue.put("ALBERT",false);
        boolean containsFalse = names_on_queue.get("ALBERT");
        assertEquals(containsFalse,false);
    }


}


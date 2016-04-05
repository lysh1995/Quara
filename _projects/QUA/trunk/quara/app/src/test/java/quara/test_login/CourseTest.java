package quara.test_login;

/**
 * Created by ylin9 on 4/5/2016.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class CourseTest {

    @Test
    public void customer_constructorTest()
    {
        Course course = new Course("cs241", "good");
        assertEquals("cs241", course.getCourse_name());
        assertEquals("good", course.getDescription());
    }

}

package quara.test_login;

/**
 * Created by ylin9 on 4/5/2016.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void customer_constructorTest()
    {
        User user = new User("aaa", "bbb", "ccc");
        assertEquals("aaa", user.getName());
        assertEquals("bbb", user.getUsername());
        assertEquals("ccc", user.getPassword());
    }

}

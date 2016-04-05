package quara.test_login;

/**
 * Created by ylin9 on 4/5/2016.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class GradeTest {

    @Test
    public void default_constructorTest()
    {
        Grade grade = new Grade();
        assertEquals(-1.0, grade.getScore(),0.0);
    }

    @Test
    public void custer_constructorTest()
    {
        Grade grade = new Grade("aaa", 100, "bbb");
        assertEquals(100.0, grade.getScore(), 0.0);
    }

}

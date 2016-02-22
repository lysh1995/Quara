package quara.test_login;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;


public class LoginTest extends ActivityInstrumentationTestCase2<Login>{
    public LoginTest(){
        super(Login.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testAuthenticateSuccess(){
        User user = new User("testpzhao12","testpzhao12","testpzhao12");
       // this.getActivity().authenticate(user);
        assertEquals(true, this.getActivity().userLocalStore.getUserLoggedIn());
    }

}

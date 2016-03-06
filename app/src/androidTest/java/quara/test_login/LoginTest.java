package quara.test_login;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.HashMap;
import java.util.Map;

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
        User user = new User("5pm","5pm","5pm");
        this.getActivity().authenticate(user);
        assertEquals(true, this.getActivity().userLocalStore.getUserLoggedIn());
    }

    @SmallTest
    public void testServerRequestForLogin(){
        ServerRequests sr = new ServerRequests(this.getActivity());
        User user = new User("testUserRegister","testUserRegister","testUserRegister");
        sr.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnUser) {
                getActivity().logUserIn(returnUser);
            }
        });
        assertEquals(true, this.getActivity().userLocalStore.getUserLoggedIn());
    }

    @SmallTest
    public void testServerRequestEncode(){
        ServerRequests sr = new ServerRequests(this.getActivity());
        Map<String, String> data = new HashMap<String, String>();
        data.put("name","testname");
        data.put("username","testusername");
        data.put("password", "testpassword");
        assertEquals("username=testusername&name=testname&password=testpassword", sr.getEncodedData(data));
    }
}

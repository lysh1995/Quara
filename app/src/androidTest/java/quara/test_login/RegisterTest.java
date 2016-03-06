package quara.test_login;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

public class RegisterTest extends ActivityInstrumentationTestCase2<Register> {
    public RegisterTest(){
        super(Register.class);
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
    public void testRegisterAuthenticate(){
        User user = new User("testpzhao12","testpzhao12","testpzhao12");
        this.getActivity().authenticate(user);
    }

    @SmallTest
    public void testRegisterUser(){
        User user = new User("testUserRegister1","testUserRegister1","testUserRegister");
        this.getActivity().RegisterUser(user);
    }

    @SmallTest
    public void testServerRequestForRegisterCheckSuccess(){
        ServerRequests sr = new ServerRequests(this.getActivity());
        User user = new User("testUserRegister","testUserRegister","testUserRegister");
        sr.checkUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnUser) {
                assertNotNull(returnUser);
            }
        });
    }

    @SmallTest
    public void testServerRequestForRegisterCheckFail(){
        ServerRequests sr = new ServerRequests(this.getActivity());
        User user = new User("testpzhao12","testpzhao12","testpzhao12");
        sr.checkUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnUser) {
                assertNull(returnUser);
            }
        });
    }

    @SmallTest
    public void testServerRequestForRegister(){
        ServerRequests sr = new ServerRequests(this.getActivity());
        User user = new User("testpzhao12","testpzhao12","testpzhao12");
        sr.storeUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnUser) {
                assertNotNull(returnUser);
            }
        });
    }


}

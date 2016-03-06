package quara.test_login;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest(){
        super(MainActivity.class);
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
    public void testUserLocalStoreStoreData(){
        UserLocalStore usl = getActivity().userLocalStore;
        User usr = new User("test name","test user","user pwd");
        usl.storeUserData(usr);
        assertNotNull(usl.getLoggedInUser());
        User user = usl.getLoggedInUser();
        assertEquals("test name", user.name);
        usl.setUserLoggedIn(true);
        assertTrue(usl.getUserLoggedIn());
        usl.setUserLoggedIn(false);
        assertFalse(usl.getUserLoggedIn());
        usl.clearUserData();
        assertEquals("",usl.getLoggedInUser().name);
    }

    @SmallTest
    public void testNotes() {
        String notes = "How do we pipeline this code?";
        Queue testQueue = new Queue("John", "1310", "Tomasulo", "CS433", notes);
        assertEquals(testQueue.user_notes, notes);
    }
}

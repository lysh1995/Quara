package quara.test_login;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.ArrayList;

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
        Question testQueue = new Question("John", "1310", "Tomasulo", "CS433", notes);
        assertEquals(testQueue.user_notes, notes);
    }

    @SmallTest
    public void testGetNavItemCount() {
        MainActivity.NavItem item1 = new MainActivity().new NavItem("Test1", "Test1 content", 0);
        MainActivity.NavItem item2 = new MainActivity().new NavItem("Test2", "Test2 content", 1);
        ArrayList<MainActivity.NavItem> list = new ArrayList<MainActivity.NavItem>();
        list.add(item1);
        list.add(item2);
        MainActivity.DrawerListAdapter adapter = new MainActivity().new DrawerListAdapter(getActivity(), list);
        assertEquals(adapter.getCount(), 2);

    }

    @SmallTest
    public void testGetNavItem() {
        MainActivity.NavItem item1 = new MainActivity().new NavItem("Test1", "Test1 content", 0);
        MainActivity.NavItem item2 = new MainActivity().new NavItem("Test2", "Test2 content", 1);
        ArrayList<MainActivity.NavItem> list = new ArrayList<MainActivity.NavItem>();
        list.add(item1);
        list.add(item2);
        MainActivity.DrawerListAdapter adapter = new MainActivity().new DrawerListAdapter(getActivity(), list);
        assertEquals(adapter.getItem(0), item1);
        assertEquals(adapter.getItem(1), item2);
    }
}

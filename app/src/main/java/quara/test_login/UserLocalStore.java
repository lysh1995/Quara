package quara.test_login;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name",user.name);
        spEditor.putString("name",user.username);
        spEditor.putString("name",user.password);
        spEditor.commit();
    }

    public User getLoggedInUser()
    {
        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username","");
        String password = userLocalDatabase.getString("password","");

        User storeduser = new User(name,username,password);
        return storeduser;
    }

    public boolean getUserLoggedIn()
    {
        if (userLocalDatabase.getBoolean("loggedIn", false))
            return true;
        else
            return false;
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}

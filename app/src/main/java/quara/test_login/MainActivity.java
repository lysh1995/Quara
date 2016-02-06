package quara.test_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogout;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    private boolean authenticate()
    {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails()
    {
        User user = userLocalStore.getLoggedInUser();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (authenticate())
        {
            // if user logged in, it will run follow methods
            displayUserDetails();
        }
        else
        {
            startActivity(new Intent(MainActivity.this, Login.class));
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bLogout:

                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

}

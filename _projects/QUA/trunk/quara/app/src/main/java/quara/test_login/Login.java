package quara.test_login;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;

    UserLocalStore userLocalStore;

    final Context temp = this;
    View vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    private void showErrorMessage()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect username or password");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnUser)
    {

        userLocalStore.storeUserData(returnUser);
        userLocalStore.setUserLoggedIn(true);

        //Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTRATION");
        //registrationIntent.putExtra("app", PendingIntent.getBroadcast(vt.getContext(), 0, new Intent(), 0));
        //registrationIntent.putExtra("sender","470822730050");
        //startService(registrationIntent);

        startActivity(new Intent(this, MainActivity.class));
    }

    public void authenticate(User user)
    {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnUser) {
                if (returnUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnUser);
                    TA ta = new TA(returnUser.name,"");
                    ServerRequests serverRequest = new ServerRequests(temp);
                    serverRequest.setOnDutyInBackground(ta, new UpdateDutyCallBack() {
                        @Override
                        public void done(String returnTA) {
                            return;
                        }
                    });
                }
            }
        });
        serverRequests = new ServerRequests(this);
    }

    //Click will trigger this function
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User(username, password);
                vt = v;
                authenticate(user);

                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

}

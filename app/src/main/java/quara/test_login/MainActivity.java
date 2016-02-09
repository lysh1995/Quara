package quara.test_login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Context temp = this;
    Spinner cSpinner;
    Button bLogout;
    TextView tv;

    Map<String, String> course_list;

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

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void displayUserDetails()
    {
        User user = userLocalStore.getLoggedInUser();
        List<String> course_name_list = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course_name_list);
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.getAllCourseInBackground(user, new GetCourseCallBack() {
            @Override
            public void done(Map returnCourse) {
                Map<String, String> rC = returnCourse;
                List<String> course_name_list = new ArrayList<String>();
                for (String key : rC.keySet()) {
                    course_name_list.add(key);
                }
                adapter.addAll(course_name_list);
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cSpinner = (Spinner) findViewById(R.id.course_spinner);
        cSpinner.setAdapter(adapter);

        cSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        Course selected_course = new Course(selected, "");
                        ServerRequests serverRequests = new ServerRequests(temp);
                        serverRequests.getCourseDescriptionInBackground(selected_course, new GetDescriptionCallBack() {
                            @Override
                            public void done(String returnDescription) {
                                //this is the place that can be used to create question queue
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                TextView tv = new TextView(temp);
                                tv.setText(returnDescription);
                                tv.setId(0);
                                tv.setTextColor(Color.parseColor("#000000"));
                                linearLayout.removeAllViews();
                                linearLayout.addView(tv);
                            }
                        });
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        showToast("Spinner1: unselected");
                    }
                });

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

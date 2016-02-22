package quara.test_login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static Context tt;
    final Context temp = this;
    public static Spinner cSpinner;
    public static LinearLayout lyout1;
    public static LinearLayout lyout2;
    Button bLogout;
    Button add;
    Button delete;
    Button answer;

    EditText text1;
    EditText text2;
    String first;
    TextView text3;
    String selected;

    final String MODIFY_QUEUE_STRING = "Modify Queue";

    // Keeps track of names that are already in the queue. Prevents user from submitting more than one quests.
    Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();

    Map<String, String> course_list;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        answer = (Button) findViewById(R.id.answer);
        answer.setOnClickListener(this);

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
        cSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                        layout.removeAllViews();
                        selected = parent.getItemAtPosition(position).toString();
                        Course selected_course = new Course(selected, "");
                        Queue selected_queue = new Queue("","","",selected);
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
                        serverRequests = new ServerRequests(temp);
                        serverRequests.getQueueInBackground(selected_queue, new GetQueueCallBack() {
                            @Override
                            public void done(ArrayList returnQueue) {
                                Iterator<ArrayList> iterator = returnQueue.iterator();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                while (iterator.hasNext()) {
                                    Map entry = (Map) iterator.next();
                                    TextView tv = new TextView(temp);
                                    Map result = entry;
                                    tv.setText("student name: "+ result.get("user_name")+ " position: "+ result.get("user_pos")+ " topic: "+ result.get("user_topic"));
                                    tv.setId(0);
                                    tv.setTextColor(Color.parseColor("#000000"));
                                    linearLayout.addView(tv);
                                }
                            }
                        });

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        showToast("Spinner1: unselected");
                    }
                });
        cSpinner.setAdapter(adapter);

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
            case R.id.add:
                //get text of add button
                final Button addButton = (Button)v;
                String buttonText = addButton.getText().toString();
                final boolean edit = buttonText == MODIFY_QUEUE_STRING;

                tt = temp;
                LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                layout.removeAllViews();
                text1 = new EditText(temp);
                text1.setLayoutParams(new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text1.setHint("user_position");
                layout.addView(text1);
                text2 = new EditText(temp);
                text2.setLayoutParams(new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text2.setHint("user_topic");
                layout.addView(text2);

                //create new submit button
                Button b = new Button(temp);
                if (edit) {
                    b.setText("edit");
                }
                else {
                    b.setText("submit");
                }
                b.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = userLocalStore.getLoggedInUser().name;
                                LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                lyout1 = layout;
                                String pos = text1.getText().toString();
                                String topic = text2.getText().toString();
                                Queue queue = new Queue(name, pos, topic, selected);
                                ServerRequests serverRequests = new ServerRequests(temp);

                                if (!edit) { //insert into queue

                                    showToast("Add to queue" + name);
                                    names_on_queue.put(name, true);

                                    //change the add button text
                                    addButton.setText(MODIFY_QUEUE_STRING);



                                    serverRequests.insertQueueInBackground(queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                            lyout1 = layout;
                                            layout.removeAllViews();
                                            Course selected_course = new Course(selected, "");
                                            Queue selected_queue = new Queue("", "", "", selected);
                                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                            lyout2 = linearLayout;
                                            linearLayout.removeAllViews();
                                            Intent intent = new Intent(temp, MyReceiverAdd.class);
                                            intent.setAction("com.pycitup.BroadcastReceiverAdd");
                                            sendBroadcast(intent);
                                        }
                                    });
                                }
                                else { //edit queue
                                    serverRequests.editQueueInBackground(queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                            lyout1 = layout;
                                            layout.removeAllViews();
                                            Course selected_course = new Course(selected, "");
                                            Queue selected_queue = new Queue("", "", "", selected);
                                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                            lyout2 = linearLayout;
                                            linearLayout.removeAllViews();
                                            Intent intent = new Intent(temp, MyReceiverAdd.class);
                                            intent.setAction("com.pycitup.BroadcastReceiverAdd");
                                            sendBroadcast(intent);
                                        }
                                    });

                                }
                            }
                        }
                );
                layout.addView(b);
                break;
            case R.id.delete:
                //Change the other button's text back to add
                Button button = (Button)findViewById(R.id.add);
                button.setText("Add to queue");

                tt = temp;
                // we remove the name if they remove themselves from the queue
                names_on_queue.remove(userLocalStore.getLoggedInUser().name);
                layout = (LinearLayout) findViewById(R.id.user_info_form);
                layout.removeAllViews();
                String name = userLocalStore.getLoggedInUser().name;
                Queue queue = new Queue(name,"","",selected);
                ServerRequests serverRequests = new ServerRequests(temp);
                serverRequests.deleteQueueInBackground(queue, new GetQueueCallBack() {
                    @Override
                    public void done(ArrayList returnQueue) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                        lyout1 = layout;
                        layout.removeAllViews();
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                        lyout2 = linearLayout;
                        linearLayout.removeAllViews();
                        Intent intent = new Intent(temp, MyReceiverDelete.class);
                        intent.setAction("com.pycitup.BroadcastReceiverDelete");
                        sendBroadcast(intent);
                    }
                });
                break;
            case R.id.answer:
                tt = temp;
                layout = (LinearLayout) findViewById(R.id.answer_form);
                //get the name of the person who is at front of queue so we can answer his question
                final Queue sel_queue = new Queue("","","",selected);
                serverRequests = new ServerRequests(temp);
                serverRequests.getQueueInBackground(sel_queue, new GetQueueCallBack() {
                    @Override
                    public void done(ArrayList returnQueue) {
                        Iterator<ArrayList> iterator = returnQueue.iterator();
                        LinearLayout linlayout = (LinearLayout) findViewById(R.id.answer_form);

                        if(iterator.hasNext()){
                            Map entry = (Map) iterator.next();
                            Map res = entry;
                            text3 = new TextView(temp);
                            String name = (String) res.get("user_name"); //name of first person in queue
                            first = name;
                            text3.setText("Answering " + name + "'s Question.....");
                            text3.setId(0);
                            text3.setTextColor(Color.parseColor("#000000"));
                            linlayout.removeAllViews();
                            linlayout.addView(text3);
                            Button b = new Button(temp);
                            b.setText("Finish Answering");
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // we remove the first question in Queue if T.A. has answered it.

                                    ServerRequests serverRequests = new ServerRequests(temp);
                                    serverRequests.deleteQueueInBackground(sel_queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            names_on_queue.remove(sel_queue.user_name);
                                            LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                            layout.removeAllViews();
                                            LinearLayout answer_layout = (LinearLayout) findViewById(R.id.answer_form);
                                            answer_layout.removeAllViews();
                                            layout = (LinearLayout) findViewById(R.id.user_info_form);
                                            lyout1 = layout;
                                            layout.removeAllViews();
                                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                            lyout2 = linearLayout;
                                            linearLayout.removeAllViews();
                                            Intent intent = new Intent(temp, MyReceiverDelete.class);
                                            intent.setAction("com.pycitup.BroadcastReceiverDelete");
                                            sendBroadcast(intent);
                                        }
                                    });
                                }
                            });
                            linlayout.addView(b);
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "No Question in Queue.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                layout.removeAllViews();
                break;
        }
    }

}

package quara.test_login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pushbots.push.Pushbots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String SERVER_API_KEY = "AIzaSyBxki27xuo5kB-7er64dN2t2mqtPnWZvio";
    private final String SERVER_ID = "79491460392";

    String Project_number = "470822730050";

    static Context tt;
    final Context temp = this;
    public static Spinner cSpinner;
    public static LinearLayout lyout1;
    public static LinearLayout lyout2;
    Button bLogout;
    Button add;
    Button delete;
    Button answer;

    Boolean TA_status = false;

    EditText text1;
    EditText text2;
    String first;
    TextView text3;
    TextView countdown;
    String selected;

    final String MODIFY_QUEUE_STRING = "Modify Queue";

    // Keeps track of names that are already in the queue. Prevents user from submitting more than one quests.
    Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();

    Map<String, String> course_list;

    UserLocalStore userLocalStore;

    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }.execute(null, null, null);
    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisInFinished)
        {
            long mills = millisInFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mills),
                    TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mills)),
                    TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mills)));
            countdown.setText(hms);
        }

        public void onFinish(){
            Vibrator v = (Vibrator) temp.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        }

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

                        TA ta = new TA("",selected);
                        serverRequests = new ServerRequests(temp);
                        serverRequests.getOnDutyTAInBackground(ta, new getOnDutyCallBack() {
                            @Override
                            public void done(String[] ta_list) {
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                TextView tv = new TextView(temp);
                                tv.setText("On-Duty Staff:");
                                tv.setTextColor(Color.parseColor("#000000"));
                                linearLayout.addView(tv);
                                linearLayout = (LinearLayout) findViewById(R.id.couse_queue_form);
                                for (int i=0;i<5;i++) {
                                    if (ta_list[i] != null && !ta_list[i].equals("null")) {
                                        tv = new TextView(temp);
                                        tv.setText(ta_list[i]);
                                        tv.setTextColor(Color.parseColor("#000000"));
                                        linearLayout.addView(tv);
                                    }
                                }
                            }
                        });

                        TA check_ta = new TA(userLocalStore.getLoggedInUser().name,selected);
                        serverRequests = new ServerRequests(temp);
                        serverRequests.CheckAuthorisationInBackground(check_ta, new CheckAuthorisationCallBack() {
                            @Override
                            public void done(String ta_info) {
                                System.out.println("ta_info "+ ta_info);
                                //this is the place that can be used to create question queue
                                if (!ta_info.equals(""))
                                    TA_status = true;
                                else
                                    TA_status = false;
                                if (TA_status) {
                                    Button answer = new Button(temp);
                                    answer.setText("ANSWER QUESTION");
                                    answer.setOnClickListener(
                                            new View.OnClickListener(){
                                                @Override
                                                public void onClick(View v) {
                                                    tt = temp;
                                                    LinearLayout layout = (LinearLayout) findViewById(R.id.answer_form);
                                                    //get the name of the person who is at front of queue so we can answer his question
                                                    Queue sel_queue = new Queue("", "", "", selected);
                                                    ServerRequests serverRequests = new ServerRequests(temp);
                                                    serverRequests.getQueueInBackground(sel_queue, new GetQueueCallBack() {
                                                        @Override
                                                        public void done(ArrayList returnQueue) {
                                                            Iterator<ArrayList> iterator = returnQueue.iterator();
                                                            LinearLayout linlayout = (LinearLayout) findViewById(R.id.answer_form);
                                                            if (iterator.hasNext()) {
                                                                Map entry = (Map) iterator.next();
                                                                Map res = entry;
                                                                text3 = new TextView(temp);
                                                                final String name = (String) res.get("user_name"); //name of first person in queue
                                                                first = name;
                                                                text3.setText("Answering " + name + "'s Question.....");
                                                                text3.setId(0);
                                                                text3.setTextColor(Color.parseColor("#000000"));
                                                                linlayout.removeAllViews();
                                                                linlayout.addView(text3);

                                                                countdown = new TextView(temp);
                                                                countdown.setText("00:08:00");
                                                                countdown.setTextSize(20);
                                                                countdown.setTextColor(Color.parseColor("#000000"));

                                                                final CounterClass timer = new CounterClass(480000,1000);
                                                                timer.start();

                                                                LinearLayout linelayout = (LinearLayout) findViewById(R.id.countdown);
                                                                linelayout.addView(countdown);

                                                                Button b = new Button(temp);
                                                                b.setText("Finish Answering");
                                                                b.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        // we remove the first question in Queue if T.A. has answered it.
                                                                        LinearLayout linelayout = (LinearLayout) findViewById(R.id.countdown);
                                                                        linelayout.removeAllViews();

                                                                        names_on_queue.remove(name);
                                                                        LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                                                        layout.removeAllViews();
                                                                        Queue queue = new Queue(name, "", "", selected);

                                                                        ServerRequests serverRequests = new ServerRequests(temp);
                                                                        serverRequests.deleteQueueInBackground(queue, new GetQueueCallBack() {
                                                                            @Override
                                                                            public void done(ArrayList returnQueue) {
                                                                                LinearLayout answer_layout = (LinearLayout) findViewById(R.id.answer_form);
                                                                                answer_layout.removeAllViews();
                                                                                LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                                                                lyout1 = layout;
                                                                                layout.removeAllViews();
                                                                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_student_form);
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
                                                            } else {
                                                                Toast toast = Toast.makeText(getApplicationContext(), "No Question in Queue.", Toast.LENGTH_LONG);
                                                                toast.show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                    );
                                    LinearLayout linlayout = (LinearLayout) findViewById(R.id.answer_bottom);
                                    linlayout.removeAllViews();
                                    linlayout.addView(answer);
                                }
                                else
                                {
                                    LinearLayout linlayout = (LinearLayout) findViewById(R.id.answer_bottom);
                                    linlayout.removeAllViews();
                                }
                            }
                        });


                        serverRequests = new ServerRequests(temp);
                        serverRequests.getQueueInBackground(selected_queue, new GetQueueCallBack() {
                            @Override
                            public void done(ArrayList returnQueue) {
                                Iterator<ArrayList> iterator = returnQueue.iterator();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_student_form);
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
                TA ta = new TA(userLocalStore.getLoggedInUser().name,"");
                ServerRequests serverRequest = new ServerRequests(temp);
                serverRequest.setOffDutyInBackground(ta, new UpdateDutyCallBack() {
                    @Override
                    public void done(String returnTA) {
                        return;
                    }
                });
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

                                    //change the add button
                                    // text
                                    addButton.setText(MODIFY_QUEUE_STRING);

                                    serverRequests.insertQueueInBackground(queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            LinearLayout layout = (LinearLayout) findViewById(R.id.user_info_form);
                                            lyout1 = layout;
                                            layout.removeAllViews();
                                            Course selected_course = new Course(selected, "");
                                            Queue selected_queue = new Queue("", "", "", selected);
                                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_student_form);
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
                                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_student_form);
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
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.couse_student_form);
                        lyout2 = linearLayout;
                        linearLayout.removeAllViews();
                        Intent intent = new Intent(temp, MyReceiverDelete.class);
                        intent.setAction("com.pycitup.BroadcastReceiverDelete");
                        sendBroadcast(intent);
                    }
                });
                break;
        }
    }

}

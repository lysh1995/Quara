package quara.test_login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Activity act = this;
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
    EditText notesText;
    String first;
    TextView text3;
    TextView countdown;
    String selected;
    HashMap<String, String> taMap;

    final String MODIFY_QUEUE_STRING = "Modify Queue";

    // Keeps track of names that are already in the queue. Prevents user from submitting more than one quests.
    Map<String, Boolean> names_on_queue = new HashMap<String, Boolean>();

    UserLocalStore userLocalStore;

    Context context;
    String regId;
    ShareExternalServer appUtil;
    AsyncTask<Void, Void, String> shareRegidTask;

    static final String TAG = "Register Activity";

    /*add for drawer */
    private static String TAG2 = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    public EditText createEditText(String hint)
    {
        EditText text = new EditText(temp);
        text.setLayoutParams(new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setHint(hint);
        return text;
    }

    public void setView()
    {
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

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*add for drawer*/
        mNavItems.add(new NavItem("Quara", "Make request for OH", R.drawable.ic_launcher));
        mNavItems.add(new NavItem("Grade Center", "Check grades", R.drawable.ic_grade));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.login_form);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /*add for drawer end*/
        context = getApplicationContext();

        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    /*
    * Called when a particular item from the navigation drawer
    * is selected.
    * */
    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.user_login_form, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        String temp = mNavItems.get(position).mTitle;
        if (temp.equals("Grade Center"))
        {
            mDrawerLayout.closeDrawer(mDrawerPane);
            startActivity(new Intent(MainActivity.this, GradeActivity.class));
        }
        else if (temp.equals("Quara"))
        {
            mDrawerLayout.closeDrawer(mDrawerPane);
        }

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
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
                        Question selected_queue = new Question("","","",selected);
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
                                if (!ta_info.equals("")) {
                                    TA_status = true;
                                }
                                else
                                    TA_status = false;
                                if (TA_status) {
                                    try {
                                        taMap = jsonToMap(ta_info);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    displayTaOptions(taMap.get("ta_id"));

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
                                linearLayout.removeAllViews();
                                while (iterator.hasNext()) {
                                    Map entry = (Map) iterator.next();
                                    TextView tv = new TextView(temp);
                                    Map result = entry;
                                    //tv.setText("student name: "+ result.get("user_name")+ " position: "+ result.get("user_pos")+ " topic: "+ result.get("user_topic"));
                                    if (!result.get("user_notes").equals(""))
                                        tv.setText("student name: "+ result.get("user_name")+ " position: "
                                            + result.get("user_pos")+ " topic: "+ result.get("user_topic") + " notes: " + result.get("user_notes"));
                                    else
                                        tv.setText("student name: "+ result.get("user_name")+ " position: "
                                                + result.get("user_pos")+ " topic: "+ result.get("user_topic"));
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

    private void displayTaOptions(final String ta_id) {
        Button answer = new Button(temp);
        answer.setText("ANSWER QUESTION");
        answer.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        tt = temp;
                        LinearLayout layout = (LinearLayout) findViewById(R.id.answer_form);
                        //get the name of the person who is at front of queue so we can answer his question
                        Question sel_queue = new Question("", "", "", selected);
                        ServerRequests serverRequests = new ServerRequests(temp);
                        //Log.i("before server req", "");
                        serverRequests.getQueueInBackground(sel_queue, new GetQueueCallBack() {
                            @Override
                            public void done(ArrayList returnQueue) {
                                Iterator<ArrayList> iterator = returnQueue.iterator();
                                LinearLayout lineLayout = (LinearLayout) findViewById(R.id.answer_form);
                                if (iterator.hasNext()) {
                                    Map entry = (Map) iterator.next();
                                    Map res = entry;
                                    while (!res.get("ta_id").equals("")){
                                        if (iterator.hasNext()){
                                            entry = (Map) iterator.next();
                                            res = entry;
                                        }else{
                                            return;
                                        }
                                    }

                                    final String name = (String) res.get("user_name"); //name of first person in queue
                                    first = name;

                                    Question queue = new Question(name, "", "", selected);
                                    queue.setAnswering(ta_id);
                                    ServerRequests serverRequests = new ServerRequests(temp);
                                    serverRequests.answerQuestion(queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            MainActivity.this.setView();
                                        }
                                    });

                                    text3 = new TextView(temp);
                                    text3.setText("Answering " + name + "'s Question.....");
                                    text3.setId(0);
                                    text3.setTextColor(Color.parseColor("#000000"));
                                    lineLayout.removeAllViews();
                                    lineLayout.addView(text3);

                                    countdown = new TextView(temp);
                                    countdown.setText("00:08:00");
                                    countdown.setTextSize(20);
                                    countdown.setTextColor(Color.parseColor("#000000"));

                                    final CounterClass timer = new CounterClass(480000, 1000, act, countdown);
                                    timer.start();

                                    LinearLayout lineLayout2 = (LinearLayout) findViewById(R.id.countdown);
                                    lineLayout2.addView(countdown);

                                    Button b = createFinishAnsweringButton(name);
                                    lineLayout2.addView(b);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No Question in Queue.", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                }
        );
        //Create button for clearing queue
        Button clear = new Button(temp);
        clear.setText("Clear Queue");
        clear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                names_on_queue.clear();
                tt = temp;
                Question queue = new Question("","","",selected);
                ServerRequests serverRequests = new ServerRequests(temp);
                serverRequests.clearQueue(queue, new GetQueueCallBack() {
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
            }
        });


        LinearLayout lineLayout = (LinearLayout) findViewById(R.id.answer_bottom);
        lineLayout.removeAllViews();
        lineLayout.addView(answer);
        lineLayout.addView(clear);
    }

    @NonNull
    private Button createFinishAnsweringButton(final String name) {
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
                Question queue = new Question(name, "", "", selected);

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
        return b;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (authenticate())
        {
            // if user logged in, it will run follow methods
            displayUserDetails();
            appUtil = new ShareExternalServer();

            regId = getIntent().getStringExtra("regId");
            //Add the regId to our database as well
            if (regId.length() != 0) {
                ServerRequests req = new ServerRequests(temp);

                req.insertRegIdInBackground(regId, new GetRegIdCallBack() {
                    @Override
                    public void done(String regId) {
                        //do nothing
                    }
                });
            }

            Log.d("MainActivity", "regId: " + regId);
            final Context context1 = this;
            shareRegidTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String result = appUtil.shareRegIdWithAppServer(context1, regId);
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    shareRegidTask = null;
                    Toast.makeText(getApplicationContext(), result,
                            Toast.LENGTH_LONG).show();
                }

            };
            shareRegidTask.execute(null, null, null);

            TextView userName = (TextView) findViewById(R.id.userName);
            TextView Name = (TextView) findViewById(R.id.desc);
            userName.setText(userLocalStore.getLoggedInUser().name);
            Name.setText(userLocalStore.getLoggedInUser().username);

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

                text1 = createEditText("Location");
                layout.addView(text1);

                text2 = createEditText("Topic");
                layout.addView(text2);

                notesText = createEditText("Comment");
                layout.addView(notesText);

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
                                String notes = notesText.getText().toString();
                                Question queue = new Question(name, pos, topic, selected, notes);
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
                                            MainActivity.this.setView();
                                        }
                                    });
                                }
                                else { //edit queue
                                    serverRequests.editQueueInBackground(queue, new GetQueueCallBack() {
                                        @Override
                                        public void done(ArrayList returnQueue) {
                                            MainActivity.this.setView();
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
                Question queue = new Question(name,"","",selected);
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

    public static HashMap<String, String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }
        return map;
    }

    /* add inner class */
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    /* inner class to bind to the ListView for the sake of population */

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}

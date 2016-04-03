package quara.test_login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GradeActivity extends AppCompatActivity implements View.OnClickListener{

    final Context temp = this;
    Button bLogout;
    UserLocalStore userLocalStore;
    Context context;
    ServerRequests serverRequests;

    static final String TAG = "Register Activity";

    /*add for drawer */
    private static String TAG2 = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    ArrayList<TextView> temp_tv;
    ArrayList<Integer> temp_score;
    ArrayList<String> temp_title;
    int temp_index;

    ArrayList<Entry> entries;
    ArrayList<String> labels;

    LineChart lineChartGraph;

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
        setContentView(R.layout.activity_grade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*add for drawer*/
        mNavItems.add(new NavItem("Quara", "Make request for OH", R.drawable.ic_launcher));
        mNavItems.add(new NavItem("Grade Center", "Check grades", R.drawable.ic_grade));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.grade_form);

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

        userLocalStore = new UserLocalStore(this);

        initGradeList();
        System.out.print("index: "+ temp_index);
        {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.course_recover_form);
            Button recover = new Button(this);
            recover.setText("Refresh Grade");
            recover.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initGradeList();
                        }
                    });
            linearLayout.removeAllViews();
            linearLayout.addView(recover);
        }
    }

    public void redraw()
    {
        LineDataSet dataset = new LineDataSet(entries, "");
        LineData data = new LineData(labels, dataset);
        lineChartGraph.setData(data);
        lineChartGraph.invalidate();
    }

    /**
     * Initialize the listView showing the grades for all users
     */
    protected void initGradeList() {
        serverRequests = new ServerRequests(temp);
        UserLocalStore uls = new UserLocalStore(temp);
        final User curUser = uls.getLoggedInUser();

        serverRequests.getGradeInBackground(curUser, new GetGradeCallBack() {
            @Override
            public void done(ArrayList returnGrades) {
                entries = new ArrayList<>();
                labels = new ArrayList<String>();

                temp_score = new ArrayList<Integer>();
                temp_title = new ArrayList<String>();
                temp_tv = new ArrayList<TextView>();

                Iterator<ArrayList> iterator = returnGrades.iterator();
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.course_grade_form);
                LineChart lineChart = new LineChart(temp);
                lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                lineChartGraph = lineChart;
                linearLayout.removeAllViews();
                int i = 0;
                while (iterator.hasNext()) {
                    temp_index = i;
                    Map entry = (Map) iterator.next();
                    TextView tv = new TextView(temp);
                    SeekBar seekBar = new SeekBar(temp);
                    Map result = entry;
                    tv.setText(result.get("description") + ": " + result.get("score"));
                    tv.setId(0);
                    tv.setTextColor(Color.parseColor("#000000"));
                    System.out.print(tv);
                    temp_tv.add(tv);
                    int score = Integer.parseInt(result.get("score").toString());
                    entries.add(new Entry(score, i));
                    labels.add(result.get("description").toString());
                    temp_score.add(score);
                    temp_title.add(result.get("description").toString());
                    seekBar.setMax(100);
                    seekBar.setProgress(score);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progress = temp_score.get(temp_index);
                        int id = temp_index;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                            progress = progresValue;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            temp_tv.get(id).setText(temp_title.get(id) + " socre: " + progress + "/" + seekBar.getMax());
                            temp_score.set(id, progress);
                            entries.get(id).setVal(progress);
                            redraw();
                        }
                    });
                    linearLayout.addView(tv);
                    linearLayout.addView(seekBar);
                    i++;
                }
                LineDataSet dataset = new LineDataSet(entries, "");
                LineData data = new LineData(labels, dataset);
                lineChart.setData(data);
                lineChart.setDescription("Description");
                lineChart.animateX(2000);
                LinearLayout layout = (LinearLayout) findViewById(R.id.grade_chart_form);
                layout.removeAllViews();
                layout.addView(lineChart);
            }
        });
    }

    /*
    * Called when a particular item from the navigation drawer
    * is selected.
    * */
    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.user_grade_form, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        String temp = mNavItems.get(position).mTitle;
        if (temp.equals("Grade Center"))
        {
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        else if (temp.equals("Quara"))
        {
            mDrawerLayout.closeDrawer(mDrawerPane);
            startActivity(new Intent(GradeActivity.this, MainActivity.class));
        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        TextView userName = (TextView) findViewById(R.id.userName);
        TextView Name = (TextView) findViewById(R.id.desc);
        userName.setText(userLocalStore.getLoggedInUser().name);
        Name.setText(userLocalStore.getLoggedInUser().username);
    }


    @Override
    public void onClick(View v) {
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

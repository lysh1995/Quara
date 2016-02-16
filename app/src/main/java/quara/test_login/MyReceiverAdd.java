package quara.test_login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

public class MyReceiverAdd extends BroadcastReceiver {
    final Context temp = MainActivity.tt;
    public MyReceiverAdd() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when this BroadcastReceiver receives an Intent broadcast.
        Spinner spinner1 = MainActivity.cSpinner;
        Toast.makeText(context, "Add to: " + spinner1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        LinearLayout layout = MainActivity.lyout1;
        layout.removeAllViews();
        String selected = spinner1.getSelectedItem().toString();
        Course selected_course = new Course(selected, "");
        Queue selected_queue = new Queue("","","",selected);
        ServerRequests serverRequests = new ServerRequests(temp);
        serverRequests.getCourseDescriptionInBackground(selected_course, new GetDescriptionCallBack() {
            @Override
            public void done(String returnDescription) {
                //this is the place that can be used to create question queue
                LinearLayout linearLayout = MainActivity.lyout2;
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
            public void done(Map returnQueue) {
                Iterator<Map.Entry<String,Map>> iterator = returnQueue.entrySet().iterator();
                LinearLayout linearLayout = MainActivity.lyout2;
                while (iterator.hasNext()) {
                    Map.Entry<String, Map> entry = (Map.Entry<String, Map>) iterator.next();
                    TextView tv = new TextView(temp);
                    Map result = entry.getValue();
                    tv.setText("student name: " + result.get("user_name") + " position: " + result.get("user_pos") + " topic: " + result.get("user_topic"));
                    tv.setId(0);
                    tv.setTextColor(Color.parseColor("#000000"));
                    linearLayout.addView(tv);
                }
            }
        });
    }
}





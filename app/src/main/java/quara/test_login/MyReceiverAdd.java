package quara.test_login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
        LinearLayout layout = MainActivity.lyout1;
        layout.removeAllViews();
        String selected = spinner1.getSelectedItem().toString();
        Queue selected_queue = new Queue("","","",selected);
        ServerRequests serverRequests;
        serverRequests = new ServerRequests(temp);
        serverRequests.getQueueInBackground(selected_queue, new GetQueueCallBack() {
            @Override
            public void done(ArrayList returnQueue) {
                Iterator<ArrayList> iterator = returnQueue.iterator();
                LinearLayout linearLayout = MainActivity.lyout2;
                while (iterator.hasNext()) {
                    Map entry = (Map) iterator.next();
                    TextView tv = new TextView(temp);
                    Map result = entry;
                    tv.setText("student name: " + result.get("user_name") + " position: " + result.get("user_pos") + " topic: " + result.get("user_topic"));
                    tv.setId(0);
                    tv.setTextColor(Color.parseColor("#000000"));
                    linearLayout.addView(tv);
                }
            }
        });
    }
}

package quara.test_login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when this BroadcastReceiver receives an Intent broadcast.
        System.out.println("jajajaja1");
    }
}





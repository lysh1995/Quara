package quara.test_login;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Maddie on 3/5/16.
 */
public class CounterClass extends CountDownTimer {

    Activity myActivity;
    TextView textview;

    public CounterClass(long millisInFuture, long countDownInterval, Activity activity, TextView view){
        super(millisInFuture, countDownInterval);
        myActivity = activity;
        textview = view;
    }

    public void onTick(long millisInFinished)
    {
        long mills = millisInFinished;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mills),
                TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mills)),
                TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mills)));
        textview.setText(hms);
    }

    public void onFinish(){
        Vibrator v = (Vibrator) myActivity.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

}

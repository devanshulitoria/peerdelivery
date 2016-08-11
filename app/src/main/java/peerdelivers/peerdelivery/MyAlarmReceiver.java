package peerdelivers.peerdelivery;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by iMac on 5/21/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyAlarmReceiver", "onReceiveMethodcalled");
       Intent mServiceIntent = new Intent(context,NotifyService.class);
        context.startService(mServiceIntent);



    }

}

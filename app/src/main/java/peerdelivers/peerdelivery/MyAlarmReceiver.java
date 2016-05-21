package peerdelivers.peerdelivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by iMac on 5/21/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    //public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyAlarmReceiver","devanshu");
        Intent i = new Intent(context, NotifyService.class);
        i.putExtra("foo", "bar");
        context.startService(i);


    }
}

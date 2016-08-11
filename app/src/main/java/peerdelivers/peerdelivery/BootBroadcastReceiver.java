package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by iMac on 5/21/2016.
 */
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, NotifyService.class);
        context.startService(startServiceIntent);

    }
}

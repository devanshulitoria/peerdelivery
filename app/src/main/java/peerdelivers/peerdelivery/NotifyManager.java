package peerdelivers.peerdelivery;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by iMac on 5/13/2016.
 */
public class NotifyManager{
    Context mContext;
    public NotifyManager(Context context){
        mContext=context;
    }



    @SuppressLint("NewApi")
    public void notifying(HashMap<String,String> data){
        Log.e("inside notify", "devanshu");
        Notification notify = null;
        NotificationManager notif = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(mContext);
        // Large icon appears on the left of the notification
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("New Request");
        builder.setContentText("You have a new message");
        //adding LED lights to notification
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);
        builder.setLights(0xff00ff00, 300, 100);
        // Hide the notification after its selected
        builder.setAutoCancel(true);


        builder.setOngoing(false);
        builder.build();
        Intent notificationIntent = new Intent(mContext, ServerNotifications.class);
        PendingIntent pending = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        builder.setContentIntent(pending);
        notify = builder.getNotification();
        notif.notify(0, notify);

    }
}

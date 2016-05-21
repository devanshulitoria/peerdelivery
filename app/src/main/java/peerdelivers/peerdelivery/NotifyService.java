package peerdelivers.peerdelivery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by iMac on 5/21/2016.
 */
public class NotifyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    NotifyManager nm;
    public NotifyService() {
        super("NotifyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Notifyservice","devanshu");

HashMap<String,String> hm=new HashMap<String,String>();
        nm=new NotifyManager(getApplicationContext());
        nm.notifying(hm);
    }
}

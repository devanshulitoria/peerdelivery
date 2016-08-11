package peerdelivers.peerdelivery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by iMac on 5/21/2016.
 */
public class NotifyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    RequestParams params;
    NotifyManager nm;
    String URL;
    static SharedPreferences sharedpreferences;
    static  int fetchFriendsOnlyonce=0;
    static String friendList="0";
    static String prevNotiId="0";
    String count,content,noti_id;
    String TAG="NotifyService";
    public NotifyService() {
        super("NotifyService");
        Log.e("NotifyService", "Inside constructor");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Notifyservice", "Inside on handle intent");
       fetchNotificationFromServers();

    }
    public void getUserFriends(){
        if(!getLocalRegistry().equalsIgnoreCase("0")) {
            SyncHttpClient client = new SyncHttpClient();
            client.get("https://graph.facebook.com/v2.6/me/friends?limit=500&access_token=" + getLocalRegistry(), new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    Log.e("getusersfriends", "onstart");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("getusersfriends", "failure" + responseString);
                    friendList = responseString;
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.e("getusersfriends", "onSuccess" + responseString);

                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        }
    }
    public static String getLocalRegistry(){
        String access_token=sharedpreferences.getString("fbToken","0");
        return access_token;
    }
    public void fetchNotificationFromServers() {
        URL = getResources().getString(R.string.URL) + "/NotifyService.php";
        Log.e(TAG, "URL:" + URL);
        String sessionId = GetSessionCookie.getCookie(getApplicationContext());
        if (!sessionId.equalsIgnoreCase("0")) {
            SyncHttpClient myClient = new SyncHttpClient();
            myClient.setTimeout(10);
            params = new RequestParams();
            myClient.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
            Log.e(TAG, sessionId);
            myClient.post(URL, params, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e(TAG, "HTTP STARTED");

                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, String obj) {
                    Log.e("NotifyService", "onSuccess " + obj.toString());
                    JSONObject jsonObj;
                    HashMap<String, String> hm = new HashMap<String, String>();
                    try {
                        Log.e(TAG, "INSIDE TRY BLOCK");
                        jsonObj = new JSONObject(String.valueOf(obj));
                        count = jsonObj.getString("COUNT");
                        content = jsonObj.getString("CONTENT");
                        noti_id=jsonObj.getString("last_noti_id");

                        hm.put("count", count);
                        hm.put("content", content);
                        Log.e("count", count);
                        Log.e("content", content);
                        Log.e("noti_id", noti_id);
                        Log.e("prevNotiId", prevNotiId);
                        if (!count.equalsIgnoreCase("0") && !noti_id.equalsIgnoreCase(prevNotiId)) {
                            nm = new NotifyManager(getApplicationContext());
                            nm.notifying(hm);
                            prevNotiId=noti_id;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage().toString());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.e(TAG, "FAIL" + e.toString());
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.e(TAG, "retry" + String.valueOf(retryNo));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.i(TAG, "onFinish" + "OKOKOK");
                }

            });


        }
    }

}

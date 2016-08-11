package peerdelivers.peerdelivery;
/*
notification types
R-> REQUESTED TO CARRY AN ITEM
A->ACCEPTED TO CARRY AN ITEM
N->REJECTED TO CARRY THE ITEM
D->DEFFERED SEARCH RESULT
*/
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

public class ServerNotifications extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private NotificationCustomAdapter nca;
    String TAG="ServerNotifications";
    Button sort,filter,mark;
    AsyncHttpClient myClient1;
    RequestParams params;
    WebView viewWeb;
    static String t_content="0";
    String URL;
    String source,destination,item;
    TextView tv_no_noti_message;
    Typeface custom_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_notifications);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("ServerNotifications"));
        CheckConnection.isConnected(getApplicationContext(), ServerNotifications.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/myriad-set-pro_thin.ttf");
        viewWeb = (WebView)findViewById(R.id.myWebView);
        tv_no_noti_message= (TextView)findViewById(R.id.tv_no_noti_message);
        tv_no_noti_message.setTypeface(custom_font);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        URL=getResources().getString(R.string.URL)+"/ServerNotifications.php";
        sort=(Button)findViewById(R.id.bt_sort);
        filter=(Button)findViewById(R.id.bt_filter);
        mark=(Button)findViewById(R.id.bt_mark_as_read);
        sort.setTypeface(custom_font);
        filter.setTypeface(custom_font);
        mark.setTypeface(custom_font);
        fetchNotificationFromTheServer();

        hm= new LinkedList<HashMap<String, String>>();
        serverNotificatioLV=(ListView) findViewById(R.id.listViewServerNotification);

            sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nca.remove(hm.size());

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void fetchNotificationFromTheServer(){
        {
            myClient1 = new AsyncHttpClient();
            String sessionId=GetSessionCookie.getCookie(getApplicationContext());
            if(!sessionId.equalsIgnoreCase("0")) {
                Log.e("FragmentFriendsActivity", sessionId);
                myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
            }
            params = new RequestParams("noti_data","noti_data");

            myClient1.post(URL, params, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("FRAGMENT ACTIVITY", "HTTP STARTED");

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),
                            "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String str) {
                    // called when response HTTP status is "200 OK"
                    JSONArray peoples = null;
                    JSONObject jso=new JSONObject();
                    Log.e("http response", str);
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject(String.valueOf(str));
                        peoples=jsonObj.getJSONArray("notification_details");
                        Log.e("length",String.valueOf(peoples.length()));
                        HashMap<String,String> tDetail;
                        for(int i=0;i<peoples.length();i++) {
                            JSONObject c = peoples.getJSONObject(i);
                            String noti_id = c.getString("noti_id");
                            String reference_id = c.getString("reference_id");
                            String noti_type=c.getString("noti_type");
                            String noti_status=c.getString("noti_status");
                            String name=c.getString("name");
                            String from_id=c.getString("from_id");
                            String travel_id=c.getString("travel_id");
                            t_content=c.getString("t_content");
                            String profile_pic_url=c.getString("profile_pic_url");
                            String noti_time=c.getString("noti_time");
                            long longTimeAgo    = timeStringtoMilis(noti_time.trim());
                            long now            = System.currentTimeMillis();
                            //longTimeAgo=now-longTimeAgo;
                            Log.e("longTimeAgo",String.valueOf(longTimeAgo));
                            String doj2=(String) DateUtils.getRelativeDateTimeString(ServerNotifications.this, longTimeAgo, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                            Log.e("ago waala time",doj2);
                            DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date src=format12.parse(noti_time);
                            DateFormat ft=new SimpleDateFormat("E dd MMM yyyy");
                            noti_time=ft.format(src);
                            Log.e("doj",noti_time);
                            tDetail=new HashMap<String,String>();
                            String content="";
                            if(noti_type.equalsIgnoreCase("R")){
                                content=name+" has requested you to carry an item.";
                            }
                            else if(noti_type.equalsIgnoreCase("A")){
                                content=name+" has accepted to carry an item.";
                            }
                            else if(noti_type.equalsIgnoreCase("N")){
                                content=name+" has rejected to carry an item.";
                            }
                            else if(noti_type.equalsIgnoreCase("D")){
                                String[] tempStrip=name.split("-");
                                Log.e("servernotification","name->"+name);
                                source=tempStrip[0];
                                destination=tempStrip[1];
                                item=tempStrip[2];
                                Log.e("serverNotification","deffered"+source+"->"+destination+"->"+item);
                                content="We've found a new result for search from "+source+" to "+destination;
                            }
                            else if(noti_type.equalsIgnoreCase("T")){
                                if(!t_content.equalsIgnoreCase("0")) {
                                    Log.e("t_content123", t_content);
                                    String temp[]=t_content.split("-");
                                    content = "Your friend " + name + " is travelling to "+temp[1] +" from "+temp[0];
                                }
                                else{
                                    content="Something went wrong!";
                                }
                            }
                            else if(noti_type.equalsIgnoreCase("U")){
                                content = "Your friend " + name + " is now on PeerDelivery.";
                            }
                            else{
                                content = "We are embarrassed.Something just went wrong ";
                            }
                            tDetail.put("content",content);
                            tDetail.put("time", doj2);
                            tDetail.put("noti_id",noti_id);
                            tDetail.put("reference_id",reference_id);
                            tDetail.put("profilePicURL", profile_pic_url);
                            Log.e("profilePicURL", profile_pic_url);
                            tDetail.put("noti_status", noti_status);
                            tDetail.put("noti_type",noti_type);
                            tDetail.put("from_id",from_id);
                            tDetail.put("t_content",t_content);
                            tDetail.put("travel_id",travel_id);

                            hm.add(tDetail);
                        }

                    }catch(JSONException e) {
                        e.printStackTrace();
                        Log.e("exception", e.getMessage().toString());
                        tv_no_noti_message.setVisibility(View.VISIBLE);
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }

                    populateListView();
//
                }


                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.e("retry", String.valueOf(retryNo));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.i("onFinish", "OKOKOK");
                }

            });
        }
    }
    private long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }
    public void populateListView(){
        viewWeb.setVisibility(View.GONE);
        nca=new NotificationCustomAdapter(ServerNotifications.this,hm);
        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
                String noti_id=(String)map.get("noti_id");
                String noti_type=(String)map.get("noti_type");
                String reference_id=(String)map.get("reference_id");
                String from_user_id=(String)map.get("from_id");
                String tt_content=(String)map.get("t_content");
                String travel_id=(String)map.get("travel_id");
                Log.e("noti_type", noti_type);
                Log.e("noti_id", noti_id);
                Log.e("t_content", tt_content);
                if(noti_type.equalsIgnoreCase("R")) {
                    Log.e(TAG,"INSIDE IF CONDITION");
                    Intent a = new Intent(ServerNotifications.this, Requested.class);
                    a.putExtra("noti_id",noti_id);
                    a.putExtra("reference_id",reference_id);
                    startActivity(a);
                }
                else if(noti_type.equalsIgnoreCase("A")) {
                    Log.e(TAG,"INSIDE ACCEPTED CONDITION");
                    Intent a = new Intent(ServerNotifications.this, activity_accepted.class);
                    a.putExtra("noti_id",noti_id);
                    a.putExtra("reference_id",reference_id);
                    startActivity(a);
                }
                else if(noti_type.equalsIgnoreCase("N")) {
                    Log.e(TAG,"INSIDE REJECTED CONDITION");
                    Intent a = new Intent(ServerNotifications.this, activity_rejected.class);
                    a.putExtra("noti_id",noti_id);
                    a.putExtra("reference_id",reference_id);
                    startActivity(a);
                }
                else if(noti_type.equalsIgnoreCase("D")) {
                    Log.e(TAG, "INSIDE DEFFERED CONDITION");
                    Intent a = new Intent(ServerNotifications.this, SearchResult.class);
                    a.putExtra("source",source);
                    a.putExtra("destination",destination);
                    a.putExtra("item",item);
                    a.putExtra("noti_id",noti_id);
                    startActivity(a);
                }
                else if(noti_type.equalsIgnoreCase("T")) {
                    Log.e(TAG, "INSIDE NEW TRAVEL CONDITION"+tt_content);
                    String temp[]=tt_content.split("-");
                    Intent a = new Intent(ServerNotifications.this, Result.class);
                    a.putExtra("source",temp[0]);
                    a.putExtra("destination",temp[1]);
                    a.putExtra("item",'C');
                    a.putExtra("noti_id",noti_id);
                    a.putExtra("travel_id",travel_id);

                    startActivity(a);
                }
                else if(noti_type.equalsIgnoreCase("U")) {
                    Log.e(TAG, "INSIDE NEW user CONDITION");
                    if(!from_user_id.equalsIgnoreCase("0")) {
                        Intent a = new Intent(ServerNotifications.this, profile.class);
                        a.putExtra("noti_id",noti_id);
                        a.putExtra("facebook_id", from_user_id);
                        startActivity(a);
                    }
                }
//
            }
        });
    }
}

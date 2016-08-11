package peerdelivers.peerdelivery;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

public class Friends extends AppCompatActivity {
    AsyncHttpClient myClient1;
    RequestParams params;
    String URL;
    ListView serverNotificatioLV;
    private NotificationCustomAdapter nca;
    TextView tv_no_noti_message;
    WebView viewWeb;
    private List<HashMap<String,String>> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        CheckConnection.isConnected(getApplicationContext(), Friends.this);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Friends"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        URL=getResources().getString(R.string.URL)+"/Friends.php";
        tv_no_noti_message= (TextView)findViewById(R.id.tv_no_noti_message);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        hm= new LinkedList<HashMap<String, String>>();
        serverNotificatioLV=(ListView) findViewById(R.id.listViewServerNotification);
        fetchFriendsFromTheServer();
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
    public void fetchFriendsFromTheServer() {
        myClient1 = new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("FragmentFriendsActivity", sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        params = new RequestParams("friends","friends");
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
                Log.e("Friends", str);
                JSONArray peoples = null;
                JSONObject jso=new JSONObject();
                Log.e("http response", str);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(str));
                    peoples = jsonObj.getJSONArray("friend_travel_details");
                    Log.e("length", String.valueOf(peoples.length()));
                    HashMap<String, String> tDetail;
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        String count = c.getString("count");
                        String NAME = c.getString("NAME");
                        String WORK = c.getString("WORK");
                        String PROFILE_PIC_URL=c.getString("PROFILE_PIC_URL");
                        String FACEBOOK_ID=c.getString("FACEBOOK_ID");
                        tDetail=new HashMap<String,String>();
                        tDetail.put("content",NAME+"\n"+WORK);

                        if(count.equalsIgnoreCase("1"))
                        tDetail.put("time", count + " journey");
                        else
                        tDetail.put("time", count + " journeys");

                        tDetail.put("profilePicURL",PROFILE_PIC_URL);
                        tDetail.put("facebook_id",FACEBOOK_ID);
                        tDetail.put("noti_status","U");

                        hm.add(tDetail);
                        populateListView();
                    }
                }catch (Exception e){
                    Log.e("Friends",e.getMessage());
                    tv_no_noti_message.setVisibility(View.VISIBLE);
                    viewWeb.setVisibility(View.GONE);
                }
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
    public void populateListView() {
        viewWeb.setVisibility(View.GONE);
        nca = new NotificationCustomAdapter(Friends.this, hm);
        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
                String noti_id="0";
                String facebook_id=(String)map.get("facebook_id");
                Intent a = new Intent(Friends.this, profile.class);
                a.putExtra("noti_id",noti_id);
                a.putExtra("facebook_id", facebook_id);
                startActivity(a);
            }
        });
    }

}

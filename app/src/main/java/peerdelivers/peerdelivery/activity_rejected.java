package peerdelivers.peerdelivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class activity_rejected extends AppCompatActivity {
    Button request,home;
    Intent in;
    String URL;
    String noti_id,request_id;
    AsyncHttpClient myClient;
    RequestParams params;

    TextView work, name,travel_points,doj,request_message,reject_message_tv;
    String TAG="activity_rejected";
    Typeface custom_font;
    WebView viewWeb;
    CircleImageView profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected);
        CheckConnection.isConnected(getApplicationContext(), activity_rejected.this);
        URL=getResources().getString(R.string.URL)+"/activity_rejected.php";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                noti_id= null;
                request_id=null;
            } else {
                noti_id= extras.getString("noti_id");
                request_id= extras.getString("reference_id");
            }
        } else {
            noti_id= (String) savedInstanceState.getSerializable("noti_id");
            request_id= (String) savedInstanceState.getSerializable("reference_id");
        }
        if(noti_id!=null && request_id!=null){
            fetchDataFromTheServer();
        }
        request=(Button)findViewById(R.id.bt_request);
        home=(Button)findViewById(R.id.bt_home);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/myriad-set-pro_thin.ttf");

        profile_pic=(CircleImageView)findViewById(R.id.iv_profilepic);
        profile_pic.setBorderColor(Color.parseColor("#FFFFFF"));
        profile_pic.setBorderWidth(2);
        name=(TextView)findViewById(R.id.textView2);
        work=(TextView)findViewById(R.id.textView3);
        travel_points=(TextView)findViewById(R.id.textView5);
        doj=(TextView)findViewById(R.id.tv_doj);
        request_message=(TextView)findViewById(R.id.tv_displayMessage);
        reject_message_tv=(TextView)findViewById(R.id.reason_for_rejection);
        name.setTypeface(custom_font);
        work.setTypeface(custom_font);
        travel_points.setTypeface(custom_font);
        doj.setTypeface(custom_font);
        request_message.setTypeface(custom_font);
        request.setTypeface(custom_font);
        home.setTypeface(custom_font);
        reject_message_tv.setTypeface(custom_font);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    private void fetchDataFromTheServer() {
        myClient= new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("activity_rejected", sessionId);
            myClient.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        params = new RequestParams();
        Log.e(TAG,"request_id:"+request_id);
        Log.e(TAG,"noti_id:"+noti_id);
        params.put("request_id", request_id);
        params.put("noti_id",noti_id);
        myClient.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("activity_rejected", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("activity_rejected", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                // called when response HTTP status is "200 OK"
                viewWeb.setVisibility(View.GONE);
                JSONArray peoples = null;
                Log.e("activity_rejected", str);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(str));
                    peoples = jsonObj.getJSONArray("rejected");
                    Log.e("length", String.valueOf(peoples.length()));
                    JSONObject c = peoples.getJSONObject(0);
                    String sender_name = c.getString("NAME");
                    String gender=c.getString("GENDER");
                    String request_message_str = "We are very sorry to inform you that " + sender_name + " has refused your request to carry your item!";

                    String sender_work = c.getString("WORK");
                    String profile_pic_url_str = c.getString("PROFILE_PIC_URL");
                    String reason=c.getString("reject_reason");
                    String reason_message;
                    if(reason.equalsIgnoreCase("1"))
                        reason_message="I am no more taking this journey.";
                    else if(reason.equalsIgnoreCase("2"))
                    reason_message="I don't know this person.";
                    else if (reason.equalsIgnoreCase("3"))
                        reason_message="I am simply not interested";
                    else
                        reason_message="I can't carry this particular item.";
                    reject_message_tv.setText(reason_message);
                    String travel_points_str = c.getString("SOURCE") + " to " + c.getString("DESTINATION");
                    String doj_str = c.getString("DOJ");
                    long longTimeAgo = timeStringtoMilis(doj_str);
                    String doj2 = (String) DateUtils.getRelativeDateTimeString(getApplicationContext(), longTimeAgo, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                    name.setText(sender_name);
                    work.setText(sender_work);
                    doj.setText(doj2);
                    request_message.setText(request_message_str);
                    travel_points.setText(travel_points_str);
                    //<todo> remove this in production
                    Log.e(TAG, profile_pic_url_str);
                    if(gender.equalsIgnoreCase("M")) {
                        Picasso.with(getApplicationContext()).load(profile_pic_url_str).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.showman)).error(getApplicationContext().getResources().getDrawable(R.drawable.showman)).into(profile_pic);
                    }
                    else{
                        Picasso.with(getApplicationContext()).load(profile_pic_url_str).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.woman)).error(getApplicationContext().getResources().getDrawable(R.drawable.showman)).into(profile_pic);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception", e.getMessage().toString());
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

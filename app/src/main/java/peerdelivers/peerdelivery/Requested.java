package peerdelivers.peerdelivery;
//coming from servernotification.java
//A-accepted
//N-rejected
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class Requested extends AppCompatActivity {
    CircleImageView profile_pic;
    ImageView item_pic,iv_bigger_image;
    TextView work, name,item_type,travel_points,doj,request_time,request_message;
    String noti_id, reference_id;
    AsyncHttpClient myClient1,myClient2;
    RequestParams params,params2;
    String URL;
    Button accept,reject;
    String TAG="Requested",item_pic_str;
    Typeface custom_font;
    WebView viewWeb;
    String reject_reason="0";
    private PopupWindow pgallery,bigger_image;
    private RelativeLayout ll;
    private RadioGroup itemSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ll=(RelativeLayout)findViewById(R.id.rl);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/myriad-set-pro_thin.ttf");
        accept=(Button)findViewById(R.id.bt_accept);
        reject=(Button)findViewById(R.id.bt_reject);
        profile_pic=(CircleImageView)findViewById(R.id.iv_profilepic);
        profile_pic.setBorderColor(Color.parseColor("#FFFFFF"));
        profile_pic.setBorderWidth(2);
        item_pic=(ImageView)findViewById(R.id.iv_type);
        name=(TextView)findViewById(R.id.textView2);
        work=(TextView)findViewById(R.id.textView3);
        item_type=(TextView)findViewById(R.id.textView4);
        travel_points=(TextView)findViewById(R.id.textView5);
        doj=(TextView)findViewById(R.id.tv_doj);
        request_time=(TextView)findViewById(R.id.pnr);
        request_message=(TextView)findViewById(R.id.tv_displayMessage);

        accept.setTypeface(custom_font);
        reject.setTypeface(custom_font);

        name.setTypeface(custom_font);
        work.setTypeface(custom_font);
        item_type.setTypeface(custom_font);
        travel_points.setTypeface(custom_font);
        doj.setTypeface(custom_font);
        request_time.setTypeface(custom_font);
        request_message.setTypeface(custom_font);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept.setEnabled(false);
                reject.setEnabled(false);
                accept.setBackgroundColor(Color.GREEN);
                sendDataToServer('A');
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject.setEnabled(false);
                accept.setEnabled(false);
                reject.setBackgroundColor(Color.BLACK);
                initiatePopupWindowSort();

            }
        });

        item_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBiggerImage();
            }
        });

        URL=getResources().getString(R.string.URL)+"/Requested.php";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                noti_id = "0";
                reference_id = null;


            } else {
                noti_id = extras.getString("noti_id");
                reference_id = extras.getString("reference_id");

            }
        } else {
            noti_id = (String) savedInstanceState.getSerializable("noti_id");
            reference_id = (String) savedInstanceState.getSerializable("reference_id");
        }
        getDataFromTheServer();
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
    private void openBiggerImage() {
        {
            try {
// We need to get the instance of the LayoutInflater
                LayoutInflater inflater = (LayoutInflater) Requested.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.pop_up_bigger_image,
                        (ViewGroup) findViewById(R.id.popup_bigger_image));
                bigger_image = new PopupWindow(layout, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT, true);
                bigger_image.showAtLocation(layout, Gravity.CENTER, 0, 0);
                bigger_image.setOutsideTouchable(false);
                iv_bigger_image = (ImageView) layout.findViewById(R.id.iv_bigger_image);
                Picasso.with(getApplicationContext()).load(item_pic_str).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.clothes)).error(getApplicationContext().getResources().getDrawable(R.drawable.clothes)).into(iv_bigger_image);
                bigger_image.showAtLocation(ll,Gravity.NO_GRAVITY,0,0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendDataToServer(char b) {
        myClient2 = new AsyncHttpClient();
        params2= new RequestParams();
        params2.put("status",String.valueOf(b));
        params2.put("reference_id",reference_id);
        params2.put("reject_reason",reject_reason);
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("FragmentFriendsActivity", sessionId);
            myClient2.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        myClient2.post(URL, params2, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("Requested", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Requested", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                Log.e("Requested","HTTP reponse:"+str);
                Intent a = new Intent(Requested.this, MainActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                finish();
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

    public void getDataFromTheServer(){
        {
            myClient1 = new AsyncHttpClient();
            params = new RequestParams();
            Log.e(TAG,"reference_id:"+reference_id);
            Log.e(TAG,"noti_id:"+noti_id);
            params.put("reference_id",reference_id);
            params.put("noti_id",noti_id);
            String sessionId=GetSessionCookie.getCookie(getApplicationContext());
            if(!sessionId.equalsIgnoreCase("0")) {
                Log.e("FragmentFriendsActivity", sessionId);
                myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
            }

            myClient1.post(URL, params, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("FRAGMENT ACTIVITY", "HTTP STARTED");

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("FRAGMENT ACTIVITY", "failed");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String str) {
                    // called when response HTTP status is "200 OK"
                    viewWeb.setVisibility(View.GONE);
                    JSONArray peoples = null;
                    JSONObject jso=new JSONObject();
                    Log.e("http response", str);
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject(String.valueOf(str));
                        peoples=jsonObj.getJSONArray("request_details");
                        Log.e("length",String.valueOf(peoples.length()));

                            JSONObject c = peoples.getJSONObject(0);
                            String request_message_str = c.getString("request_message");
                            String request_time_str = c.getString("request_time");
                            c = peoples.getJSONObject(1);
                            String sender_name=c.getString("request_message");
                            String sender_work=c.getString("request_time");
                            String temp[]=sender_work.split("~");
                            sender_work=temp[0];
                            String profile_pic_url_str=temp[1];
                            c = peoples.getJSONObject(2);
                            item_pic_str=c.getString("request_message");
                            String item_type_str=c.getString("request_time");
                            c = peoples.getJSONObject(3);
                            String travel_points_str=c.getString("request_message");
                            String doj_str=c.getString("request_time");
                            String content="";
                            if(item_type_str.equalsIgnoreCase("B")){
                                content="has requested you to carry Books";
                            }
                            else if(item_type_str.equalsIgnoreCase("C")){
                                content="has requested you to carry Books";
                            }
                            else if(item_type_str.equalsIgnoreCase("B")){
                                content="has requested you to carry Clothes";
                            }
                            else if(item_type_str.equalsIgnoreCase("E")){
                                content="has requested you to carry Electronic item";
                            }
                            else if(item_type_str.equalsIgnoreCase("M")){
                                content="has requested you to carry Medicines";
                            }
                            else{
                                content="has requested you to carry other HouseHold item";
                            }
                        c = peoples.getJSONObject(4);
                        String r_status=c.getString("request_message");
                        if(r_status.equalsIgnoreCase("A")){
                            accept.setEnabled(false);
                            accept.setText("Already accepted");
                            reject.setEnabled(false);
                        }
                        else if(r_status.equalsIgnoreCase("N")){
                            reject.setText("Already Rejected");
                            reject.setEnabled(false);
                            accept.setEnabled(false);
                        }
                        long longTimeAgo    = timeStringtoMilis(request_time_str);
                        String request_time2=(String)DateUtils.getRelativeDateTimeString(getApplicationContext(), longTimeAgo, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                        longTimeAgo    = timeStringtoMilis(doj_str);
                        String doj2=(String)DateUtils.getRelativeDateTimeString(getApplicationContext(), longTimeAgo, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
                        name.setText(sender_name);
                        work.setText(sender_work);
                        item_type.setText(content);
                        doj.setText(doj2);
                        request_time.setText(request_time2);
                        request_message.setText(request_message_str);
                        travel_points.setText(travel_points_str);
                        //<todo> remove this in production
                        item_pic_str=item_pic_str.replace("localhost", "192.168.173.1");
                        Log.e(TAG, item_pic_str);
                        Log.e(TAG, profile_pic_url_str);

                        Picasso.with(getApplicationContext()).load(item_pic_str).resize(100, 100).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.clothes)).error(getApplicationContext().getResources().getDrawable(R.drawable.clothes)).into(item_pic);
                        Picasso.with(getApplicationContext()).load(profile_pic_url_str).resize(100, 100).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.showman)).error(getApplicationContext().getResources().getDrawable(R.drawable.showman)).into(profile_pic);



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("exception",e.getMessage().toString());
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

    private void initiatePopupWindowSort(){
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) Requested.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.pop_reject_reason,
                    (ViewGroup) findViewById(R.id.popup_gallery_linear));
            pgallery = new PopupWindow(layout, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT, true);
            pgallery.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pgallery.setOutsideTouchable(false);
            itemSort = (RadioGroup) layout.findViewById(R.id.radioSort);
            itemSort = (RadioGroup) layout.findViewById(R.id.radioSort);
            itemSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId)
                    {
                        case R.id.rb_1:
                            reject_reason="1";
                            break;
                        case R.id.rb_2:
                            reject_reason="2";
                            break;
                        case R.id.rb_3:
                            reject_reason="3";
                            break;
                        case R.id.rb_4:
                            reject_reason="4";
                            break;
                    }

                    pgallery.dismiss();
                    sendDataToServer('N');
                }
            });
            pgallery.showAtLocation(ll,Gravity.NO_GRAVITY,0,0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



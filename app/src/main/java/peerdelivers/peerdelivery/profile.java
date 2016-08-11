package peerdelivers.peerdelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class profile extends AppCompatActivity {
TextView profile_status,place_count,last_travel,name,nothing;
    AsyncHttpClient myClient1;
    private SearchResultCustomAdpater nca;
    private List<HashMap<String,String>> hm;
    String facebook_id,URL,noti_id;
    String gName,gStatus,gPlaceCount,gLast_Travel,gProfilePic;
    RequestParams params;
    JSONArray peoples = null;
    JSONArray user_detail = null;
    CircleImageView profile_pic;
    Button fb_link;
    ListView serverNotificatioLV;
    WebView viewWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("profile"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        nothing=(TextView)findViewById(R.id.tv_nothing_in_profile);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        URL=getResources().getString(R.string.URL)+"/profile.php";
        hm= new LinkedList<HashMap<String, String>>();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                facebook_id= null;
                noti_id=null;

            } else {
                facebook_id= extras.getString("facebook_id");
                noti_id= extras.getString("noti_id");

            }
        } else {
            facebook_id= (String) savedInstanceState.getSerializable("facebook_id");
            noti_id= (String) savedInstanceState.getSerializable("noti_id");
        }

        getDataFromServer();
    }

    public void getDataFromServer(){
        myClient1 = new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(profile.this);
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("profile.java", sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        params = new RequestParams();
        params.put("facebook_id", facebook_id);
        params.put("noti_id", noti_id);
        myClient1.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("profile.java", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),
                        "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {

                Log.e("http response", str);
                viewWeb.setVisibility(View.GONE);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(str));

                    user_detail = jsonObj.getJSONArray("user_detail");

                    for (int i = 0; i < user_detail.length(); i++) {
                        JSONObject c = user_detail.getJSONObject(i);
                        gProfilePic = c.getString("profile_pic_url");

                        gName = c.getString("name");
                        gStatus = "\" " + c.getString("text_status") + " \"";


                        gPlaceCount = "Been to " + c.getString("travel_count") + " places";
                        if (c.getString("last_destination") == null) {
                            gLast_Travel = "Lastest travel destination NA";
                        } else {
                            gLast_Travel = "Lastest journey to " + c.getString("last_destination");
                        }

                    }
                    peoples = jsonObj.getJSONArray("user_travel_details");
                    Log.e("length", String.valueOf(peoples.length()));
                    HashMap<String, String> tDetail;
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        String TRAVEL_ID = c.getString("TRAVEL_ID");
                        String source = c.getString("SOURCE");
                        String destination = c.getString("DESTINATION");
                        String type = c.getString("TYPE_TRANSPORT");
                        String NAME = c.getString("NAME");
                        String DOJ = c.getString("DOJ");
                        String DOJ2 = c.getString("DOJ");
                        DateFormat format12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date src = format12.parse(DOJ);
                        DateFormat ft = new SimpleDateFormat("E dd MMM yyyy");
                        DOJ = ft.format(src);
                        Log.e("doj", DOJ);
                        tDetail = new HashMap<String, String>();
                        tDetail.put("content", source + " to " + destination);
                        tDetail.put("time", DOJ);
                        tDetail.put("travel_id", TRAVEL_ID);
                        tDetail.put("name", NAME);
                        tDetail.put("DOJ2", DOJ2);
                        tDetail.put("profilePicURL", gProfilePic);
                        Log.e("profilePicURL", gProfilePic);
                        tDetail.put("type", type);
                        hm.add(tDetail);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    nothing.setVisibility(View.VISIBLE);
                    Log.e("exception", e.getMessage().toString());

                } catch (ParseException e) {
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
    public void populateListView(){
        serverNotificatioLV=(ListView) findViewById(R.id.lv_search_result);
        nca=new SearchResultCustomAdpater(profile.this,hm);

        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setHapticFeedbackEnabled(true);
        View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header,serverNotificatioLV , false);
        profile_status=(TextView)footerView.findViewById(R.id.profile_status);
        place_count=(TextView)footerView.findViewById(R.id.tv_place_count);
        last_travel=(TextView)footerView.findViewById(R.id.tv_last_time);

        fb_link=(Button)footerView.findViewById(R.id.button_fb);
        name=(TextView)footerView.findViewById(R.id.tv_user_name);
        name.setText(gName);
        last_travel.setText(gLast_Travel);
        place_count.setText(gPlaceCount);
        profile_status.setText(gStatus);
        profile_pic=(CircleImageView)footerView.findViewById(R.id.image_profile);
        profile_pic.setBorderColor(Color.parseColor("#FFFFFF"));
        profile_pic.setBorderWidth(2);
        Picasso.with(getApplicationContext()).load(gProfilePic)
                .placeholder(R.drawable.showman).error(R.drawable.showman)
                .into(profile_pic);
        fb_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + facebook_id));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebook_id)));
                }
            }
        });
        serverNotificatioLV.addHeaderView(footerView);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.e("listItem", parent.getItemAtPosition(position).toString());
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String travel_id = (String) map.get("travel_id");
                String timestamp_doj=(String) map.get("DOJ2");
                long longTimeAgo    = timeStringtoMilis(timestamp_doj.trim());
                long now            = System.currentTimeMillis();
                if(longTimeAgo>now) {
                    String temp_str[] = map.get("content").toString().split("to");
                    Log.e("before start of act:", travel_id + "--" + temp_str[0] + "--" + temp_str[1]);
                    Log.e("travel_id", travel_id);
                    Intent a = new Intent(profile.this, Result.class);
                    a.putExtra("travel_id", travel_id);
                    a.putExtra("source", temp_str[0]);
                    a.putExtra("destination", temp_str[1]);
                    a.putExtra("item_type", 'C');
                    startActivity(a);
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(profile.this);
                    alertDialogBuilder.setTitle("Sorry");
                    alertDialogBuilder
                            .setMessage("This journey is already completed.You cannot place request for past dated journey.")
                            .setCancelable(false)
                            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    alertDialogBuilder.show();
                }
            }


        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

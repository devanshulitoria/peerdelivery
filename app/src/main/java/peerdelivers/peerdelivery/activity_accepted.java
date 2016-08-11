package peerdelivers.peerdelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class activity_accepted extends AppCompatActivity {
    Button call,text;
    Intent in;
    String URL;
    String noti_id,request_id;
    AsyncHttpClient myClient;
    RequestParams params;

    CircleImageView profile_pic;
    ImageView item_pic;
    TextView work, name,item_type,travel_points,doj,request_time,request_message;
    String TAG="activity_accepted";
    Typeface custom_font;
    WebView viewWeb;
    String phNumber;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckConnection.isConnected(getApplicationContext(), activity_accepted.this);
        URL=getResources().getString(R.string.URL)+"/activity_accepted.php";

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


        setContentView(R.layout.activity_activity_accepted);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/myriad-set-pro_thin.ttf");

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

        name.setTypeface(custom_font);
        work.setTypeface(custom_font);
        item_type.setTypeface(custom_font);
        travel_points.setTypeface(custom_font);
        doj.setTypeface(custom_font);
        request_time.setTypeface(custom_font);
        request_message.setTypeface(custom_font);
        call=(Button)findViewById(R.id.bt_call);
        text=(Button)findViewById(R.id.bt_text);
        call.setTypeface(custom_font);
        text.setTypeface(custom_font);
        call.setEnabled(false);
        text.setEnabled(false);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(phNumber);
            }
        });
    }

    private void callOverPhone() {
        in = new Intent(Intent.ACTION_CALL);
        in.setPackage("com.android.server.telecom");
        in.setData(Uri.parse("tel:" + phNumber));
        startActivity(in);
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
    public void checkPermission(){
        int permissioncheck=-1;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            Log.e(TAG,"INSIDE MARSHMELLOW");
            permissioncheck=ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE);
            if(permissioncheck==PackageManager.PERMISSION_GRANTED){

                Log.e(TAG,"PERMISSION GRANTED");
                callOverPhone();
            }
            else{

                Log.e(TAG, "PERMISSION not GRANTED");
                accessPermissions();
            }
        } else {
            // Pre-Marshmallow

            Log.e(TAG, "not marshmellow");
            callOverPhone();

        }

        Log.e("permission", String.valueOf(permissioncheck));
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity_accepted.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void fetchDataFromTheServer() {
        myClient= new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("FragmentFriendsActivity", sessionId);
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
                JSONObject jso = new JSONObject();
                Log.e("http response", str);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(str));
                    peoples = jsonObj.getJSONArray("accepted");
                    Log.e("length", String.valueOf(peoples.length()));
                    JSONObject c = peoples.getJSONObject(0);
                    String sender_name = c.getString("NAME");
                    call.setText("Call " + sender_name);
                    text.setText("Text " + sender_name);
                    call.setEnabled(true);
                    text.setEnabled(true);
                    String request_message_str = "Congratulations " + sender_name + " has accepted to carry your item";
                    phNumber = c.getString("PHONE_NO");

                    String sender_work = c.getString("WORK");
                    String profile_pic_url_str = c.getString("PROFILE_PIC_URL");

                    String item_type_str = c.getString("TYPE_TRANSPORT");

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

                    Picasso.with(getApplicationContext()).load(profile_pic_url_str).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.showman)).error(getApplicationContext().getResources().getDrawable(R.drawable.showman)).into(profile_pic);
                    if (item_type_str.equalsIgnoreCase("B")) {
                        item_pic.setImageResource(R.drawable.bus);
                    } else if (item_type_str.equalsIgnoreCase("T")) {
                        item_pic.setImageResource(R.drawable.train);
                    } else if(item_type_str.equalsIgnoreCase("F")){
                        item_pic.setImageResource(R.drawable.airplane);
                    }
                    else{
                        item_pic.setImageResource(R.drawable.car);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception", e.getMessage().toString());
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

    private void sendSMS(String phNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19
            Log.e("SMS TO",phNumber);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phNumber));
            sendIntent.putExtra("sms_body", "Hey thanks for accepting my request on PeerDelivery!");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",phNumber);
            smsIntent.putExtra("sms_body","Hey thanks for accepting my request on PeerDelivery");
            startActivity(smsIntent);
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
    private void accessPermissions() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity_accepted.this,
                Manifest.permission.CALL_PHONE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity_accepted.this,
                    Manifest.permission.CALL_PHONE)) {
                showMessageOKCancel("You need to allow access to Telephone to call to this number",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity_accepted.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(activity_accepted.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    Log.e(TAG,"ALL PERMISSION GRANTED");
                    callOverPhone();
                } else {
                    // Permission Denied
                    Toast.makeText(activity_accepted.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;

        }
    }
}

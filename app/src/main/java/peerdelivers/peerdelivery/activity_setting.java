package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class activity_setting extends AppCompatActivity {
    EditText et_train,et_plane,et_bus,et_status;
    Switch email;
    String gStatus;
    TextView title,title2,title3,title4,title5,title6;
    SharedPreferences settings;
    Typeface custom_font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Settings"));
        setContentView(R.layout.activity_setting);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        email = (Switch) findViewById(R.id.switch_noti);
        title = (TextView) findViewById(R.id.tv_noti);
        title2 = (TextView) findViewById(R.id.textView16);
        title3 = (TextView) findViewById(R.id.textView17);
        title4 = (TextView) findViewById(R.id.textView18);
        title5 = (TextView) findViewById(R.id.textView19);
        title6 = (TextView) findViewById(R.id.textView20);

        et_train = (EditText) findViewById(R.id.tv_train);
        et_plane = (EditText) findViewById(R.id.tv_airplane);
        et_bus = (EditText) findViewById(R.id.tv_bus);
        et_status = (EditText) findViewById(R.id.edit_status);

        title.setTypeface(custom_font);
        title2.setTypeface(custom_font);
        title3.setTypeface(custom_font);
        title4.setTypeface(custom_font);
        title5.setTypeface(custom_font);
        title6.setTypeface(custom_font);
        et_train.setTypeface(custom_font);
        et_plane.setTypeface(custom_font);
        et_bus.setTypeface(custom_font);

        settings.getInt("email", 0);
        if(settings.getInt("email", 0)==1){
            email.setChecked(true);
        }
        else if(settings.getInt("email", 0)==0){
            email.setChecked(false);
        }
        et_train.setText(String.valueOf(settings.getInt("train", 5)));
        et_plane.setText(String.valueOf(settings.getInt("plane", 2)));
        et_bus.setText(String.valueOf(settings.getInt("bus", 5)));
        et_status.setText(settings.getString("status","Hey there I am using PeerDelivery"));

    }


    @Override
    public void onBackPressed(){

        int t,p,b;
        t=Integer.parseInt(et_train.getText().toString());
        p=Integer.parseInt(et_plane.getText().toString());
        b=Integer.parseInt(et_bus.getText().toString());
        gStatus=et_status.getText().toString();
        if(t<2 || p<2 || b<2){
            Toast.makeText(getApplicationContext(), "You cannot set the weights less then 2kgs", Toast.LENGTH_LONG).show();
        }
        else{
            SharedPreferences.Editor editor = settings.edit();
            if(email.isChecked()) {
                editor.putInt("email", 1);
            }
            else {
                editor.putInt("email", 0);
            }
            editor.putInt("train", t);
            editor.putInt("plane", p);
            editor.putInt("bus", b);
            editor.putString("status", gStatus);
            editor.commit();
            sendToserver();
            Log.e("Srttings", "on back called");
            super.onBackPressed();
        }

    }
    public void sendToserver(){
        AsyncHttpClient myClient1 = new AsyncHttpClient();
        RequestParams params2 = new RequestParams();
        Log.e("status",gStatus);
        params2.put("data",gStatus );
        String URL2=getResources().getString(R.string.URL)+"/activity_setting.php";
        String sessionId=GetSessionCookie.getCookie(activity_setting.this);
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("activity_setting",sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
            //myClient1.setConnectTimeout(30000);
            myClient1.post(URL2, params2, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("activity_Setting", "inside onstart");

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),
                            "Connection to server failed!", Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String str) {
                    Log.e("setting_activity", "on success "+str);

                }


                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    Log.e("setting_activity", String.valueOf(retryNo));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.i("setting_activity", "OKOKOK");
                }

            });
        }
        else{

        }
    }

}

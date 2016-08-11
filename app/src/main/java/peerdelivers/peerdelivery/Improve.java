package peerdelivers.peerdelivery;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Improve extends AppCompatActivity {
    EditText et_love,et_hate;
    TextView message;
    Button send;
    Typeface custom_font;
    final String TAG="Improve.java";
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Improve"));
        setContentView(R.layout.activity_improve);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        URL=getResources().getString(R.string.URL)+"/Improve.php";
        message=(TextView)findViewById(R.id.textView11);
        et_love=(EditText)findViewById(R.id.et_love);
        et_hate=(EditText)findViewById(R.id.et_hate);
        send=(Button)findViewById(R.id.bt_send);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        message.setTypeface(custom_font);
        et_love.setTypeface(custom_font);
        et_hate.setTypeface(custom_font);
        send.setTypeface(custom_font);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_love.getText().length()>10 || et_hate.getText().length()>10) {
                    send.setEnabled(false);
                    send.setText("Sending");
                    sendDataToserver();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Please hate or love before sending", Toast.LENGTH_LONG)
                            .show();
                }
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
    private void sendDataToserver() {
        AsyncHttpClient myClient1 = new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("Improve.java", sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        RequestParams params = new RequestParams();
        params.put("hate",et_hate.getText());
        params.put("love",et_love.getText());

        myClient1.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e(TAG, "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "failed");
                Log.e(TAG, responseString);
                Log.e(TAG, String.valueOf(statusCode));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                Log.e(TAG,str);
                Toast.makeText(getApplicationContext(),
                        "Thanks for your feedback...We will get back to you soon", Toast.LENGTH_LONG)
                        .show();
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

}

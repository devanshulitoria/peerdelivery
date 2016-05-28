package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {
    AsyncHttpClient myClient;
    TextView temp;
    WebView view;
    SharedPreferences cookies;
    String fbID,auth_code;
    SharedPreferences sharedpreferences;
    final String URL="http://192.168.137.1/splashActivity.php";
    RequestParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cookies = this.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        view = (WebView) findViewById(R.id.myWebView);
        view.loadUrl("file:///android_asset/screen.gif");
        view.setBackgroundColor(Color.TRANSPARENT);
        CheckConnection.isConnected(getApplicationContext(), SplashActivity.this);
        view.setVisibility(View.VISIBLE);
        if(CheckPreferences.hasAlreadySignedUp(getApplicationContext()))
        LoginUser();
        else{
            Intent goToNextActivity = new Intent(SplashActivity.this, FacebookLogin.class);
            startActivity(goToNextActivity);
            finish();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void LoginUser() {
        sharedpreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        fbID=sharedpreferences.getString("facebook_id", null);
        auth_code=sharedpreferences.getString("auth_code", null);
        Log.e("inside devanshu", "post request devanshu");

        if(fbID==null || auth_code==null)
        {
            Toast.makeText(getApplicationContext(),
                    "Mother fuck...cookie missing,SplashActivity", Toast.LENGTH_LONG)
                    .show();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        params = new RequestParams();
        params.put("fbID",fbID);
        params.put("auth_code",auth_code);

        myClient = new AsyncHttpClient();



        myClient.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                // called when response HTTP status is "200 OK"
                String s = null;
                Log.e("http response", obj.toString());
                try {
                    s = obj.getString("sessionId").toString();
                    if (!s.equalsIgnoreCase("0")) {
                        SharedPreferences.Editor editor = cookies.edit();
                        editor.putString("phpID", s);
                        editor.commit();
                        Intent goToNextActivity = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(goToNextActivity);
                        finish();

                    } else {
                        Intent goToNextActivity = new Intent(SplashActivity.this, PreStart.class);
                        startActivity(goToNextActivity);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("http response", s);
                view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", e.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("retry", String.valueOf(retryNo));
            }
        });
    }
}

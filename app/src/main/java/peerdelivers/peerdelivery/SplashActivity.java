package peerdelivers.peerdelivery;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {
    AsyncHttpClient myClient;
    TextView temp;
    WebView view;
    final String URL="http://192.168.173.1/splashActivity.php";
    String phNumber="+919706783069";
    String secretCode="hbdvhdbsi7934hbfkde";
    RequestParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        temp=(TextView)findViewById(R.id.result);
        view = (WebView) findViewById(R.id.myWebView);
        view.loadUrl("file:///android_asset/screen.gif");
        view.setBackgroundColor(Color.TRANSPARENT);
        CheckConnection.isConnected(getApplicationContext(), SplashActivity.this);
        registerUser();
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
    private void registerUser() {

        Log.e("inside devanshu", "posst request devanshu");
        params = new RequestParams();
        params.put("phoneNumber",phNumber.substring(3));
        params.put("code",secretCode);

        myClient = new AsyncHttpClient();



        myClient.post( URL,params , new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");

            }



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                // called when response HTTP status is "200 OK"
                String s = obj.toString();
                Log.e("http response", s);
                view.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", errorResponse.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("retry", String.valueOf(retryNo));
            }
        });
    }
}

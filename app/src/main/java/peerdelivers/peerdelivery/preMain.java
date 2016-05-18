package peerdelivers.peerdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class preMain extends AppCompatActivity {
    EditText email;
    EditText userName;
    Button go;
    private static preMain instPreMain;
    AsyncHttpClient client;
    final String URL="http://146.148.22.97/auth.php";
    RequestParams params;
    static String secretCode=PreStart.uuid;
    static String message = PreStart.phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_main);
        CheckConnection.isConnected(getApplicationContext(), preMain.this);
        instPreMain = this;
        go = (Button) findViewById(R.id.btnSendEmail);
        email = (EditText) findViewById(R.id.EditEmail);
        userName=(EditText) findViewById(R.id.userName);
    }
    public static preMain instance() {
        return instPreMain;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pre_main, menu);
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
        params.put("phoneNumber",message.substring(3));
        params.put("code",secretCode);

        client = new AsyncHttpClient();



        client.post( URL,params , new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String s = new String(response);
                Log.e("http response", s);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
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

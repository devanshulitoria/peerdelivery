package peerdelivers.peerdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Result extends AppCompatActivity {
    Button accept,back;
    String travel_id=null;
    String URL;
    AsyncHttpClient client,SendClient;
    RequestParams params;
    JSONArray travel_details = null;
    JSONObject jso=new JSONObject();
    EditText et_message;
    String search_id,messageRequest;
    ImageView iv_profilepic,iv_travel_type;
    TextView tv_name,tv_work,probabilty,travel_city,travel_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);
        URL=getResources().getString(R.string.URL)+"/Result.php";
        iv_profilepic=(ImageView)findViewById(R.id.iv_profilepic);
        iv_travel_type=(ImageView)findViewById(R.id.iv_type);
        tv_name=(TextView)findViewById(R.id.textView2);
        tv_work=(TextView)findViewById(R.id.textView3);
        probabilty=(TextView)findViewById(R.id.textView4);
        travel_city=(TextView)findViewById(R.id.textView5);
        travel_date=(TextView)findViewById(R.id.tv_doj);
        et_message=(EditText)findViewById(R.id.request_box);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                travel_id= null;

            } else {
                travel_id= extras.getString("travel_id");


            }
        } else {
            travel_id= (String) savedInstanceState.getSerializable("travel_id");
        }
        accept=(Button)findViewById(R.id.bt_accept);
        back=(Button)findViewById(R.id.bt_back);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageRequest=et_message.getText().toString();
                if(messageRequest.length()>10){
                   sendRequestToserver();
                }
                else{
                    Toast.makeText(getBaseContext(), "You cannot leave the message request blank",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FetchDataFromTheServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }
public void FetchDataFromTheServer(){
    String sessionId=GetSessionCookie.getCookie(getApplicationContext());
    client = new AsyncHttpClient();
    params = new RequestParams();
    client.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
    params.put("data",travel_id);

    client.post(URL, params, new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            // called before request is started
            Log.e("HTTP", "STARTED");

        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, String obj) {
            Log.e("SearchforResult", obj.toString());
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(String.valueOf(obj));
                travel_details = jsonObj.getJSONArray("travel_details");
                Log.e("length", String.valueOf(travel_details.length()));
                HashMap<String, String> tDetail;
                for (int i = 0; i < travel_details.length(); i++) {
                    JSONObject c = travel_details.getJSONObject(i);
                    String travel_id = c.getString("travel_id");
                    Log.e("travel_id", travel_id);
                    String name = c.getString("name");
                    String work = c.getString("work");
                    String source = c.getString("source");
                    String profilePicURL = c.getString("profile_pic_url");
                    String destination = c.getString("destination");
                    String type = c.getString("type_transport");
                    String gender = c.getString("gender");
                    String doj = c.getString("doj");
                    DateFormat format12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date src = format12.parse(doj);
                    DateFormat ft = new SimpleDateFormat("E dd MMM yyyy");
                    doj = ft.format(src);
                    Log.e("doj", doj);
                    tv_name.setText(name);
                    tv_work.setText(work);
                    travel_city.setText(source + " to " + destination);
                    if (type.equalsIgnoreCase("B")) {
                        iv_travel_type.setImageResource(R.drawable.bus);
                    } else if (type.equalsIgnoreCase("T")) {
                        iv_travel_type.setImageResource(R.drawable.train);
                    } else
                        iv_travel_type.setImageResource(R.drawable.airplane);

                    Picasso.with(getApplicationContext()).load(profilePicURL).into(iv_profilepic);


                }
                Log.e("m here", "123");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("exception", e.getMessage().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            Log.e("http failure", e.toString());
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

    public void sendRequestToserver(){
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        SendClient=new AsyncHttpClient();
        SendClient.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        params.put("rqst", search_id);
        params.put("message", messageRequest);
        params.put("message", messageRequest);
        client.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String obj) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("http failure", e.toString());
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
}

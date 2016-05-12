package peerdelivers.peerdelivery;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {
    String sms="";
    Filtering ft;
    Button button;
    TextView view;
    public static final String user_id = "user_id" ;
    SharedPreferences sharedpreferences;
    static String message = PreStart.phoneNo;
    static String secretCode=PreStart.uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ft=new Filtering();



        button = (Button) findViewById(R.id.b_next);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent goToNextActivity = new Intent(getApplicationContext(), SearchForPeer.class);
                startActivity(goToNextActivity);

            }

        });
        Bundle bundle = getIntent().getExtras();
       // static String message = PreStart;//bundle.getString("user_id");
        sharedpreferences = getSharedPreferences(user_id, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(message!=null) {
            editor.putLong("user_id", Long.parseLong(message.substring(3)));
            editor.putLong("auth_code", Long.parseLong(secretCode));
            editor.commit();
            sms=message;
            Log.e("devanshu main", message);
            PreStart.instance().finish();
            preMain.instance().finish();
        }
        else{
            sms=String.valueOf(sharedpreferences.getLong("user_id",0));
        }


        view=(TextView)findViewById(R.id.devanshu);
        view.setMovementMethod(new ScrollingMovementMethod());
        view.setText("this is sparta");
        getSMS();

    }
    public void getSMS(){
        String progess=null;
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, new String[]{"_id", "address", "date", "body"}, null, null, null);
        int total=cur.getCount();
        int i=0;


        String temp=null;
        String type=null;
        int m=0;
        String body;
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray();
        while (cur.moveToNext()) {

            temp = cur.getString(1);
            body = cur.getString(3);
            Log.e(temp, "value");
            if (body.contains("PNR")) {
                type = ft.filter(temp);
                Log.e(type, "type of transport");
                if (type.equalsIgnoreCase("train") || type.equalsIgnoreCase("FLIGHT")) {
                    try {
                        jso.put("TYPE", type);
                        jso.put("BODY", cur.getString(3));
                        jso.put("DATE", cur.getString(2));
                        jsa.put(m++,jso);
                        //sms += "Type:" + type + " Body:" + cur.getString(3) + " Date:" + cur.getString(2) + "\n";
                        //sms += "--------------------------------------------------------------------------\n";

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        view.setText("Welcome back " + sms + "\n" + jsa.toString());
        Log.e(jsa.toString(), "JSON");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {


                    startActivity(new Intent(this, about.class));


        }

        return super.onOptionsItemSelected(item);
    }

}
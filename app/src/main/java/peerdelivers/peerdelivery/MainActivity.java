package peerdelivers.peerdelivery;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {
    String sms="";
    Filtering ft;
    Button button,notify;
    TextView view;
    NotifyManager nm;
    public static final String user_id = "user_id" ;
    SharedPreferences sharedpreferences,smsSharedPreferences;
    static String message = PreStart.phoneNo;
    static String secretCode=PreStart.uuid;
    HashMap<String,String> notifydata=new HashMap<String,String>();
    Context ct;
    long msG_ID;
   ListView mDrawerList;
    ListView tListView;
    private CustomAdapter travelList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    List<HashMap<String,String>> hm;

    String[] testArry={"devanshu","siddharth","aditya","himanshu","tatti","pheshab","cannada","India","Chutiya","Randi ka baccha"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       view=(TextView)findViewById(R.id.devanshu);
//        view.setMovementMethod(new ScrollingMovementMethod());
        tListView= (ListView)findViewById(R.id.travelList);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();

        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
                R.layout.listview_layout,R.id.listTextView, testArry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ft=new Filtering();
        ct=getApplication();


        button = (Button) findViewById(R.id.b_next);
        notify=(Button)findViewById(R.id.notify);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent goToNextActivity = new Intent(getApplicationContext(), SearchForPeer.class);
                startActivity(goToNextActivity);

            }

        });

        notify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                notifydata.put("title","This is a sample title");
                notifydata.put("content","this is a sample content");
            nm=new NotifyManager(ct);
                nm.notifying(notifydata);


            }

        });

        Bundle bundle = getIntent().getExtras();
       // static String message = PreStart;//bundle.getString("user_id");
        //optimizing sms reading...not reading a sms which is already read.
        smsSharedPreferences= getSharedPreferences("last_read_msg_id", Context.MODE_PRIVATE);
        msG_ID=smsSharedPreferences.getLong("last_msg_id", 0);
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



        getSMS();

    }
    public void getSMS(){
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, new String[]{"_id", "address", "date", "body"}, null, null, null);
       // long total=cur.getCount();
        int i=0;


        String temp=null;
        String type=null;
        int m=0;
        String body;
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray();
        Long lastsmsId= Long.valueOf(0);
        int j=0;
        Log.e("last read id:",String.valueOf(msG_ID));
        hm= new LinkedList<HashMap<String, String>>();
        while (cur.moveToNext()) {
            if((i++)==0) {
                //to be uncommented during production
               // lastsmsId=Long.parseLong(cur.getString(0));
            }
            temp = cur.getString(1);
            body = cur.getString(3);
            Log.e(temp, "value");
            Log.e("_id",cur.getString(0));
            if (Long.parseLong(cur.getString(0)) > msG_ID){
                if (body.contains("PNR")) {
                    type = ft.filter(temp);
                    Log.e(type, "type of transport");
                    if (type.equalsIgnoreCase("train") || type.equalsIgnoreCase("FLIGHT")) {
                        try {
                            HashMap<String,String> tDetail=new HashMap<String,String>();
                            jso.put("TYPE", type);
                            jso.put("BODY", cur.getString(3));
                            jso.put("DATE", cur.getString(2));

                            jsa.put(m++, jso);
                            tDetail.put("type",type);
                            tDetail.put("content", cur.getString(3));
                            if(cur.getString(3).contains("DEVANSHU"))
                            tDetail.put("status","N");
                            else
                                tDetail.put("status","A");
                            //sms += "Type:" + type + " Body:" + cur.getString(3) + " Date:" + cur.getString(2) + "\n";
                            //sms += "--------------------------------------------------------------------------\n";
                            hm.add(tDetail);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                }

        }
            //lastsmsId=Long.parseLong(cur.getString(0));
        }
        SharedPreferences.Editor editor = smsSharedPreferences.edit();
        editor.putLong("last_msg_id", lastsmsId);
        editor.commit();

       view.setText("Welcome back " + sms);
        Log.e(jsa.toString(), "JSON");
        travelList=new CustomAdapter(this,hm);
        travelList.setListView(tListView);
        tListView.setAdapter(travelList);

    }

    private void addDrawerItems() {
        String[] osArray = { "Home", "Notification", "History", "Sent Items", "Received Items","Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

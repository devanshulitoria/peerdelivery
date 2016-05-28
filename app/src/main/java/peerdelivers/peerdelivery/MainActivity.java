package peerdelivers.peerdelivery;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {
    String sms="";
    String session_id;
    AsyncHttpClient myClient;
    TextView view;
    public static final String user_id = "user_id" ;
    SharedPreferences sharedpreferences,smsSharedPreferences;
    String phNumber =null;
    String auth_code=null;
    String fbToken=null;
    String fbID=null;
    static Context ct;
    SharedPreferences cookies;
    long msG_ID;
    final String URL="http://192.168.137.1/MainActivity.php";
    RequestParams params;
   ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Typeface custom_font;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TelephonyManager telephonyManager;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cookies = this.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        session_id=cookies.getString("phpID","0");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                phNumber= null;
                auth_code=null;
                fbToken=null;


            } else {
                phNumber= extras.getString("phNumber");
                auth_code= extras.getString("auth_code");
                fbToken= extras.getString("fbToken");

            }
        } else {
            phNumber= (String) savedInstanceState.getSerializable("phNumber");
            auth_code= (String) savedInstanceState.getSerializable("auth_code");
            fbToken= (String) savedInstanceState.getSerializable("fbToken");
        }

       // scheduleAlarm();
        ct=getApplication();

        CheckConnection.isConnected(getApplicationContext(), MainActivity.this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

            view = (TextView) findViewById(R.id.devanshu);



            mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();
            custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
            view.setTypeface(custom_font);


            addDrawerItems();
            setupDrawer();


            // static String message = PreStart;//bundle.getString("user_id");
            //optimizing sms reading...not reading a sms which is already read.
            smsSharedPreferences = getSharedPreferences("last_read_msg_id", Context.MODE_PRIVATE);
            msG_ID = smsSharedPreferences.getLong("last_msg_id", 0);
            sharedpreferences = getSharedPreferences(user_id, Context.MODE_PRIVATE);
                 editor = sharedpreferences.edit();
            if (phNumber != null && sharedpreferences.getLong("phNumber", 0)==0) {
                registerUser(phNumber, auth_code, fbToken);


            } else {
                sms = String.valueOf(sharedpreferences.getLong("user_id", 0));
                Log.e("Mainactivity user_id",sms);
            }




    }
    public void localRegistry(String fb_id){
        Toast.makeText(getApplicationContext(),
                "MainActivity local registry"+fb_id+":"+phNumber, Toast.LENGTH_LONG)
                .show();
        fbID=fb_id;
        editor.putLong("phNumber", Long.parseLong(phNumber.substring(3)));
        editor.putString("auth_code", auth_code);
        editor.putString("facebook_id",fbID);
        editor.commit();
        sms = phNumber;
        Log.e("devanshu main", phNumber);
        //// TODO: 5/28/2016
        //PreStart.inst.finish();
    }

        public void registerUser(String phNumber, String auth_code, String fbToken){
            telephonyManager=( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
            String deviceIMIE = telephonyManager.getDeviceId();
            String subscriberID=telephonyManager.getSubscriberId();
            params = new RequestParams();
            params.put("phNumber",phNumber.substring(3));
            params.put("auth_code",auth_code);
            params.put("fbtoken",fbToken);
            params.put("deviceID", deviceIMIE);
            params.put("userID", subscriberID);
            Log.e("phNumber", phNumber.substring(3));
            Log.e("auth_code", auth_code);
            Log.e("fbtoken", fbToken);
            Log.e("deviceID", deviceIMIE);
            Log.e("userID", subscriberID);


            myClient = new AsyncHttpClient();

            if(!session_id.equalsIgnoreCase("0")) {
                Toast.makeText(getApplicationContext(),
                        "MainActivity SessionId is present:"+session_id, Toast.LENGTH_LONG)
                        .show();
                myClient.addHeader("Cookie", "PHPSESSID=" + session_id + "");
            }
            myClient.post(URL, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("HTTP", "STARTED");

                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    // called when response HTTP status is "200 OK"
                    Log.e("http response", obj.toString());
                    Toast.makeText(getApplicationContext(),
                            "MainActivity " +obj.toString(), Toast.LENGTH_LONG)
                            .show();
                    try {
                        if (obj.getString("error").toString().equalsIgnoreCase("true")){
                        Toast.makeText(getApplicationContext(),
                                "MainActivity " + ErrorHandling.displayError(obj.getString("errorcode")), Toast.LENGTH_LONG)
                                .show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "inside else "+obj.getString("fbID").toString(), Toast.LENGTH_LONG)
                                    .show();
                            localRegistry(obj.getString("fbID").toString());
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),
                                "EXCEPTION"+e.toString(), Toast.LENGTH_LONG)
                                .show();
                    }

//                    try {
//                        s = obj.getString("sessionId").toString();
//                        if(!s.equalsIgnoreCase("0")) {
//
//                        }
//                        else{
//
//                        }
//
//                    }catch (JSONException e) {e.printStackTrace();}
//                    Log.e("http response", s);
//                    view.setVisibility(View.GONE);

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

                @Override
                public void onFinish() {
                    super.onFinish();
                    Log.i("onFinish", "OKOKOK");
                }

            });
        }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentYourActivity(), "Your Activity");
        adapter.addFragment(new FragmentYourFriendsActivity(), "Friends Activity");
        viewPager.setAdapter(adapter);
    }


    private void addDrawerItems() {
        String[] osArray = { "Home", "Notification", "History", "Sent your Items", "Received Items","Settings" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position drawer click", String.valueOf(position));


                switch(position) {
                    case 3:
                        Intent goToNextActivity = new Intent(MainActivity.this, SearchForPeer.class);
                        startActivity(goToNextActivity);
                        break;
                    case 4:
                        Intent a = new Intent(MainActivity.this, CarrierActivity.class);
                        startActivity(a);
                        break;
                    case 5:
                        Intent b = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(b);
                        break;
                    default:
                }
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

    public  Context getContext(){
        return getApplicationContext();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.notificationlayout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        final TextView tv = (TextView) notifCount.findViewById(R.id.tvcounter);
        Button bt=(Button)notifCount.findViewById(R.id.bt_noti);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //item.setIcon(R.drawable.notification);
                startActivity(new Intent(getApplicationContext(), ServerNotifications.class));
                tv.setText("");
                tv.setVisibility(View.INVISIBLE);


            }

        });
        tv.setText("12");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.e("Menu ID:", String.valueOf(id));
        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {


                    startActivity(new Intent(this, about.class));


        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if(id==R.id.badge)
        {


        }
        return super.onOptionsItemSelected(item);
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Log.e("Schedule alaram called","devanshu");
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        Log.e("Schedule alaram called1","devanshu");
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("Schedule alaram called2","devanshu");
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        Log.e("Schedule alaram called3",String.valueOf(firstMillis));
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Log.e("Schedule alaram called4","devanshu");
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_DAY, pIntent);
        Log.e("Schedule alaram called5", "devanshu");
    }
    ///async task to collect all the text messages


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}

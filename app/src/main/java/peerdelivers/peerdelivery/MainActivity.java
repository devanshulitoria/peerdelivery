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
    Filtering ft;
    SpannableString s;
    TextView view,not_found;
    public static final String user_id = "user_id" ;
    SharedPreferences sharedpreferences,smsSharedPreferences;
    static String message = PreStart.phoneNo;
    static String secretCode=PreStart.uuid;
    HashMap<String,String> notifydata=new HashMap<String,String>();
    static Context ct;
    long msG_ID;
   ListView mDrawerList;
    ListView tListView;
    private CustomAdapter travelList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    List<HashMap<String,String>> hm;
    Typeface custom_font;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final BroadcastReceiver mybroadcast = new IncomingSms();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
            SharedPreferences.Editor editor = sharedpreferences.edit();
            if (message != null) {
                editor.putLong("user_id", Long.parseLong(message.substring(3)));
                editor.putLong("auth_code", Long.parseLong(secretCode));
                editor.commit();
                sms = message;
                Log.e("devanshu main", message);
                PreStart.instance().finish();
                preMain.instance().finish();
            } else {
                sms = String.valueOf(sharedpreferences.getLong("user_id", 0));
            }




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

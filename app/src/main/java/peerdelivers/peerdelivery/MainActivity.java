package peerdelivers.peerdelivery;


import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import com.crashlytics.android.Crashlytics;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.answers.Answers;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    String sms="";
    String session_id;
    TextView view;
    SharedPreferences sharedpreferences;
    String phNumber =null;
    String auth_code=null;
    String fbToken=null;
    String fbID=null;
    static Context ct;
    SharedPreferences cookies;
    String URL;
    String current_user_name,current_user_gender;
    TextView tv,ftv;
    RequestParams params;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Typeface custom_font;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TelephonyManager telephonyManager;
    Intent mServiceIntent;
    private NavigationView navigationView;
    private NotifyService mSensorService;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 70;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main2);
        FacebookSdk.sdkInitialize(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Answers.getInstance().logCustom(new CustomEvent("MainActivity")
                .putCustomAttribute("Facebookid", "abcd")
                .putCustomAttribute("Length", 120));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.headerdrawer);
        TextView tv_name=(TextView)headerLayout.findViewById(R.id.username);
        TextView tv_phnumber=(TextView)headerLayout.findViewById(R.id.tv_phnumber_tv);
        CircleImageView imageView_profile = (CircleImageView) headerLayout.findViewById(R.id.profile_image);
        imageView_profile.setBorderColor(Color.parseColor("#FFFFFF"));
        imageView_profile.setBorderWidth(2);
        String localPicUrl="https://graph.facebook.com/me/picture?type=large&access_token="+GetSessionCookie.fbToken(MainActivity.this);
        if(GetSessionCookie.gender(MainActivity.this).equalsIgnoreCase("m")){
            Picasso.with(MainActivity.this).load(localPicUrl).placeholder(getResources().getDrawable(R.drawable.showman)).error(getResources().getDrawable(R.drawable.showman)).into(imageView_profile);

        }
        else {
            Picasso.with(MainActivity.this).load(localPicUrl).placeholder(getResources().getDrawable(R.drawable.woman)).error(getResources().getDrawable(R.drawable.woman)).into(imageView_profile);
        }
        tv_name.setText(GetSessionCookie.user_name(MainActivity.this));
        tv_phnumber.setText(GetSessionCookie.phoneNumber(MainActivity.this));
        URL=getResources().getString(R.string.URL)+"/MainActivity.php";
        sharedpreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
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
        //<TODO>
        CheckConnection.isConnected(getApplicationContext(), MainActivity.this);
        ct=getApplication();

        //<// TODO: 6/20/2016
       // scheduleAlarm();
        mSensorService = new NotifyService();
        mServiceIntent = new Intent(getApplicationContext(),NotifyService.class);
        if (!isMyServiceRunning(mSensorService.getClass())) {
            Log.e("MainActivity","Insidemyservicerunning");

            scheduleAlarm();
        }





        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

           // view = (TextView) findViewById(R.id.devanshu);



           // mDrawerList = (ListView) findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();
            custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
           // view.setTypeface(custom_font);


            // static String message = PreStart;//bundle.getString("user_id");
            //optimizing sms reading...not reading a sms which is already read.

            if (phNumber != null && sharedpreferences.getLong("phNumber", 0)==0) {

                checkPermission();

            } else {
                sms = String.valueOf(sharedpreferences.getLong("user_id", 0));
                Log.e("Mainactivity user_id",sms);
            }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.notifications:
                        Intent goTonoti = new Intent(MainActivity.this, ServerNotifications.class);
                        goTonoti.putExtra("type", "H");
                        startActivity(goTonoti);
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.myrequeststatus:
                        Intent goToNexthistory = new Intent(MainActivity.this, CarrierActivity.class);
                        goToNexthistory.putExtra("type", "H");
                        startActivity(goToNexthistory);
                        return true;
                    case R.id.requestrecieved:
                        Intent a = new Intent(MainActivity.this, CarrierActivity.class);
                        a.putExtra("type", "R");
                        startActivity(a);
                        return true;
                    case R.id.sendyouritems:
                        Intent goToNexthistory1 = new Intent(MainActivity.this, SearchForPeer.class);
                        goToNexthistory1.putExtra("type", "R");
                        startActivity(goToNexthistory1);
                        return true;
                    case R.id.settings:
                        Intent settings = new Intent(MainActivity.this, activity_setting.class);
                        settings.putExtra("type", "R");
                        startActivity(settings);
                        return true;
                    case R.id.logout:

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Logout");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure you want to logout!")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        TravelDataSource tds = new TravelDataSource(getContext());
                                        tds.DropTable(MainActivity.this);
                                        SharedPreferences preferences = getSharedPreferences("user_id", 0);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.clear();
                                        editor.commit();
                                        LoginManager.getInstance().logOut();
                                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                        finish();
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        return true;
                    case R.id.about:
                        Intent aBT = new Intent(MainActivity.this, about.class);
                        aBT.putExtra("type", "R");
                        startActivity(aBT);
                        return true;
                    case R.id.improve:
                        Intent improve = new Intent(MainActivity.this, Improve.class);
                        improve.putExtra("type", "R");
                        startActivity(improve);
                        return true;
                    case R.id.faq:
                        Intent faq = new Intent(MainActivity.this, activity_faq.class);
                        faq.putExtra("type", "R");
                        startActivity(faq);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }

            }
        });

        setupDrawer();




    }

    public void localRegistry(String fb_id){

        fbID=fb_id;
        SharedPreferences settings =getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_settings = settings.edit();
        editor_settings.putInt("email", 1);
        editor_settings.putInt("train", 5);
        editor_settings.putInt("plane", 2);
        editor_settings.putInt("bus", 5);
        editor_settings.putString("status","Hey there I am using PeerDelivery");


        editor_settings.commit();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong("phNumber", Long.parseLong(phNumber.substring(3)));
        editor.putString("auth_code", auth_code);
        editor.putString("facebook_id",fbID);
        editor.putString("fbToken",fbToken);
        editor.putString("current_user_name",current_user_name);
        editor.putString("gender",current_user_gender);

        editor.putLong("last_msg_id", 0);
        editor.commit();
        sms = phNumber;
        Log.e("devanshu main", phNumber);
        Intent goToNextActivity = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(goToNextActivity);
        finish();

    }
public void getNotificationCount(){
    AsyncHttpClient myClient1 = new AsyncHttpClient();
    RequestParams params2 = new RequestParams();
    params2.put("notification_count", "noti_count");
    String URL2=getResources().getString(R.string.URL)+"/ServerNotifications.php";
    String sessionId=GetSessionCookie.getCookie(getContext());
    if(!sessionId.equalsIgnoreCase("0")) {
        Log.e("FragmentFriendsActivity",sessionId);
        myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        //myClient1.setConnectTimeout(30000);
        myClient1.post(URL2, params2, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("main ACTIVITY", "fetching notification count");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),
                        "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                Log.e("main ACTIVITY", "fetching notification count data:"+str);
                String[] temp=str.split("~");
                if(!temp[0].equalsIgnoreCase("0")) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(temp[0]);
                }
                if(!temp[1].equalsIgnoreCase("0")){
                    ftv.setVisibility(View.VISIBLE);
                    ftv.setText(temp[1]);
                }

            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("main activity", String.valueOf(retryNo));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i("main activity", "OKOKOK");
            }

        });
    }
    else{

    }
}
    public  void accessPermissions() {
        Log.e("MainAcitivity", "Inside access permissions");
        int hasWriteContactsPermission=-1;
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("MainActivity", String.valueOf(hasWriteContactsPermission));
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_PHONE_STATE)) {
                showMessageOKCancel("In order to enrich the experience we need to access to your phone so that you don't have to leave PeerDelivery just to make a phone call",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                Log.e("MainAcitivty", "Return1");

                return;
            }  ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            Log.e("MainAcitivty", "Return2");
            return;
        }
        registerUser(phNumber, auth_code, fbToken);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void checkPermission(){
        int permissioncheck=-1;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            permissioncheck= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
            if(permissioncheck== PackageManager.PERMISSION_GRANTED){
                registerUser(phNumber, auth_code, fbToken);
            }
            else{
                accessPermissions();
            }
        } else {
            // Pre-Marshmallow
            registerUser(phNumber, auth_code, fbToken);

        }

        Log.e("permission", String.valueOf(permissioncheck));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results

                if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    registerUser(phNumber, auth_code, fbToken);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Denied Permissions");
                    alertDialogBuilder
                            .setMessage("You have denied some important permission")
                            .setCancelable(false)
                            .setPositiveButton("Exit now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }

                            });


                    // show it
                    alertDialogBuilder.show();
                }
            }
            break;

        }
    }

        public  String getFacebookId(){
            String facebook_id="";
            SharedPreferences sharedpreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
            facebook_id=sharedpreferences.getString("facebook_id", "");
            return facebook_id;
        }

        public void registerUser(String phNumber, String auth_code, String fbToken){
            Log.e("MainAcitivity","Inside registerUser");
            SyncHttpClient myClient3 = new SyncHttpClient();
            telephonyManager=( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );
            String deviceIMIE = telephonyManager.getDeviceId();
            String subscriberID=telephonyManager.getSubscriberId();
            params = new RequestParams();
            params.put("phNumber",phNumber.substring(3));
            params.put("auth_code", auth_code);
            params.put("fbtoken",fbToken);
            params.put("deviceID", deviceIMIE);
            params.put("userID", subscriberID);
            Log.e("phNumber", phNumber.substring(3));
            Log.e("auth_code", auth_code);
            Log.e("fbtoken", fbToken);
            Log.e("deviceID", deviceIMIE);
            Log.e("userID", subscriberID);




            if(!session_id.equalsIgnoreCase("0")) {

                myClient3.addHeader("Cookie", "PHPSESSID=" + session_id + "");
            }
            myClient3.post(URL, params, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("MAINACTIVITY", "HTTP STARTED");

                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, String str) {
                    // called when response HTTP status is "200 OK"
                    JSONObject obj;
                    Log.e("Mainactivity:","ON SUCCESS"+ str);

                    try {
                        obj= new JSONObject(String.valueOf(str));
                        if (obj.getString("error").toString().equalsIgnoreCase("true")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertDialogBuilder.setTitle("Huh!");
                            alertDialogBuilder
                                    .setMessage(ErrorHandling.displayError(obj.getString("errorcode").toString()))
                                    .setCancelable(false)
                                    .setPositiveButton("Exit now", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            LoginManager.getInstance().logOut();
                                            finish();
                                            android.os.Process.killProcess(android.os.Process.myPid());

                                        }

                                    });
                            alertDialogBuilder.show();
                        }
                        else {
                            if(obj.getString("fbID").toString().equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Something terrible went wrong! We are very sorry", Toast.LENGTH_LONG)
                                        .show();
                            }
                            else{
                                current_user_gender=obj.getString("gender").toString();
                                current_user_name=obj.getString("name").toString();
                                localRegistry(obj.getString("fbID").toString());
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),
                                "EXCEPTION"+e.toString(), Toast.LENGTH_LONG)
                                .show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("Mainactivity", String.valueOf(statusCode));
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

        adapter.addFragment(new FragmentYourFriendsActivity(), "Friends Activity");
        adapter.addFragment(new FragmentYourActivity(), "Your Activity");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
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
        mDrawerToggle.syncState();
    }
/*
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
*/
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

        tv = (TextView) notifCount.findViewById(R.id.tvcounter);
        ftv = (TextView) notifCount.findViewById(R.id.friendcounter);
        Button bt=(Button)notifCount.findViewById(R.id.bt_noti);
        Button invite=(Button)notifCount.findViewById(R.id.invite);
        tv.setVisibility(View.INVISIBLE);
        ftv.setVisibility(View.INVISIBLE);
        String sessionId=GetSessionCookie.getCookie(getContext());
        Log.e("MainAcitivity: ","sessionid:"+sessionId);
        if(sessionId!=null) {
            getNotificationCount();
        }
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //item.setIcon(R.drawable.notification);
                startActivity(new Intent(getApplicationContext(), ServerNotifications.class));
                tv.setText("");
                tv.setVisibility(View.INVISIBLE);


            }

        });
        invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                String appLinkUrl, previewImageUrl;
//
//                appLinkUrl = "https://fb.me/1139275469462206";
//                previewImageUrl = "http://www.peerdelivery.in/pic/icon.png";
//
//                if (AppInviteDialog.canShow()) {
//                    AppInviteContent content = new AppInviteContent.Builder()
//                            .setApplinkUrl(appLinkUrl)
//                            .setPreviewImageUrl(previewImageUrl)
//                            .build();
//                    AppInviteDialog appInviteDialog = new AppInviteDialog(MainActivity.this);
//                    appInviteDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<AppInviteDialog.Result>()
//                    {
//                        @Override
//                        public void onSuccess(AppInviteDialog.Result result)
//                        {
//                            Log.e("invitation", result.toString());
//                        }
//
//                        @Override
//                        public void onCancel()
//                        {
//                        }
//
//                        @Override
//                        public void onError(FacebookException e)
//                        {
//                            Log.e("invitation", e.getMessage().toString());
//                        }
//                    });
//                    AppInviteDialog.show(MainActivity.this, content);
//                }
                Intent goTonoti = new Intent(MainActivity.this, Friends.class);
                startActivity(goTonoti);

            }

        });
        tv.setText("");
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
         PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), MyAlarmReceiver.REQUEST_CODE,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("Schedule alaram called2","devanshu");
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        Log.e("Schedule alaram called3",String.valueOf(firstMillis));
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Log.e("Schedule alaram called4","devanshu");
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        if(Build.VERSION.SDK_INT < 23){
            Log.e("old api","Mainactivity");
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis+30000,
                    20000*10, pIntent);
        }
        else{
            Log.e("new api", "Mainactivity");
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis+30000,
                   120*1000, pIntent);
        }

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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

}

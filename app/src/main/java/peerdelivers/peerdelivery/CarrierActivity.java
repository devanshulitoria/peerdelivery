package peerdelivers.peerdelivery;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class CarrierActivity extends AppCompatActivity implements FragmentAccepted.OnHeadlineSelectedListener,FragmentPending.OnHeadlineSelectedListenerP,FragmentRejected.OnHeadlineSelectedListenerR{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AsyncHttpClient myClient1;
    RequestParams params;
    static String command;
    FragmentAccepted articleFragA;
    FragmentRejected articleFragR;
    FragmentPending articleFragP;
    int pos;
    private List<HashMap<String,String>> hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                command= null;
            } else {
                command= extras.getString("type");
            }
        } else {
            command= (String) savedInstanceState.getSerializable("type");

        }
        FragmentPending.counter=0;
        FragmentRejected.counter=0;
        FragmentAccepted.counter=0;

        setContentView(R.layout.activity_carrier);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String title="Peer Delivery";


        if(command.equalsIgnoreCase("H")){
            title="My Request Status";
        }
        else{
            title="Request Received";
        }
        Log.e("CarrierActivity", title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle(title);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPending(), "Pending");
        adapter.addFragment(new FragmentAccepted(), "Accepted");
        adapter.addFragment(new FragmentRejected(), "Rejected");

        viewPager.setAdapter(adapter);
    }


        public void fetchDataFromtheServer(int position){
            pos=position;
            final String queryString;
            Log.e("CarrierActivity","command value"+command);
            if(position==0){
                articleFragA = (FragmentAccepted)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_accepted);

                if(command.equalsIgnoreCase("H")){
                    queryString="C0";
                }
                else{
                    queryString="H0";
                }

            }
            else if(position==1){
                articleFragP = (FragmentPending)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_pending);


                if(command.equalsIgnoreCase("H")){
                    queryString="C1";
                }
                else{
                    queryString="H1";
                }
            }
            else{
                articleFragR = (FragmentRejected)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_rejected);
                if(command.equalsIgnoreCase("H")){
                    queryString="C2";
                }
                else{
                    queryString="H2";
                }
            }

            myClient1 = new AsyncHttpClient();
            final String URL=getResources().getString(R.string.URL)+"/CarrierActivity.php";
            String sessionId=GetSessionCookie.getCookie(getApplicationContext());
            if(!sessionId.equalsIgnoreCase("0")) {
                Log.e("CarrierActivity",sessionId);
                myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
            }
            params = new RequestParams();
            params.put("data", queryString);
            Log.e("CarrierActivity:","data send to server is "+queryString);

            myClient1.post(URL, params, new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.e("CarrierActivity", "HTTP STARTED");

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),
                            "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String str) {
                    // called when response HTTP status is "200 OK"
                    Log.e("CarrierActivity", "httpresponse:" + str);
                    hm = new LinkedList<HashMap<String, String>>();
                    JSONArray peoples = null;
                    JSONObject jsonObj;
                    String serverCommand;
                    String queryString1 = "";
                    try {
                        jsonObj = new JSONObject(String.valueOf(str));
                        peoples = jsonObj.getJSONArray("accepted");
                        if (peoples.isNull(1)) {
                            serverCommand = peoples.getString(0);
                            if(serverCommand.equalsIgnoreCase("H0") || serverCommand.equalsIgnoreCase("C0")){

                                articleFragA.nothinghere();
                                Log.e("CarrierActivity", "calling nothinghere accepted");
                            }
                            else if(serverCommand.equalsIgnoreCase("H1") || serverCommand.equalsIgnoreCase("C1")){
                                articleFragP.nothinghere();
                                Log.e("CarrierActivity", "calling nothinghere pending");
                            }
                            else if(serverCommand.equalsIgnoreCase("H2") || serverCommand.equalsIgnoreCase("C2")){
                                articleFragR.nothinghere();
                                Log.e("CarrierActivity", "calling nothinghere rejected");
                            }
                            else{
                                Log.e("CarrierActivity123","Everything is fucked");
                            }

                        }
                        else {
                            Log.e("length", String.valueOf(peoples.length()));
                            HashMap<String, String> tDetail;
                            for (int i = 1; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                String content = "";
                                String request_id = c.getString("request_id");
                                String profilePicURL = c.getString("profile_pic_url");
                                String noti_id = "0";
                                queryString1 = c.getString("querystring");
                                if (c.has("noti_id")) {
                                    noti_id = c.getString("noti_id");
                                }
                                String name = c.getString("name");
                                String request_time = c.getString("request_time");
                                DateFormat format12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date src = format12.parse(request_time);
                                DateFormat ft = new SimpleDateFormat("E dd MMM yyyy");
                                request_time = ft.format(src);
                                String timestr="";
                                if (queryString1.equalsIgnoreCase("H1")) {
                                    content = name + "'s request is still awaiting for your approval";
                                    timestr= "Requested you on " + request_time;
                                } else if (queryString1.equalsIgnoreCase("H0")) {
                                    content = "You have accepted " + name + "'s request to carry his belongings!";
                                    timestr= "Accepted on " + request_time;
                                } else if (queryString1.equalsIgnoreCase("H2")) {
                                    content = "You have rejected " + name + "'s request to carry his belongings!";
                                    timestr= "Rejected on " + request_time;
                                } else if (queryString1.equalsIgnoreCase("C0")) {
                                    content = name + " has agreed to carry your item";
                                    timestr= "Agreed on " + request_time;
                                } else if (queryString1.equalsIgnoreCase("C1")) {
                                    content = name + " hasn't yet responded to your request";
                                    timestr= "Not responed since " + request_time;
                                } else if (queryString1.equalsIgnoreCase("C2")) {
                                    content = name + " has refused to carry your item";
                                    timestr= "Refused on " + request_time;
                                } else {
                                    content = "Error 404";
                                }
                                Log.e("request_time", request_time);
                                tDetail = new HashMap<String, String>();
                                tDetail.put("content", content);
                                tDetail.put("time", timestr);
                                tDetail.put("noti_status", "R");
                                tDetail.put("noti_id", noti_id);
                                tDetail.put("request_id", request_id);
                                tDetail.put("profilePicURL", profilePicURL);
                                tDetail.put("command", command);
                                Log.e("noti_id", noti_id);

                                hm.add(tDetail);
                            }
                        }
                        callFragment(hm, pos, queryString1, command);

                    } catch (JSONException e) {
                        String temp=e.getMessage().toString();
                        Log.e("exception", temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
    public void callFragment(List<HashMap<String,String>> list,int position,String str,String command){
        Log.e("CarrierActivity","call fragment called with position:"+str+"--"+list.size());
        if(str.equalsIgnoreCase("H0") || str.equalsIgnoreCase("C0")){

            articleFragA.populateListView(list);
            Log.e("CarrierActivity", "calling accepted");
        }
        else if(str.equalsIgnoreCase("H1") || str.equalsIgnoreCase("C1")){
            articleFragP.populateListView(list);
            Log.e("CarrierActivity", "calling pending");
        }
        else if(str.equalsIgnoreCase("H2") || str.equalsIgnoreCase("C2")){
            articleFragR.populateListView(list);
            Log.e("CarrierActivity", "calling rejected");
        }
        else{
            Log.e("CarrierActivity","Everything is fucked");
        }


    }

    }
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
            Log.e("ViewPagerAdapter", "addFragment called"+title);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

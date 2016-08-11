package peerdelivers.peerdelivery;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewTrip extends AppCompatActivity {
    AutoCompleteTextView source;
    AutoCompleteTextView destination;
    private RadioGroup itemRadioGroup;
    private RadioButton itemRadioButton;
    DatePicker dt;
    Button add_travel,bt_change;
    String et_source,et_destination;
    String URL;
    ArrayAdapter adapter;
    String finalDate;
    String travel_type;
    RequestParams params;
    List<HashMap<String,String>> hm;
    AsyncHttpClient myClient1;
    HashMap<String,Integer> hcities=new HashMap<String,Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        hm= new LinkedList<HashMap<String, String>>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        URL=getResources().getString(R.string.URL)+"/NewTrip.php";
        itemRadioGroup = (RadioGroup) findViewById(R.id.radioGroupitems);
        dt=(DatePicker)findViewById(R.id.datePicker);
        add_travel=(Button) findViewById(R.id.bt_add_travel);
        bt_change=(Button) findViewById(R.id.bt_change);
        source=(AutoCompleteTextView)findViewById(R.id.autoCompleteSource);
        destination=(AutoCompleteTextView)findViewById(R.id.autocompleteDest);
        adapter = new ArrayAdapter(NewTrip.this,android.R.layout.simple_dropdown_item_1line,SearchForPeer.cities);
        source.setThreshold(1);//will start working from first character
        source.setAdapter(adapter);
        hcities=SearchForPeer.hashmapCities();
        Log.e("hcities", String.valueOf(hcities.size()));
        dt.setMinDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        destination.setThreshold(1);//will start working from first character
        destination.setAdapter(adapter);
        add_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = itemRadioGroup.getCheckedRadioButtonId();
                Log.e("Selected id is ", String.valueOf(selectedId));


                if (selectedId <= 0) {
                    Toast.makeText(getBaseContext(), "Please select the type of transport!",
                            Toast.LENGTH_LONG).show();
                } else {
                    itemRadioButton = (RadioButton) findViewById(selectedId);
                    travel_type = itemRadioButton.getTag().toString().trim().toUpperCase();
                    int month = dt.getMonth();
                    int year = dt.getYear();
                    int day = dt.getDayOfMonth();
                    et_source = source.getText().toString().trim().toUpperCase();
                    et_destination = destination.getText().toString().trim().toUpperCase();
                    finalDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " 00:00:00";
                    if (hcities.containsKey(et_source) && hcities.containsKey(et_destination) && travel_type != null) {
                        add_travel.setEnabled(false);
                        add_travel.setText("In Progress");
                        sendDataToServer();
                    } else {
                        Toast.makeText(getBaseContext(), "You have not entered input fields",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                if(source.getText().toString()!="" || destination.getText().toString()!="") {
                    temp = source.getText().toString();
                    source.setText(destination.getText().toString());
                    destination.setText(temp);
                }
            }
        });

    }
    private long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }
    private void sendDataToServer() {
        params = new RequestParams();
        params.put("source",et_source);
        params.put("destination",et_destination);
        params.put("doj",finalDate);
        params.put("type",travel_type);

        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        myClient1 = new AsyncHttpClient();

        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("FragmentYour", sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        Log.e("FragmentActivity","after looper");
        myClient1.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("FRAGMENT ACTIVITY", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("newtrip.java", "failed"+responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                // called when response HTTP status is "200 OK"
                Log.e("newtrip.java", str);
                HashMap<String,String> tDetail=new HashMap<String,String>();
                long longTimeAgo    = timeStringtoMilis(finalDate);
                String doj2=(String) DateUtils.getRelativeDateTimeString(NewTrip.this, longTimeAgo, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);

                tDetail.put("type",travel_type);
                tDetail.put("content", et_source+" to "+et_destination);
                tDetail.put("status", "A");
                tDetail.put("doj",doj2);
                tDetail.put("doj_time",finalDate);
                hm.add(tDetail);
                if(hm.size()>0) {
                    TravelDataSource tds=new TravelDataSource(NewTrip.this);
                    tds.insertTravel_details(hm);
                    add_travel.setText("Completed");
                    finish();
                }

//
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

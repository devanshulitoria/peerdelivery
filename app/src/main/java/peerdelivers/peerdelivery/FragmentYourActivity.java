package peerdelivers.peerdelivery;
//[0]-> from
//[1]->TO
//[2]->PNR
//[3]->DOJ MM/DD/YYYY only
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.os.Looper.getMainLooper;

/**
 * Created by iMac on 5/25/2016.
 */
// Display SMS
public class FragmentYourActivity extends ListFragment implements AdapterView.OnItemClickListener {
    SharedPreferences smsSharedPreferences;
    Filtering ft=new Filtering();
    private CustomAdapter travelList;
    final String URL="http://192.168.137.1/FragmentYourActivity.php";
    String session_id;
    private CustomAdapter nca;
    ListView tListView;
    Typeface custom_font;
    SharedPreferences cookies;
    AsyncHttpClient myClient1;
    List<HashMap<String,String>> hm;
    long msG_ID;
    RequestParams params;
    public FragmentYourActivity() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       getSMS(getContext());
//        hm= new LinkedList<HashMap<String, String>>();
//        HashMap<String,String> tDetail=new HashMap<String,String>();
//        tDetail.put("content", "xyz");
//        tDetail.put("time", "xxx min ago");
//        hm.add(tDetail);
//        nca=new CustomAdapter(getContext(),hm);
//        setListAdapter(nca);
//        getListView().setOnItemClickListener(this);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("fragment list: ", parent.getItemAtPosition(position).toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_activity, container, false);
    }
    public void getSMS(Context context){
        smsSharedPreferences = context.getSharedPreferences("last_read_msg_id", Context.MODE_PRIVATE);
        msG_ID = smsSharedPreferences.getLong("last_msg_id", 0);

        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = context.getContentResolver().query(uriSMSURI, new String[]{"_id", "address", "date", "body"}, null, null, null);
        // long total=cur.getCount();
        int i=0;

        String []tDetails=new String[6];
        String temp=null;
        String type=null;
        int m=0;
        String body;
        JSONObject jso;
        JSONArray jsa = new JSONArray();
        Long lastsmsId= Long.valueOf(0);
        int j=0;
        Log.e("last read id:", String.valueOf(msG_ID));
        hm= new LinkedList<HashMap<String, String>>();
        while (cur.moveToNext()) {
            if((i++)==0) {
                //to be uncommented during production
                //lastsmsId=Long.parseLong(cur.getString(0));
                lastsmsId=0L;
            }
            temp = cur.getString(1);
            body = cur.getString(3);
            Log.e(temp, "value");
            Log.e("_id",cur.getString(0));
            if (Long.parseLong(cur.getString(0)) > msG_ID){
                if (body.contains("PNR")) {
                    tDetails = ft.filter(temp,body);
                    if(tDetails!=null && tDetails[0]!=null && tDetails[1]!=null && tDetails[2]!=null && tDetails[3]!=null)
                    type=tDetails[4];
                    else
                    type="U";
                    Log.e(type, "type of transport");
                    if (type.equalsIgnoreCase("T") || type.equalsIgnoreCase("F") || type.equalsIgnoreCase("B")) {
                        try {
                            HashMap<String,String> tDetail=new HashMap<String,String>();
                            jso = new JSONObject();
                            jso.put("TYPE", tDetails[4].trim());
                            jso.put("0", tDetails[0].trim());
                            jso.put("1", tDetails[1].trim());
                            jso.put("2",tDetails[2].trim());
                            jso.put("3", tDetails[3].trim());
                            if(cur.getString(3).toLowerCase().contains("cancelled") || cur.getString(3).toLowerCase().contains("cancel"))
                            jso.put("STATUS","N");
                            else
                                jso.put("STATUS","A");
                            Log.e("tdetails",tDetails[0]+"->"+tDetails[1]);
                            jsa.put(m++, jso);
                            tDetail.put("type",type);
                            tDetail.put("content", cur.getString(3));
                            tDetail.put("status", "A");

                            //sms += "Type:" + type + " Body:" + cur.getString(3) + " Date:" + cur.getString(2) + "\n";
                            //sms += "--------------------------------------------------------------------------\n";
                            hm.add(tDetail);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }//unknown text
                    else{
                        if(!cur.getString(1).contains("9") && !cur.getString(1).contains("8") && !cur.getString(1).contains("7") && !cur.getString(1).contains("6") && !cur.getString(1).contains("5") && !cur.getString(1).contains("4")&& !cur.getString(1).contains("3")&& !cur.getString(1).contains("2")&& !cur.getString(1).contains("1"))
                        try {
                            jso = new JSONObject();
                            jso.put("TYPE", "U");
                            jso.put("0", cur.getString(1));
                            jso.put("1", cur.getString(3));
                            jso.put("2", cur.getString(3));
                            jso.put("3", cur.getString(2));

                            jsa.put(m++, jso);

                        }catch (JSONException e) {
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


        Log.e("JSON", jsa.toString());
        sendDataToServer(jsa.toString(), getContext());
        if(hm.size()>0) {
            travelList = new CustomAdapter(context, hm);
            travelList.setListView(getListView());
            getListView().setAdapter(travelList);

        }


    }

    private void sendDataToServer(String dataToBeSend,Context context) {
        params = new RequestParams();
        params.put("travelData",dataToBeSend);

        cookies = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        session_id=cookies.getString("phpID", "0");
        myClient1 = new AsyncHttpClient();

        if(!session_id.equalsIgnoreCase("0")) {
            Log.e("FragmentYour",session_id);
            myClient1.addHeader("Cookie", "PHPSESSID=" + session_id + "");
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
                Log.e("FRAGMENT ACTIVITY", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                // called when response HTTP status is "200 OK"
                Log.e("http response", str);



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

//    private class MyAsyncTask extends AsyncTask<List<HashMap<String,String>>,Integer,String> {
//
//        @Override
//        protected void onPreExecute() {
//            // Runs on the UI thread before doInBackground()
//        }
//
//        @Override
//        protected String doInBackground(List<HashMap<String, String>>... params) {
//            // Perform an operation on a background thread
//            Log.e("background","devanshu");
//            getSMS(getActivity());
//            return null;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            // Runs on the UI thread after doInBackground()
//
//        }
//
//
//    }

}

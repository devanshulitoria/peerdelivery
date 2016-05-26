package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by iMac on 5/25/2016.
 */
// Display SMS
public class FragmentYourActivity extends ListFragment implements AdapterView.OnItemClickListener {
    SharedPreferences smsSharedPreferences;
    Filtering ft=new Filtering();
    private CustomAdapter travelList;
    private CustomAdapter nca;
    ListView tListView;
    Typeface custom_font;
    List<HashMap<String,String>> hm;
    long msG_ID;
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

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
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
        Log.e("fragment list: ",parent.getItemAtPosition(position).toString());
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


        String temp=null;
        String type=null;
        int m=0;
        String body;
        JSONObject jso = new JSONObject();
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


        Log.e(jsa.toString(), "JSON");
        if(hm.size()>0) {
            travelList = new CustomAdapter(context, hm);
            travelList.setListView(getListView());
            getListView().setAdapter(travelList);
        }


    }
    private class MyAsyncTask extends AsyncTask<List<HashMap<String,String>>,Integer,String> {

        @Override
        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground()
        }

        @Override
        protected String doInBackground(List<HashMap<String, String>>... params) {
            // Perform an operation on a background thread
            Log.e("background","devanshu");
            getSMS(getActivity());
            return null;
        }



        @Override
        protected void onPostExecute(String result) {
            // Runs on the UI thread after doInBackground()

        }


    }

}

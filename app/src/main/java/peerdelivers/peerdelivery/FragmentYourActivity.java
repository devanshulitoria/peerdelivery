package peerdelivers.peerdelivery;
//[0]-> from
//[1]->TO
//[2]->PNR
//[3]->DOJ MM/DD/YYYY only
import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static peerdelivers.peerdelivery.R.drawable.bus;
import static peerdelivers.peerdelivery.R.drawable.train;


/**
 * Created by iMac on 5/25/2016.
 */
// Display SMS
public class FragmentYourActivity extends ListFragment implements AdapterView.OnItemClickListener {
    SharedPreferences smsSharedPreferences;
    Filtering ft=new Filtering();
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 69;
    private CustomAdapter travelList;
    String URL;
    View vv;
    Button newTrip;
    String session_id;
    private CustomAdapter nca;
    ListView tListView;
    Typeface custom_font;
    TextView no_further_travel_detail;
    SharedPreferences cookies;
    AsyncHttpClient myClient1;
    List<HashMap<String,String>> hm;
    long msG_ID;
    WebView viewWeb;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
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
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        viewWeb = (WebView) vv.findViewById(R.id.myWebView);
        newTrip=(Button)vv.findViewById(R.id.bt_new_trip);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        URL=getResources().getString(R.string.URL)+"/FragmentYourActivity.php";
        String sessionId=GetSessionCookie.getCookie(getContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            checkPermission();
        }
        newTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), NewTrip.class);
                startActivity(a);
            }
        });

    }
    public void checkPermission(){
        int permissioncheck=-1;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            permissioncheck=ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_SMS);
            if(permissioncheck==PackageManager.PERMISSION_GRANTED){
                getSMS(getContext());
            }
            else{
                accessPermissions();
            }
        } else {
            // Pre-Marshmallow
            getSMS(getContext());

        }

        Log.e("permission", String.valueOf(permissioncheck));
    }
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        String[]temp=input.split(" ");
        for (int i=0;i<temp.length;i++) {
            String temp2=String.valueOf(temp[i].charAt(0)).toUpperCase()+temp[i].substring(1).toLowerCase()+" ";
            titleCase.append(temp2);
        }

        return titleCase.toString();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("FragmentYourActivity ", parent.getItemAtPosition(position).toString());
        Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
        String content=(String)map.get("content");
        String temp[]=content.split("To");
        content=toTitleCase(content);
        content ="Travelling from "+content;
        String type=(String)map.get("type");
        if (type.equalsIgnoreCase("T")) {
            content=content+"by Train";
        } else if(type.equalsIgnoreCase("B")){
            content=content+"by Bus";
        }

        else if(type.equalsIgnoreCase("F")){
            content=content+"by Flight";
        }
        else{
            content=content+"by Car";
        }
        String doj=(String)map.get("doj");
        content=content+" on "+doj;
        Log.e("Final content", content);
        Log.e(temp[0], temp[1]);
        ShareLinkContent contentf = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=peerdelivers.peerdelivery"))
                .setContentTitle("PeerDelivery")
                .setContentDescription(content)
                .setQuote(content)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#PeerDelivery")
                        .build()).build();
        shareDialog.show(contentf);
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("FragmentYourActivityShare"));

    }
    public  void accessPermissions() {

        int hasWriteContactsPermission=-1;
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Checkpermission", String.valueOf(hasWriteContactsPermission));
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            } else {
                showMessageOKCancel("You need to allow access to SMS",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions( new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv=inflater.inflate(R.layout.fragment_your_activity, container, false);
        no_further_travel_detail=(TextView)vv.findViewById(R.id.no_further_travel_detail);
        custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriad-set-pro_thin.ttf");
        no_further_travel_detail.setTypeface(custom_font);
        Answers.getInstance().logContentView(new ContentViewEvent());
        return vv;
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("FragmentActivity","inside on request permission");
        Log.e("FragmentActivity", String.valueOf(requestCode));
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    getSMS(getContext());

                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "You have not granted the permission", Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public  void getSMS(Context context){

        smsSharedPreferences = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
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

        Long lastsmsId= 0L;
        int j=0;
        Log.e("last read id:", String.valueOf(msG_ID));
        hm= new LinkedList<HashMap<String, String>>();
        while (cur.moveToNext()) {
            if((i++)==0) {
                //<// TODO: 6/18/2016
                //to be uncommented during production
               lastsmsId=Long.parseLong(cur.getString(0));
                //lastsmsId=0L;
            }
            temp = cur.getString(1);
            body = cur.getString(3);

            if (Long.parseLong(cur.getString(0)) > msG_ID){
                if (body.contains("PNR")) {
                    Log.e("pnr 123",temp);
                    tDetails = ft.filter(temp,body);

                    if(tDetails!=null && tDetails[0]!=null && tDetails[1]!=null && tDetails[2]!=null && tDetails[3]!=null)
                    type=tDetails[4];
                    else
                    type="U";
                    Log.e(type, "type of transport");
                    if (type.equalsIgnoreCase("T") || type.equalsIgnoreCase("F") || type.equalsIgnoreCase("B")) {
                        Log.e("ouside return journey",String.valueOf(tDetails.length));
                        try {
                            HashMap<String,String> tDetail=new HashMap<String,String>();
                            jso = new JSONObject();
                            jso.put("TYPE", tDetails[4].trim());
                            jso.put("0", tDetails[0].trim());
                            jso.put("1", tDetails[1].trim());
                            jso.put("2",tDetails[2].trim());
                            jso.put("3", tDetails[3].trim());

                            //handling of return journey


                            if(cur.getString(3).toLowerCase().contains("cancelled") || cur.getString(3).toLowerCase().contains("cancel"))
                            jso.put("STATUS","N");
                            else
                                jso.put("STATUS","A");
                            Log.e("tdetails",tDetails[0]+"->"+tDetails[1]);
                            jsa.put(m++, jso);
                            //return journey
                            if(tDetails.length>6){
                                Log.e("inside return journey",String.valueOf(tDetails.length));

                                jso = new JSONObject();
                                jso.put("TYPE", tDetails[9].trim());
                                jso.put("0", tDetails[5].trim());
                                jso.put("1", tDetails[6].trim());
                                jso.put("2",tDetails[7].trim());
                                jso.put("3", tDetails[8].trim());

                                Log.e("tdetails return", tDetails[5] + "->" + tDetails[6]);
                                jsa.put(m++, jso);

                            }
                            String DOJ=tDetails[3].trim();
                            DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date src=format12.parse(DOJ);
                            DateFormat ft=new SimpleDateFormat("E dd MMM yyyy");
                            DOJ=ft.format(src);

                            tDetail.put("type",tDetails[4].trim());
                            tDetail.put("content", tDetails[0]+" to "+tDetails[1]);
                            tDetail.put("status", "A");
                            tDetail.put("doj",DOJ);
                            tDetail.put("doj_time",tDetails[3].trim());
                            hm.add(tDetail);

                        } catch (JSONException e) {
                            Log.e("fragmentyouractivity",e.getMessage());
                        }catch (Exception e){
                            Log.e("fragmentyouractivity",e.getMessage());
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
        String sessionId=GetSessionCookie.getCookie(getContext());
        Log.e("youractivity: ","sessionid:"+sessionId);
        if(!sessionId.equalsIgnoreCase("0")) {
            sendDataToServer(jsa.toString(), getContext());
        }
        if(hm.size()>0) {
            /*
            travelList = new CustomAdapter(context, hm);
            travelList.setListView(getListView());
            getListView().setAdapter(travelList);
            */
            TravelDataSource tds=new TravelDataSource(getContext());
            tds.insertTravel_details(hm);
            FillupCustomListAdapter();
        }
        else {
            //no_further_travel_detail.setVisibility(View.VISIBLE);

            FillupCustomListAdapter();
        }


    }

    public void FillupCustomListAdapter(){
        viewWeb.setVisibility(View.GONE);
        List<HashMap<String,String>> ll=new LinkedList<HashMap<String, String>>();
        TravelDataSource tds=new TravelDataSource(getContext());
        ll=tds.getAlldetails();
        travelList = new CustomAdapter(getContext(), ll);
        travelList.setListView(getListView());
        getListView().setAdapter(travelList);
        getListView().setOnItemClickListener(this);
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
                Log.e("your activity", str);



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
    public void onResume() {
        super.onResume();
    }

}

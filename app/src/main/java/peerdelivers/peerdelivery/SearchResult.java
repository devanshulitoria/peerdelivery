package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchResult extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private SearchResultCustomAdpater nca;
    Button filter,sort;
    String source,destination,item,noti_id;
    private PopupWindow psort,pfilter;
    private RadioGroup itemSort;
    private RadioGroup itemFilter;
    static boolean completed=false;
    private LinearLayout ll;
    TextView noOne;
    String URL;
    WebView viewWeb;
    Typeface custom_font;
    String gender="A";
    String filterSelected="ASC";
    AsyncHttpClient client;
    RequestParams params;
    JSONArray peoples = null;
    JSONObject jso=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/myriad-set-pro_thin.ttf");
        noOne=(TextView) findViewById(R.id.message_no_one);
        noOne.setTypeface(custom_font);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CheckConnection.isConnected(getApplicationContext(), SearchResult.this);
        viewWeb = (WebView)findViewById(R.id.myWebView);
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        URL=getResources().getString(R.string.URL)+"/SearchResult.php";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                source= null;
                destination=null;
                item=null;
                noti_id=null;



            } else {
                source= extras.getString("source");
                destination= extras.getString("destination");
                item=extras.getString("item");
                noti_id=extras.getString("noti_id");


            }
        } else {
            source= (String) savedInstanceState.getSerializable("source");
            destination= (String) savedInstanceState.getSerializable("destination");
            item= (String) savedInstanceState.getSerializable("item");
            noti_id= (String) savedInstanceState.getSerializable("noti_id");
        }
        if(source!=null && destination!=null && item!=null){
            FetchSearchResultFromServer("all");
        }
        filter=(Button)findViewById(R.id.filter);
        ll=(LinearLayout)findViewById(R.id.ll_search_activity);
        sort=(Button)findViewById(R.id.sort);
        hm= new LinkedList<HashMap<String, String>>();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowFilter();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowSort();
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
    public void populateListview(){
        serverNotificatioLV=(ListView) findViewById(R.id.lv_search_result);
        nca=new SearchResultCustomAdpater(SearchResult.this,hm);

        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                       Log.e("listItem", parent.getItemAtPosition(position).toString());
                Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
                String travel_id=(String)map.get("travel_id");
                Log.e("travel_id", travel_id);
                Intent a = new Intent(SearchResult.this, Result.class);
                a.putExtra("travel_id", travel_id);
                a.putExtra("source", source);
                a.putExtra("destination", destination);
                a.putExtra("item_type", item);
                startActivity(a);
                }


        });
    }
    private void initiatePopupWindowFilter() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SearchResult.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_filter,
                    (ViewGroup) findViewById(R.id.popup_filter_linear));
            pfilter = new PopupWindow(layout, 300, 370, true);
            pfilter.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pfilter.setOutsideTouchable(false);
            itemFilter = (RadioGroup) layout.findViewById(R.id.radiogrfilter);
            itemFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String selected = "A";
                    switch (checkedId) {
                        case R.id.male:
                            selected = "M";
                            break;
                        case R.id.female:
                            selected = "F";
                            break;
                        case R.id.all:
                            selected = "A";
                            break;
                    }
                    Toast.makeText(getBaseContext(), selected,
                            Toast.LENGTH_LONG).show();
                    gender=selected;
                    nca.clear();
                    FetchSearchResultFromServer(selected);
                    pfilter.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void initiatePopupWindowSort(){
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SearchResult.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popwindow_sort,
                    (ViewGroup) findViewById(R.id.popup_sort_linear));
            psort = new PopupWindow(layout, 300, 370, true);
            psort.showAtLocation(layout, Gravity.CENTER, 0, 0);
            psort.setOutsideTouchable(false);

            itemSort = (RadioGroup) layout.findViewById(R.id.radioSort);
            itemSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String selected="dateAsc";
                    switch(checkedId)
                    {
                        case R.id.dateAsc:
                            selected="ASC";
                            break;
                        case R.id.dateDesc:
                            selected="DESC";
                            break;
                    }
                    Toast.makeText(getBaseContext(), selected,
                            Toast.LENGTH_LONG).show();
                    filterSelected=selected;
                    nca.clear();
                    FetchSearchResultFromServer(selected);
                    psort.dismiss();
                }
            });
            psort.showAtLocation(ll, Gravity.NO_GRAVITY, 500, 500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public  void FetchSearchResultFromServer(String bt_option){
        String sessionId=GetSessionCookie.getCookie(getApplicationContext());
        client = new AsyncHttpClient();
        params=new RequestParams();
        client.addHeader("Cookie", "PHPSESSID=" + sessionId + "");

        try {
            jso.put("source",source);
            jso.put("destination", destination);
            jso.put("item", item);
            jso.put("noti_id", noti_id);
            jso.put("gender", gender);
            jso.put("filter", filterSelected);
            params.put("data",jso.toString());
            Log.e("JSON output",jso.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("HTTP", "STARTED");

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String obj) {
            Log.e("SearchforResult",obj.toString());
                viewWeb.setVisibility(View.GONE);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(obj));
                    peoples=jsonObj.getJSONArray("travel_details");
                    Log.e("length",String.valueOf(peoples.length()));
                    HashMap<String,String> tDetail;
                    for(int i=0;i<peoples.length();i++){
                        JSONObject c = peoples.getJSONObject(i);
                        String travel_id = c.getString("travel_id");
                        Log.e("travel_id",travel_id);
                        String name = c.getString("name");
                        String source = c.getString("source");
                        String profilePicURL=c.getString("profile_pic_url");
                        String destination=c.getString("destination");
                        String type=c.getString("type_transport");
                        String gender=c.getString("gender");
                        String doj=c.getString("doj");
                        DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date src=format12.parse(doj);
                        DateFormat ft=new SimpleDateFormat("E dd MMM yyyy");
                        doj=ft.format(src);
                        Log.e("doj",doj);
                        tDetail=new HashMap<String,String>();
                        tDetail.put("content", source+" to "+destination);
                        tDetail.put("time", doj);
                        tDetail.put("travel_id",travel_id);
                        tDetail.put("name",name);
                        tDetail.put("profilePicURL",profilePicURL);
                        tDetail.put("type",type);
                        tDetail.put("gender",gender);

                        hm.add(tDetail);
                    }
                    Log.e("m here","123");
                    populateListview();
                } catch (JSONException e) {
                    noOne.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Log.e("exception",e.getMessage().toString());
                }catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
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
}

package peerdelivers.peerdelivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
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

/**
 * Created by iMac on 5/25/2016.
 */
public class FragmentYourFriendsActivity extends ListFragment implements OnItemClickListener {
    private SearchResultCustomAdpater nca;
    private List<HashMap<String,String>> hm;
    View v;
    WebView viewWeb;
    AsyncHttpClient myClient1;
    RequestParams params;
    static AutoCompleteTextView searchFriends;
    TextView tv,no_detail_found,headertext;
    Button dismiss,invite,invite_friends;
    String URL;
    String buttonText="Invite";
    String action="D"; //D->DEFAULT->iNVITE FRIENDS
    String headerMsg="PeerDelivery is nothing without friends.Invite them all!";
    static String[] user_friends;
    String facebookToken;
    JSONArray peoples = null;
    JSONArray friends = null;
    View footerView;
    JSONObject jso=new JSONObject();
    String nextPage="2014-07-20 00:00:00";
    Typeface custom_font;
    HashMap<String,String> friend_map;
    protected SwipeRefreshLayout mswipeRefreshLayout;
    static ArrayAdapter adapter;
    public FragmentYourFriendsActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("YourFriendsActivity"));
        friend_map=new HashMap<>();
        tv=(TextView)v.findViewById(R.id.no_further_travel_detail);
        invite_friends=(Button)v.findViewById(R.id.button_invite_friends);
        searchFriends=(AutoCompleteTextView)v.findViewById(R.id.autoCompleteSearch);
        viewWeb = (WebView) v.findViewById(R.id.myWebView);
        no_detail_found=(TextView) v.findViewById(R.id.no_further_travel_detail);
        custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriad-set-pro_thin.ttf");
        no_detail_found.setTypeface(custom_font);
        facebookToken=GetSessionCookie.fbToken(getContext());
        viewWeb.loadUrl("file:///android_asset/screen.gif");
        viewWeb.setBackgroundColor(Color.TRANSPARENT);
        viewWeb.setVisibility(View.VISIBLE);
        URL=getResources().getString(R.string.URL)+"/FragmentYourFriendsActivity.php";
        hm= new LinkedList<HashMap<String, String>>();

        String sessionId=GetSessionCookie.getCookie(getContext());
        Log.e("yourFRNDactivity: ","sessionid:"+sessionId);
        if(!sessionId.equalsIgnoreCase("0"))
        fetchDataFromtheServer();

    }
    public void fillupSearchFriends(){
        viewWeb.setVisibility(View.GONE);
        if(user_friends!=null) {
            adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, user_friends);
            searchFriends.setThreshold(3);//will start working from first character
            searchFriends.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            searchFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                    Log.e("Friends", String.valueOf(parent.getItemAtPosition(pos)));
                    if (friend_map.containsKey(String.valueOf(parent.getItemAtPosition(pos)))) {

                        String facebook_id = (String) friend_map.get(String.valueOf(parent.getItemAtPosition(pos)));
                        Log.e("Friends", facebook_id);
                        Intent a = new Intent(getActivity(), profile.class);
                        a.putExtra("facebook_id", facebook_id);
                        startActivity(a);
                    }

                }

            });
        }
        else{
            tv.setText("It seems that none of your facebook friends are on PeerDelivery.");
            invite_friends.setVisibility(View.VISIBLE);
            invite_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String appLinkUrl, previewImageUrl;

                    appLinkUrl = "https://fb.me/1139275469462206";
                    previewImageUrl = "http://www.peerdelivery.in/pic/icon.png";

                    if (AppInviteDialog.canShow()) {
                        AppInviteContent content = new AppInviteContent.Builder()
                                .setApplinkUrl(appLinkUrl)
                                .setPreviewImageUrl(previewImageUrl)
                                .build();
                        AppInviteDialog appInviteDialog = new AppInviteDialog(getActivity());
                        appInviteDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<AppInviteDialog.Result>()
                        {
                            @Override
                            public void onSuccess(AppInviteDialog.Result result)
                            {
                                Log.e("invitation", result.toString());
                            }

                            @Override
                            public void onCancel()
                            {
                            }

                            @Override
                            public void onError(FacebookException e)
                            {
                                Log.e("invitation", e.getMessage().toString());
                            }
                        });
                        AppInviteDialog.show(getActivity(), content);
                    }
                }
            });
        }

    }
    public void populateListView(){

        fillupSearchFriends();
        viewWeb.setVisibility(View.GONE);
        nca=new SearchResultCustomAdpater(getContext(),hm);
        setListAdapter(nca);
        getListView().setOnItemClickListener(this);

        footerView =  LayoutInflater.from(getActivity()).inflate(R.layout.headerfriends, getListView(), false);
        dismiss=(Button) footerView.findViewById(R.id.button_dismiss);
        invite=(Button) footerView.findViewById(R.id.button_invite);
        headertext=(TextView) footerView.findViewById(R.id.header_text);


        getListView().addHeaderView(footerView);


        invite.setText(buttonText);
        headertext.setText(headerMsg);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListView().removeHeaderView(footerView);
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equalsIgnoreCase("D")) {

                String appLinkUrl, previewImageUrl;

                appLinkUrl = "https://fb.me/1139275469462206";
                previewImageUrl = "http://www.peerdelivery.in/pic/icon.png";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog appInviteDialog = new AppInviteDialog(getActivity());
                    appInviteDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<AppInviteDialog.Result>()
                    {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result)
                        {
                            Log.e("invitation", result.toString());
                        }

                        @Override
                        public void onCancel()
                        {
                        }

                        @Override
                        public void onError(FacebookException e)
                        {
                            Log.e("invitation", e.getMessage().toString());
                        }
                    });
                    AppInviteDialog.show(getActivity(), content);
                }
                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.e("listItem", parent.getItemAtPosition(position).toString());
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String facebook_id = (String) map.get("facebook_id");
                Intent a = new Intent(getActivity(), profile.class);
                a.putExtra("facebook_id", facebook_id);
                startActivity(a);





    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v=inflater.inflate(R.layout.fragment_friends_activity, container, false);
        mswipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefresh);
        mswipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.BLACK);
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromtheServer();
                getListView().removeHeaderView(footerView);
            }
        });

        return v;

    }

    public void fetchDataFromtheServer(){
        myClient1 = new AsyncHttpClient();
        String sessionId=GetSessionCookie.getCookie(getContext());
        if(!sessionId.equalsIgnoreCase("0")) {
            Log.e("FragmentFriendsActivity",sessionId);
            myClient1.addHeader("Cookie", "PHPSESSID=" + sessionId + "");
        }
        params = new RequestParams();
        params.put("data","friendActivity");
        params.put("next",nextPage);
        Log.e("next->",nextPage);

        myClient1.post(URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e("FRAGMENT ACTIVITY", "HTTP STARTED");

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(),
                        "It seems there is some issue with your internet connection", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String str) {
                // called when response HTTP status is "200 OK"
                if(mswipeRefreshLayout.isRefreshing()){
                    mswipeRefreshLayout.setRefreshing(false);
                    nca.notifyDataSetChanged();

                }
                Log.e("your friends", str);
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(String.valueOf(str));

                    friends=jsonObj.getJSONArray("friend");
                    nextPage=jsonObj.getString("next");
                    Log.e("server next",nextPage);
                    headerMsg=jsonObj.getString("message");
                    buttonText=jsonObj.getString("button_text");
                    user_friends=new String[friends.length()];
                    for(int i=0;i<friends.length();i++) {
                        JSONObject c = friends.getJSONObject(i);
                        friend_map.put(c.getString("name"),c.getString("id"));
                        user_friends[i]=c.getString("name");
                    }
                    peoples=jsonObj.getJSONArray("friend_travel_details");
                    Log.e("next",nextPage);
                    Log.e("length",String.valueOf(peoples.length()));
                    HashMap<String,String> tDetail;
                    for(int i=0;i<peoples.length();i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        String TRAVEL_ID = c.getString("TRAVEL_ID");
                        String source = c.getString("SOURCE");
                        String profilePicURL=c.getString("PROFILE_PIC_URL");
                        String destination=c.getString("DESTINATION");
                        String type=c.getString("TYPE_TRANSPORT");
                        String fb_id=c.getString("FACEBOOK_ID");
                        String NAME=c.getString("NAME");
                        String DOJ=c.getString("DOJ");
                        DateFormat format12=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date src=format12.parse(DOJ);
                        DateFormat ft=new SimpleDateFormat("E dd MMM yyyy");
                        DOJ=ft.format(src);
                        Log.e("doj",DOJ);
                        tDetail=new HashMap<String,String>();
                        tDetail.put("content", source+" to "+destination);
                        tDetail.put("time", DOJ);
                        tDetail.put("travel_id",TRAVEL_ID);
                        tDetail.put("name",NAME);
                        tDetail.put("profilePicURL",profilePicURL);
                        tDetail.put("facebook_id",fb_id);
                        Log.e("profilePicURL",profilePicURL);
                        tDetail.put("type",type);
                        hm.add(tDetail);
                    }
                    populateListView();
                }catch(JSONException e) {
                    e.printStackTrace();
                    Log.e("exception",e.getMessage().toString());
                    fillupSearchFriends();
                    no_detail_found.setVisibility(View.VISIBLE);
                }catch (ParseException e) {
                    e.printStackTrace();
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
}

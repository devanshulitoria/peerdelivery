package peerdelivers.peerdelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by iMac on 5/22/2016.
 */
public class FragmentRejected extends ListFragment implements AdapterView.OnItemClickListener{
    OnHeadlineSelectedListenerR mCallback;
    NotificationCustomAdapter nca;

    View vv;
    static TextView tv;
    static ListView lv_rejected;
    private static List<HashMap<String,String>> data=new LinkedList<HashMap<String, String>>();
    static int counter=0;
    public FragmentRejected() {
        // Required empty public constructor
        Log.e("FragmentRejected","constructor");
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Fragment REJECTED:", "ON ACTIVITY CREATED CALLED");
        lv_rejected=getListView();
        lv_rejected.setOnItemClickListener(this);
        if((counter++) <2) {
            Log.e("Fragment Rejected", "inside if condition");
            mCallback.fetchDataFromtheServer(2);

        }
        else{
            Log.e("Fragment Rejected","inside else consition");
            populateListView(data);
        }

        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("FragmentRejected"));
    }
    public void nothinghere(){
        Log.e("Nothing called","Rejected");
        lv_rejected.setVisibility(View.INVISIBLE);
        tv.setText("Nothing in here right now!");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv=inflater.inflate(R.layout.fragment_rejected, container, false);
        tv=(TextView)vv.findViewById(R.id.section_label);

        return vv;

    }
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListenerR {
         void fetchDataFromtheServer(int position);
    }
    public void populateListView(List<HashMap<String,String>> accepted){
        Log.e("Fragment rejected","populateListView:"+accepted.size());
        nca=new NotificationCustomAdapter(getContext(),accepted);
        lv_rejected.setAdapter(nca);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListenerR) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
        String noti_id=(String)map.get("noti_id");
        String noti_type=(String)map.get("noti_type");
        String reference_id=(String)map.get("request_id");
        String cmd=(String)map.get("command");
        Log.e("rejected",noti_id+"--"+reference_id+"--"+cmd);
    }
}

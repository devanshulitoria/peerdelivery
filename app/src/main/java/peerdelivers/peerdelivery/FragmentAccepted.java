package peerdelivers.peerdelivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class FragmentAccepted extends ListFragment implements AdapterView.OnItemClickListener {
    static ListView lvAccepted;
    private static List<HashMap<String,String>> data=new LinkedList<HashMap<String, String>>();
    static int counter=0;
    View vv;
    static TextView tv;

    OnHeadlineSelectedListener mCallback;
     NotificationCustomAdapter nca;
    public FragmentAccepted() {
        // Required empty public constructor
        Log.e("FragmentAccepted", "constructor");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void fetchDataFromtheServer(int position);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Fragment accepted:", "ON ACTIVITY CREATED CALLED");
        Answers.getInstance().logContentView(new ContentViewEvent().putContentName("FragmentAccepted"));
        lvAccepted=getListView();
        lvAccepted.setOnItemClickListener(this);

        if((counter++) <2) {
            Log.e("Fragment accepted", "inside if condition");
            mCallback.fetchDataFromtheServer(0);

        }
        else{
            Log.e("Fragment accepted","inside else consition");
            populateListView(data);
        }
    }
    public void populateListView(List<HashMap<String,String>> accepted){
        Log.e("Fragmentaccepted", "populateListView:" + accepted.size());
        nca=new NotificationCustomAdapter(getContext(),accepted);
        lvAccepted.setAdapter(nca);

    }
    public void nothinghere(){
        Log.e("Nothing called","accepted");
        lvAccepted.setVisibility(View.INVISIBLE);
        tv.setText("Nothing in here right now!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv=inflater.inflate(R.layout.fragment_accepted, container, false);
        tv=(TextView)vv.findViewById(R.id.section_label);

        return vv;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
        String noti_id=(String)map.get("noti_id");
        String noti_type=(String)map.get("noti_type");
        String reference_id=(String)map.get("request_id");
        String cmd=(String)map.get("command");
            if(cmd.equalsIgnoreCase("H")) {
                Log.e("accepted", noti_id + "--" + reference_id + "--" + cmd);
                Intent a = new Intent(getActivity(), activity_accepted.class);
                a.putExtra("noti_id", noti_id);
                a.putExtra("reference_id", reference_id);
                startActivity(a);
                getActivity().finish();
            }
    }
}

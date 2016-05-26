package peerdelivers.peerdelivery;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by iMac on 5/25/2016.
 */
public class FragmentYourFriendsActivity extends ListFragment implements OnItemClickListener {
    private NotificationCustomAdapter nca;
    private List<HashMap<String,String>> hm;
    View v;
    TextView tv;
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
        tv=(TextView)v.findViewById(R.id.no_further_travel_detail);
        hm= new LinkedList<HashMap<String, String>>();
        HashMap<String,String> tDetail=new HashMap<String,String>();
        tDetail.put("content", "xyz");
        tDetail.put("time", "xxx min ago");
        hm.add(tDetail);
        nca=new NotificationCustomAdapter(getContext(),hm);
        setListAdapter(nca);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("FragFriendsActivity: ", parent.getItemAtPosition(position).toString());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_friends_activity, container, false);
        return v;

    }
}

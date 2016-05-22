package peerdelivers.peerdelivery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by iMac on 5/22/2016.
 */
public class FragmentAccepted extends ListFragment implements AdapterView.OnItemClickListener {
    ListView lvAccepted;
    private List<HashMap<String,String>> hm;
    private NotificationCustomAdapter nca;
    public FragmentAccepted() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hm= new LinkedList<HashMap<String, String>>();
        HashMap<String,String> tDetail=new HashMap<String,String>();
        tDetail.put("content", "xyz");
        tDetail.put("time", "xxx min ago");
        hm.add(tDetail);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        nca=new NotificationCustomAdapter(getContext(),hm);
        setListAdapter(nca);
        getListView().setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.e("fragment list: ",parent.getItemAtPosition(position).toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted, container, false);
    }
}

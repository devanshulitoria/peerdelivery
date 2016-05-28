package peerdelivers.peerdelivery;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchResult extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private SearchResultCustomAdpater nca;
    Button filter,sort;
    private PopupWindow psort,pfilter;
    private RadioGroup itemSort;
    private RadioGroup itemFilter;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        CheckConnection.isConnected(getApplicationContext(), SearchResult.this);
        filter=(Button)findViewById(R.id.filter);
        ll=(LinearLayout)findViewById(R.id.ll_search_activity);
        sort=(Button)findViewById(R.id.sort);
        hm= new LinkedList<HashMap<String, String>>();
        serverNotificatioLV=(ListView) findViewById(R.id.lv_search_result);
        HashMap<String,String> tDetail=new HashMap<String,String>();
        tDetail.put("content", "xyz");
        tDetail.put("time", "18-June-2016");
        hm.add(tDetail);
        tDetail.put("content", "xyz1");
        tDetail.put("time", "20-June-2016");
        hm.add(tDetail);
        nca=new SearchResultCustomAdpater(SearchResult.this,hm);
        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + parent.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();
            }
        });
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
                    String selected="all";
                    switch(checkedId)
                    {
                        case R.id.male:
                            selected="male";
                            break;
                        case R.id.female:
                            selected="female";
                            break;
                        case R.id.all:
                            selected="all";
                            break;
                    }
                    Toast.makeText(getBaseContext(), selected,
                            Toast.LENGTH_LONG).show();
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
                            selected="dateAsc";
                            break;
                        case R.id.dateDesc:
                            selected="dateDesc";
                            break;
                    }
                    Toast.makeText(getBaseContext(), selected,
                            Toast.LENGTH_LONG).show();
                    psort.dismiss();
                }
            });
            psort.showAtLocation(ll,Gravity.NO_GRAVITY,500,500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

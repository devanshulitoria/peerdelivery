package peerdelivers.peerdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchResult extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private SearchResultCustomAdpater nca;
    Button filter,sort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        CheckConnection.isConnected(getApplicationContext(), SearchResult.this);
        filter=(Button)findViewById(R.id.filter);
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
                PopupMenu popup = new PopupMenu(SearchResult.this, sort);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                SearchResult.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
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

package peerdelivers.peerdelivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ServerNotifications extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private NotificationCustomAdapter nca;
    Button sort,filter,mark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_notifications);
        CheckConnection.isConnected(getApplicationContext(), ServerNotifications.this);
        sort=(Button)findViewById(R.id.bt_sort);
        filter=(Button)findViewById(R.id.bt_filter);
        mark=(Button)findViewById(R.id.bt_mark_as_read);


        hm= new LinkedList<HashMap<String, String>>();
        serverNotificatioLV=(ListView) findViewById(R.id.listViewServerNotification);
        final HashMap<String,String> tDetail=new HashMap<String,String>();
        tDetail.put("content", "xyz");
        tDetail.put("time", "xxx min ago");
        hm.add(tDetail);

        tDetail.put("content","xyz1");
        tDetail.put("time", "xxx min ago2");
        hm.add(tDetail);
        nca=new NotificationCustomAdapter(ServerNotifications.this,hm);
        nca.setListView(serverNotificatioLV);
        serverNotificatioLV.setAdapter(nca);
        serverNotificatioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + parent.getItemAtPosition(position), Toast.LENGTH_LONG)
                        .show();
                Intent goToNextActivity = new Intent(ServerNotifications.this, activity_accepted.class);
                startActivity(goToNextActivity);
            }
        });

            sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),
                            "Sorting", Toast.LENGTH_LONG)
                            .show();
                }
            });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Filtering", Toast.LENGTH_LONG)
                        .show();
            }

        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Marking", Toast.LENGTH_LONG)
                        .show();
                nca.remove(2);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_notifications, menu);
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

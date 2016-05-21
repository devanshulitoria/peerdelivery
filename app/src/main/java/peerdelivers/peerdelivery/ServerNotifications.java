package peerdelivers.peerdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ServerNotifications extends AppCompatActivity {
    ListView serverNotificatioLV;
    private List<HashMap<String,String>> hm;
    private NotificationCustomAdapter nca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_notifications);
        CheckConnection.isConnected(getApplicationContext(), ServerNotifications.this);
        hm= new LinkedList<HashMap<String, String>>();
        serverNotificatioLV=(ListView) findViewById(R.id.listViewServerNotification);
        HashMap<String,String> tDetail=new HashMap<String,String>();
        tDetail.put("content", "xyz");
        tDetail.put("time", "xxx min ago");
        hm.add(tDetail);
        HashMap<String,String> tD=new HashMap<String,String>();
        tD.put("content","xyz1");
        tD.put("time", "xxx min ago2");
        hm.add(tD);
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

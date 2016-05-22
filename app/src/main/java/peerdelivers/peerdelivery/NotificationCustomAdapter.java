package peerdelivers.peerdelivery;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iMac on 5/19/2016.
 */
public class NotificationCustomAdapter extends ArrayAdapter<HashMap<String,String>> {
    private final Context context;
    private final List<HashMap<String,String>> notificationList;
    private float density = 2f;
    private ListView listView;
    String str;
    Typeface custom_font;
    TextView nHeading,nTime,nContent,nId;
    LinearLayout nLayout;
    public NotificationCustomAdapter(Context context, List<HashMap<String,String>> objects) {
        super(context, R.layout.notificationlv, objects);
        this.context = context;
        this.notificationList = objects;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.notificationlv, parent, false);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/myriad-set-pro_thin.ttf");
        nHeading = (TextView) rowView.findViewById(R.id.lvHeading);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
        str=notificationList.get(position).get("content").toString();
        nHeading.setText(str);
        nHeading.setTypeface(custom_font);
//        String not_id = notificationList.get(position).get("id").toString();
//        String not_type = notificationList.get(position).get("type").toString();
//        String not_heading = notificationList.get(position).get("heading").toString();
//        String not_content = notificationList.get(position).get("content").toString();
//        String not_time = notificationList.get(position).get("time").toString();

        return rowView;
    }
    public void setListView(ListView view) {
        listView = view;
    }
}

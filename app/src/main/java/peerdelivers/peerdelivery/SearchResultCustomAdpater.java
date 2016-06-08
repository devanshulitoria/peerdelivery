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

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iMac on 5/23/2016.
 */
public class SearchResultCustomAdpater extends ArrayAdapter<HashMap<String,String>> {
    private final Context context;
    private final List<HashMap<String,String>> searchList;
    private float density = 2f;
    private ListView listView;
    String str;
    Typeface custom_font;
    TextView nHeading,nTime,nContent,nId;
    LinearLayout nLayout;
    public SearchResultCustomAdpater(Context context, List<HashMap<String,String>> objects) {
        super(context, R.layout.search_listview_adapter, objects);
        this.context = context;
        this.searchList = objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.search_listview_adapter, parent, false);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/myriad-set-pro_thin.ttf");
        nHeading = (TextView) rowView.findViewById(R.id.dest_source);
        nTime= (TextView) rowView.findViewById(R.id.doj);
        nContent=(TextView) rowView.findViewById(R.id.user_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        String picURL=searchList.get(position).get("profilePicURL").toString();
        Picasso.with(context).load(picURL).into(imageView);
        str=searchList.get(position).get("content").toString();
        String not_time = searchList.get(position).get("time").toString();
        String not_heading = searchList.get(position).get("name").toString();

        nHeading.setText(not_heading);
        nTime.setText(not_time);
        nContent.setText(str);

        nHeading.setTypeface(custom_font);
        nTime.setTypeface(custom_font);
        nContent.setTypeface(custom_font);
//        String not_id = notificationList.get(position).get("id").toString();
//        String not_type = notificationList.get(position).get("type").toString();
//
//        String not_content = notificationList.get(position).get("content").toString();
//

        return rowView;
    }
    public void setListView(ListView view) {
        listView = view;
    }
}

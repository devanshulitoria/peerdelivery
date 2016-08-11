package peerdelivers.peerdelivery;
/*
notification types
R-> REQUESTED TO CARRY AN ITEM
A->ACCEPTED TO CARRY AN ITEM
N->REJECTED TO CARRY THE ITEM

 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iMac on 5/19/2016.
 */
public class NotificationCustomAdapter extends ArrayAdapter<HashMap<String,String>> {
    private final Context context;
    private final List<HashMap<String,String>> notificationList;
    private float density = 2f;
    private ListView listView;
    String str,tNoti,profile_pic_url;
    Typeface custom_font;
    String TAG="NotificationCustomAdapter";
    TextView nHeading,nTime,nContent,nId;
    LinearLayout nLayout;
    public NotificationCustomAdapter(Context context, List<HashMap<String,String>> objects) {
        super(context, R.layout.notification_custom_adapter, objects);
        this.context = context;
        this.notificationList = objects;


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.notification_custom_adapter, parent, false);
        RelativeLayout rl=(RelativeLayout)rowView.findViewById(R.id.rl_notification);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/myriad-set-pro_thin.ttf");
        nHeading = (TextView) rowView.findViewById(R.id.lvHeading);
        nTime= (TextView) rowView.findViewById(R.id.lvContent);
        CircleImageView imageView = (CircleImageView) rowView.findViewById(R.id.lvImage);
        imageView.setBorderColor(Color.parseColor("#FFFFFF"));
        imageView.setBorderWidth(2);
        str=notificationList.get(position).get("content").toString();
        tNoti=notificationList.get(position).get("time").toString();
        profile_pic_url=notificationList.get(position).get("profilePicURL").toString();
       nHeading.setText(str);
        nTime.setText(tNoti);
        Picasso.with(context).load(profile_pic_url).placeholder(context.getResources().getDrawable(R.drawable.showman)).error(context.getResources().getDrawable(R.drawable.showman)).into(imageView);
        nHeading.setTypeface(custom_font);
        nTime.setTypeface(custom_font);
        if(notificationList.get(position).get("noti_status").toString().equalsIgnoreCase("R")){
            rl.setBackgroundColor(20535252);
        }

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
    public void remove(int position){
        for(int i=0;i<position;i++) {
            //Log.e("custom notification", String.valueOf(i));
            remove(getItem(0));
            notifyDataSetChanged();
        }

    }
}

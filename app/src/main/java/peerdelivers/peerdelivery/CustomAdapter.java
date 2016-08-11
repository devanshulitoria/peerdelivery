package peerdelivers.peerdelivery;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static peerdelivers.peerdelivery.R.drawable.bus;
import static peerdelivers.peerdelivery.R.drawable.train;

/**
 * Created by iMac on 5/16/2016.
 */
public class CustomAdapter extends ArrayAdapter<HashMap<String,String>> {
    private final Context context;
    private final List<HashMap<String,String>> smsList;
    private float density = 2f;
    private ListView listView;
    Typeface custom_font;

    public CustomAdapter(Context context, List<HashMap<String,String>> objects) {
        super(context, R.layout.listview_layout, objects);
        this.context = context;
        this.smsList = objects;

    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        String[]temp=input.split(" ");
        for (int i=0;i<temp.length;i++) {
          String temp2=String.valueOf(temp[i].charAt(0)).toUpperCase()+temp[i].substring(1).toLowerCase()+" ";
            titleCase.append(temp2);
        }

        return titleCase.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/myriad-set-pro_thin.ttf");
        TextView textView = (TextView) rowView.findViewById(R.id.dest_source);
        TextView tv_doj = (TextView) rowView.findViewById(R.id.doj);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.typeImage);
        textView.setText(toTitleCase(smsList.get(position).get("content").toString()));
        tv_doj.setText(smsList.get(position).get("doj").toString());
        textView.setTypeface(custom_font);
        tv_doj.setTypeface(custom_font);

        String s = smsList.get(position).get("type").toString();
        if (s.equalsIgnoreCase("T")) {
            imageView.setImageResource(train);
        } else if(s.equalsIgnoreCase("B")){
            imageView.setImageResource(bus);
        }

        else if(s.equalsIgnoreCase("F")){
            imageView.setImageResource(R.drawable.airplane);
        }
        else{
            imageView.setImageResource(R.drawable.car);
        }


        return rowView;
    }






    public void setListView(ListView view) {
        listView = view;
    }



}


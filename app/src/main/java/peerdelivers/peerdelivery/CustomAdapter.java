package peerdelivers.peerdelivery;

import android.content.Context;
import android.graphics.Typeface;
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

import java.util.HashMap;
import java.util.List;

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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/myriad-set-pro_thin.ttf");
        TextView textView = (TextView) rowView.findViewById(R.id.listTextView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
        textView.setText(smsList.get(position).get("content").toString());
        textView.setTypeface(custom_font);

        String s = smsList.get(position).get("type").toString();
        if (s.equalsIgnoreCase("train")) {
            imageView.setImageResource(train);
        } else {
            imageView.setImageResource(R.drawable.airplane);
        }

        final AudioObjectHolder holder = getAudioObjectHolder(rowView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);
        if(smsList.get(position).get("status").equalsIgnoreCase("A"))
        rowView.setOnTouchListener(new SwipeDetector(holder, position));

        return rowView;
    }

    private AudioObjectHolder getAudioObjectHolder(View workingView) {
        Object tag = workingView.getTag();
        AudioObjectHolder holder = null;

        if (tag == null || !(tag instanceof AudioObjectHolder)) {
            holder = new AudioObjectHolder();
            holder.mainView = (LinearLayout)workingView.findViewById(R.id.audio_object_mainview);
            holder.deleteView = (RelativeLayout)workingView.findViewById(R.id.audio_object_deleteview);
            holder.shareView = (RelativeLayout)workingView.findViewById(R.id.audio_object_shareview);

            /* initialize other views here */

            workingView.setTag(holder);
        } else {
            holder = (AudioObjectHolder) tag;
        }

        return holder;
    }


    public static class AudioObjectHolder {
        public LinearLayout mainView;
        public RelativeLayout deleteView;
        public RelativeLayout shareView;

        /* other views here */
    }

    public void setListView(ListView view) {
        listView = view;
    }

    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private AudioObjectHolder holder;
        private int position;

        public SwipeDetector(AudioObjectHolder h, int pos) {
            holder = h;
            position = pos;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

                    if (deltaX > 0) {
                        holder.deleteView.setVisibility(View.GONE);
                    } else {
                        // if first swiped left and then swiped right
                        holder.deleteView.setVisibility(View.VISIBLE);
                    }

                    swipe(-(int) deltaX);
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        swipeRemove();
                    } else {
                        swipe(0);
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

                    holder.deleteView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    holder.deleteView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipe(int distance) {
            View animationView = holder.mainView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

        private void swipeRemove() {
            remove(getItem(position));
            notifyDataSetChanged();
        }
    }

}


package peerdelivers.peerdelivery;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;

/**
 * Created by iMac on 5/19/2016.
 */
public class FontFace {
    SpannableString s;
    public SpannableString setTitleFont(String title){
        s = new SpannableString(title);
        s.setSpan(new TypefaceSpan("myriad-set-pro_thin.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;

    }
}

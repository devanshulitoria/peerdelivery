package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iMac on 6/5/2016.
 */
public class GetSessionCookie {
    public static String getCookie(Context ct){
        SharedPreferences cookies;
        cookies = ct.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        String session_id=cookies.getString("phpID", "0");
        return session_id;
    }
}

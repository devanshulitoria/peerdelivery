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
    public static String getFacebookid(Context ct){
        SharedPreferences sharedpreferences;

        sharedpreferences = ct.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String facebook_id=sharedpreferences.getString("facebook_id", "0");
        return facebook_id;
    }
    public static String fbToken(Context ct){
        SharedPreferences sharedpreferences;

        sharedpreferences = ct.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String fbToken=sharedpreferences.getString("fbToken", "0");
        return fbToken;
    }
    public static String gender(Context ct){
        SharedPreferences sharedpreferences;

        sharedpreferences = ct.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String fbToken=sharedpreferences.getString("gender", "M");
        return fbToken;
    }
    public static String user_name(Context ct){
        SharedPreferences sharedpreferences;

        sharedpreferences = ct.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String fbToken=sharedpreferences.getString("current_user_name", "PeerDelivery");
        return fbToken;
    }
    public static String phoneNumber(Context ct){
        SharedPreferences sharedpreferences;

        sharedpreferences = ct.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String fbToken=String.valueOf(sharedpreferences.getLong("phNumber", 0L));
        return fbToken;
    }
}

package peerdelivers.peerdelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by iMac on 5/27/2016.
 */
public class CheckPreferences {

    public static boolean hasAlreadySignedUp(Context context){
        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        Log.e("CheckPref", String.valueOf(sharedpreferences.getLong("phNumber", 0)));
        if(sharedpreferences.getLong("phNumber", 0)==0) {

            return false;
        }
        else
            return true;
    }
}

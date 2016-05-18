package peerdelivers.peerdelivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by iMac on 5/18/2016.
 */
public  class CheckConnection {
    public static void isConnected(final Context context,final Activity a){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.e("Inside is connected",String.valueOf(isConnected));
        if(!isConnected){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(a);
            alertDialogBuilder.setTitle("Oh snap!");
            alertDialogBuilder
                    .setMessage("Mother fucker your internet is disabled!")
                    .setCancelable(false)
                    .setPositiveButton("Exit now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                           a.finish();

                        }

                    });


            // show it
            alertDialogBuilder.show();

        }


    }
}

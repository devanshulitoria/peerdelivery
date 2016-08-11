package peerdelivers.peerdelivery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * Created by iMac on 5/5/2016.
 */
public class IncomingSms extends BroadcastReceiver {
PreStart pp;



    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        Intent mServiceIntent = new Intent(context,NotifyService.class);
        context.getApplicationContext().startService(mServiceIntent);
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    Log.e("devanshu broadcast",senderNum+","+message+","+PreStart.phoneNo+","+PreStart.message[0]);
                    if (pp.phoneNo.contentEquals(senderNum) && pp.message[0].contentEquals(message)){
                        Log.e("devanshu ", "inside if condition");

                        Intent inte = new Intent(context,FacebookLogin.class);
                        inte.putExtra("phNumber",PreStart.phoneNo);
                        inte.putExtra("auth_code",PreStart.uuid);

                        inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(inte);



                    }


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }



}

package peerdelivers.peerdelivery;

import android.util.Log;

/**
 * Created by iMac on 5/4/2016.
 */
public class Filtering {
    String sp[];
    final String t="TRAIN";
    final String f="FLIGHT";
    final String u="UNKNOWN";
    String sms = "";
    //String temp=null;
    public String filter(String str){
        //Filtered all personal conversations
        Log.e("Inside of filter",str);
        if(str.length()<10) {
            Log.e("Filter",str);
            try{
            if (str.contains("-")) {
                Log.e("----------------------",str);
                sp = str.split("-");


                if (sp[1].substring(0, 5).equalsIgnoreCase("irctc")) {
                    Log.e("irctc","irctc");
                    return t;

                } else if (sp[1].substring(0, 6).equalsIgnoreCase("goibib")) {
                    Log.e("goibib","goibib");
                    return f;
                } else if (sp[1].substring(0, 6).equalsIgnoreCase("mmtrip")) {
                    Log.e("mmtrip","mmtrip");
                    return f;
                } else if (sp[1].substring(0, 6).equalsIgnoreCase("spicej")) {
                    Log.e("spicej","spicej");
                    return f;
                } else
                    return u;

            }
        }catch (StringIndexOutOfBoundsException e){
                Log.e("catch","catch");
                return u;
            }
        }
        return u;
    }
}

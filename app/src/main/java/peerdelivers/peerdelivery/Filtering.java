package peerdelivers.peerdelivery;
//[0]-> from
//[1]->TO
//[2]->PNR
//[3]->DOJ MM/DD/YYYY only
import android.util.Log;

/**
 * Created by iMac on 5/4/2016.
 */
public class Filtering {
    String sp[];
    final String t="T";
    final String f="F";
    final String u="U";
    final String b="B";
    String sms = "";
    String returnData[];
    //String temp=null;
    public String[] filter(String str,String body){
        //Filtered all personal conversations
        Log.e("Inside of filter",str);
        returnData=new String[6];

        if(str.length()<10 && !str.contains("9") && !str.contains("8")&& !str.contains("7")&& !str.contains("6")&& !str.contains("5")&& !str.contains("4")&& !str.contains("3")&& !str.contains("2")&& !str.contains("1")) {
            Log.e("Filter",str);
            try{
            if (str.contains("-")) {
                Log.e("filtering",str);
                sp = str.split("-");


                if (sp[1].substring(0, 5).equalsIgnoreCase("irctc")) {

                    returnData=TextProcessing.irtcProcessing(body);
                    //Log.e("filtering irctc",String.valueOf(returnData.length));
                    if(returnData==null)
                    return null;
                    else{
                        returnData[4]=t;
                        return returnData;
                    }

                } else if (sp[1].substring(0, 6).equalsIgnoreCase("goibib")) {
                    Log.e("goibib","goibib");
                    String[] tkn=TextProcessing.striping(body);
                    returnData=TextProcessing.goIbiboProcessing(tkn);
                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        return returnData;
                    }
                } else if (sp[1].substring(0, 6).equalsIgnoreCase("mmtrip")) {
                    Log.e("mmtrip","mmtrip");
                    String[] tkn=TextProcessing.striping(body);
                    returnData=TextProcessing.makeMyTripProcessing(tkn);
                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        return returnData;
                    }
                } else if (sp[1].substring(0, 6).equalsIgnoreCase("spicej")) {
                    Log.e("spicej","spicej");
                    returnData=null;
                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        return returnData;
                    }
                } else if(sp[1].substring(0, 6).equalsIgnoreCase("redbus")){
                    Log.e("filtering","redbus");
                    String[] tkn=TextProcessing.striping(body);
                    returnData=TextProcessing.redBusProcessing(tkn, body);

                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=b;
                        return returnData;
                    }
                }
                else if(sp[1].substring(0, 6).equalsIgnoreCase("yatraa")){
                    Log.e("filtering","yatraa");
                    String[] tkn=TextProcessing.striping(body);
                    returnData=TextProcessing.yatraaProcessing(tkn);
                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        return returnData;
                    }
                }

                else
               return null;

            }
        }catch (StringIndexOutOfBoundsException e){
                Log.e("catch","catch");
                return null;
            }
        }
        return null;
    }
}

package peerdelivers.peerdelivery;
//[0]-> from
//[1]->TO
//[2]->PNR
//[3]->DOJ MM/DD/YYYY only
import android.util.Log;

import java.util.Arrays;

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

        if(str.length()<10 && !str.contains("9") && !str.contains("8")&& !str.contains("7")&& !str.contains("6")&& !str.contains("5")&& !str.contains("4")&& !str.contains("3")&& !str.contains("2")) {
            Log.e("Filter",str);
            try{

            if (str.contains("-")) {
                Log.e("filtering",str);

                sp = str.split("-");
                Log.e("filter 123",sp[1]);

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
                    Log.e("goibibo","experiment");
                    String[]returntrip=new String[10];
                    String[] tkn=TextProcessing.striping(body);
                    for(int m=0;m<tkn.length;m++)
                        Log.e("token loop",tkn[m]);
                    if(tkn.length<18)
                    {
                        Log.e("m here 123",String.valueOf(tkn.length));
                        returnData=TextProcessing.goIbiboProcessing(Arrays.copyOfRange(tkn, 0, tkn.length));
                        if(returnData==null)
                            return null;
                        else{
                            returnData[4]=f;
                            return returnData;
                        }
                    }
                    else{
                        returnData=TextProcessing.goIbiboProcessing(Arrays.copyOfRange(tkn, 0, 11));
                        returnData[4]=f;
                        int counterR=0;
                        Log.e("Array lenght1",String.valueOf(returnData.length));
                        for(int k=0;k<returnData.length;k++){
                            Log.e("inside of loop1",returnData[k]);
                            returntrip[counterR++]=returnData[k];
                        }

                        returnData=TextProcessing.goIbiboProcessing(Arrays.copyOfRange(tkn, 9, tkn.length));
                        returnData[4]=f;
                        Log.e("Array lenght2",String.valueOf(returnData.length));
                        for(int k=0;k<returnData.length;k++){
                            Log.e("inside of loop1",returnData[k]);
                            returntrip[counterR++]=returnData[k];
                        }
                        return returntrip;
                    }

                } else if (sp[1].substring(0, 6).equalsIgnoreCase("mmtrip")) {
                    String[] tkn=TextProcessing.striping(body);
                    Log.e("makemytrip","content of token");
                    for(int y=0;y<tkn.length;y++){
                        Log.e("token",tkn[y]+"-->"+String.valueOf(y));
                    }
                    returnData=TextProcessing.makeMyTripProcessing(tkn);
                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        Log.e("makemytrip","return data");
                        for(int y=0;y<returnData.length;y++){
                            Log.e("returnData",returnData[y]);
                        }
                        return returnData;
                    }
                } else if (sp[1].substring(0, 6).equalsIgnoreCase("spicej")) {
                    Log.e("spicej","spicej");
                    body=body.toUpperCase();
                    body=body.substring(body.indexOf("YOU"));
                    String[] tkn=TextProcessing.striping(body);
                    Log.e("Filtering","m inside spicejet");
                    returnData =TextProcessing.spicejet(tkn);

                    if(returnData==null)
                        return null;
                    else{
                        returnData[4]=f;
                        for(int i=0;i<returnData.length;i++){
                            Log.e("returnData",returnData[i]);
                        }
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
        }catch (Exception e){
                Log.e("catch","catch");
                return null;
            }
        }
        return null;
    }
}

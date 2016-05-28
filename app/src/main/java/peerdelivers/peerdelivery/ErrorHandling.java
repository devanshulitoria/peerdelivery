package peerdelivers.peerdelivery;

import java.util.HashMap;

/**
 * Created by iMac on 5/19/2016.
 */
public class ErrorHandling {
    static HashMap<String,String> errors=new HashMap<>();
    public static void initializing() {
        errors.put("SC01", "User not logged in");
        errors.put("MN01", "Error in Insertion! User already exists");
        errors.put("SP01", "Invalid User!Intrusion Detected");
    }
    public static String displayError(String errCode){
        initializing();
        if(errors.containsKey(errCode))
            return errors.get(errCode);
        return "Unknown Error";

    }

}

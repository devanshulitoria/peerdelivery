package peerdelivers.peerdelivery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by iMac on 6/24/2016.
 */
public class TravelDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { "travel_id","doj","doj_time","travel_points","type_travel"};
    public TravelDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
    public boolean insertTravel_details(List<HashMap<String, String>> details)
    {
        Log.e("Traveldata","inside of insertTravel_details");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues ;
        for(int i=0;i<details.size();i++) {
            HashMap<String,String> tDetail=new HashMap<String,String>();
            tDetail=details.get(i);
            contentValues = new ContentValues();
            contentValues.put("doj", tDetail.get("doj"));
            contentValues.put("doj_time", tDetail.get("doj_time"));
            contentValues.put("travel_points", toTitleCase(tDetail.get("content")));
            contentValues.put("type_travel", tDetail.get("type"));
            db.insert("travel_details", null, contentValues);
        }
        return true;
    }
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
    public Cursor getData(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from travel_details ORDER BY date(doj_time) DESC", null );
        return res;
    }
    public void DropTable(Context ct){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ct.deleteDatabase("peerdelivery.db");
    }
    public List<HashMap<String,String>>  getAlldetails()
    {
        Log.e("Traveldata","inside of getalldetails");
        List<HashMap<String,String>> ll=new LinkedList<HashMap<String, String>>();

        //hp = new HashMap();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from travel_details ORDER BY date(doj_time) DESC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            HashMap<String,String> tDetail=new HashMap<String,String>();

            tDetail.put("doj",res.getString(res.getColumnIndex("doj")));
            tDetail.put("content",res.getString(res.getColumnIndex("travel_points")));
            tDetail.put("type",res.getString(res.getColumnIndex("type_travel")));
            ll.add(tDetail);
            res.moveToNext();
        }
        return ll;
    }

}

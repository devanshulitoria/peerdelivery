package peerdelivers.peerdelivery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by iMac on 6/24/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "peerdelivery.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table if not exists travel_details(travel_id integer primary key autoincrement,doj text,doj_time text,travel_points text,type_travel CHAR(1))";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("MySQLiteHelper","Oncreate method called");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS travel_details" );
        onCreate(db);
    }
}

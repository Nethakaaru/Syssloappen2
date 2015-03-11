package se.mah.ad0206.syssloappen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sebastian Aspegren on 2015-03-05.
 *
 */
public class DBController extends SQLiteOpenHelper {

    private static final String DB_NAME = "choreDB";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase database;

    public DBController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    public void open(){
        database = getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public Cursor getChores(){
        return database.rawQuery("SELECT chore, points from chores", new String[]{});
    }
    public void save(String chore,String points){
        ContentValues values = new ContentValues();
        values.put("chore", chore);
        values.put("points",points);

        database.insert("chores", null, values);
    }

    public void saveHistory(String chore, String points, String date){
        ContentValues values = new ContentValues();
        values.put("chore",chore);
        values.put("points",points);
        values.put("date",date);

        database.insert("history",null,values);
    }
    public Cursor getHistory(){
        return database.rawQuery("SELECT chore, points, date from history", new String[]{});
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE table chores ( chore VARCHAR(255), points VARCHAR(5));");
        db.execSQL("CREATE table history (chore VARCHAR(255), points VARCHAR(5), date VARCHAR(20));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

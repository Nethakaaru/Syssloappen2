package se.mah.ad0206.syssloappen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sebastian Aspegren, Jonas Dahlström on 2015-03-05.
 * A class acting as a database that stores chores.
 *
 */
public class DBController extends SQLiteOpenHelper {

    private static final String DB_NAME = "choreDB";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase database;

    /**
     * Default constructor.
     * @param context
     *              the activity.
     */
    public DBController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    /**
     * Prepare the database for action.
     */
    public void open(){
        database = getWritableDatabase();
    }

    /**
     * Close the database to save CPU.
     */
    public void close(){
        database.close();
    }

    /**
     * A method to get all the chores.
     * @return
     *          A Cursor with all the active chores.
     */
    public Cursor getChores(){
        return database.rawQuery("SELECT chore, points from chores", new String[]{});
    }

    /**
     * A method to save chores to the database.
     * @param chore
     *              the chore we want to store.
     * @param points
     *              the points that chore rewards.
     */
    public void save(String chore, String points){
        ContentValues values = new ContentValues();
        values.put("chore", chore);
        values.put("points", points);

        //insert into table chores
        database.insert("chores", null, values);
    }

    /**
     * A method to save what chores the user has completed to the history table.
     * @param chore
     *              the completed chore.
     * @param points
     *              the points that chore rewards.
     * @param date
     *              the date it was completed upon.
     */
    public void saveHistory(String chore, String points, String date){
        ContentValues values = new ContentValues();
        values.put("chore",chore);
        values.put("points",points);
        values.put("date",date);

        database.insert("history", null, values);
    }
    public void saveUser(String user){
        ContentValues values = new ContentValues();
        values.put("user",user);
        database.insert("users", null, values);
    }

    /**
     * A method to get the completed chores.
     * @return
     *          A cursor with all completed chores.
     */
    public Cursor getHistory(){
        return database.rawQuery("SELECT chore, points, date from history", new String[]{});
    }

    public Cursor getUsers(){
        return database.rawQuery("SELECT user from users", new String[]{});
    }

    /**
     * A method used to an active chore from the database.
     * @param chore
     *              the chore we want to delete.
     * @param points
     *              the points that chore rewards.
     */
    public void deleteChore(String chore, String points) {
        database.delete("chores", "chore='"+chore+"' and points='"+points+"'", null);
    }

    /**
     * The method that creates the tables. only used once.
     * @param db
     *          The database we want the tables to be in.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE table chores ( chore VARCHAR(255), points VARCHAR(5));");
        db.execSQL("CREATE table history (chore VARCHAR(255), points VARCHAR(5), date VARCHAR(20));");
        db.execSQL("CREATE table users ( user VARCHAR(100));");
    }


    /**
     * Unused inherited method.
     * @param db
     *           the database.
     * @param oldVersion
     *                  old version number.
     * @param newVersion
     *                  new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clearHistory() {
        database.execSQL("delete from history");
    }
}

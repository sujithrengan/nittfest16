package org.delta.nittfest;

/**
 * Created by HP on 05-02-2016.
 */



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController  extends SQLiteOpenHelper {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CLUSTER = "cluster";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CREDITS = "credits";

    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE scores ( departmentName TEXT, score DECIMAL(10,2) )";
        database.execSQL(query);
        query = "CREATE TABLE notifs ( notifText TEXT,time TEXT,title TEXT)";
        database.execSQL(query);
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + "events" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CLUSTER + " TEXT"
                + KEY_STATUS + " INTEGER"
                + KEY_CREDITS + " INTEGER" + ")";
        database.execSQL(CREATE_EVENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS notifs";
        database.execSQL(query);
        onCreate(database);
        database.execSQL("DROP TABLE IF EXISTS events");
        onCreate(database);
    }


    /**
     * Inserts User into SQLite DB
     * @param
     */
    public void insertDepartment(Department[] department) {
        SQLiteDatabase database = this.getWritableDatabase();
        for(int i=0;i<department.length;i++) {
            ContentValues values = new ContentValues();
            values.put("departmentName", department[i].name);
            values.put("score", department[i].score);
            database.insert("scores", null, values);
        }
        database.close();
        Log.e("DB", "inserted");
    }

    public void insertNotif(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("notifText", queryValues.get("notifText"));
        values.put("time", queryValues.get("time"));
        values.put("title", queryValues.get("title"));

        //values.put("index", queryValues.get("index"));
        database.insert("notifs", null, values);
        database.close();
    }

    public void deleteNotif(String notiftxt) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //database.insert("notifs", null, values);
        database.delete("notifs", "notifText" + "= '" + notiftxt + "'", null);
        database.close();
    }


    public ArrayList<HashMap<String, String>> getAllNotifs() {
        ArrayList<HashMap<String, String>> notifsList;
        notifsList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM notifs";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("notifText", cursor.getString(0));
                map.put("time", cursor.getString(1));
                map.put("title", cursor.getString(2));

                notifsList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return notifsList;
    }

    public void updateScores(Department[] department) {
        SQLiteDatabase database = this.getWritableDatabase();
        for(int i=0;i<department.length;i++) {
            String dn = department[i].name;
            ContentValues values = new ContentValues();
            String where = "departmentName" + "= '" + dn + "'";
            values.put("departmentName", department[i].name);
            values.put("score", department[i].score);
            database.update("scores", values, where, null);
        }
        Log.e("DB", "updated");
        database.close();
    }
    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllScores2() {
        ArrayList<HashMap<String, String>> scoresList;
        scoresList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM scores";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("departmentName", cursor.getString(0));
                map.put("score", cursor.getString(1));

                scoresList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return scoresList;
    }


    public Department[] getAllScores() {

        Department departments[]=new Department[12];

        String selectQuery = "SELECT  * FROM scores";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int i=0;
            do {
                departments[i]=new Department(cursor.getString(0),cursor.getFloat(1));
                i++;
            } while (cursor.moveToNext());
        }
        database.close();
        return departments;
    }
    public void addEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,events.get_id());
        values.put(KEY_NAME, events.get_name());
        values.put(KEY_CLUSTER, events.get_cluster());
        values.put(KEY_STATUS,events.get_status());
        values.put(KEY_CREDITS, events.get_credits());
        // Inserting Row
        db.insert("events", null, values);
        db.close(); // Closing database connection
    }
    // Getting All Events
    public List<Events> getAllEvents() {
        List<Events> eventsList = new ArrayList<Events>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "events";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Events event = new Events();
                event.set_id(Integer.parseInt(cursor.getString(0)));
                event.set_name(cursor.getString(1));
                event.set_cluster(cursor.getString(2));
                event.set_status(Integer.parseInt(cursor.getString(3)));
                event.set_credits(Integer.parseInt(cursor.getString(4)));

                eventsList.add(event);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventsList;
    }
    // Updating single Event
    public int updateEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,events.get_id());
        values.put(KEY_NAME, events.get_name());
        values.put(KEY_CLUSTER, events.get_cluster());
        values.put(KEY_STATUS,events.get_status());
        values.put(KEY_CREDITS, events.get_credits());

        // updating row
        return db.update("events", values, KEY_ID + " = ?",
                new String[] { String.valueOf(events.get_id()) });
    }

    /*public ArrayList<HashMap<String, String>> getAllNotifs() {
        ArrayList<HashMap<String, String>> notifsList;
        notifsList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM notifs";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("notifText", cursor.getString(0));
                map.put("time", cursor.getString(1));
                //map.put("index", cursor.getString(1));

                notifsList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return notifsList;
    }
    */

}

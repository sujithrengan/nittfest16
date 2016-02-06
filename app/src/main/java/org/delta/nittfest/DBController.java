package org.delta.nittfest;

/**
 * Created by HP on 05-02-2016.
 */


        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE scores ( departmentName TEXT, score DECIMAL(10,2) )";
        database.execSQL(query);
        query = "CREATE TABLE notifs ( notifText TEXT,time TEXT )";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS notifs";
        database.execSQL(query);
        onCreate(database);
    }


    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertDepartment(Department department) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("departmentName", department.name);
        values.put("score",department.score);
        database.insert("scores", null, values);
        database.close();
        Log.e("DB","inserted");
    }

    public void insertNotif(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("notifText", queryValues.get("notifText"));
        values.put("time", queryValues.get("time"));
        //values.put("index", queryValues.get("index"));
        database.insert("notifs", null, values);
        database.close();
    }

    public void deleteNotif(String notiftxt) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //database.insert("notifs", null, values);
        database.delete("notifs", "notifText"+"= '"+notiftxt+"'",null);
        database.close();
    }

    public void updateScores(Department department) {
        SQLiteDatabase database = this.getWritableDatabase();
        String dn=department.name;
        ContentValues values = new ContentValues();
        String where = "departmentName" + "= '" + dn+"'";
        values.put("departmentName", department.name);
        values.put("score", department.score);
        database.update("scores", values, where,null);
        Log.e("DB","updated");
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
                //map.put("index", cursor.getString(1));

                notifsList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return notifsList;
    }

}

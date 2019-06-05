package com.example.caffinetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.caffinetracker.model.FoodItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodDB extends SQLiteOpenHelper {

    Context ctx;
    SQLiteDatabase db;
    private static String DB_PATH = "/data/data/com.example.caffinetracker/databases/";
    static String DB_NAME = "FOOD_DB";
    static String TABLE_NAME = "FOOD_TABLE";
    static int VERSION = 1;
    //APICalls please = new APICalls();



    public FoodDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        DB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE  TABLE_NAME (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);");

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    /*(public boolean createDataBase() {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
            return dbExist;
        }
        return dbExist;

    }*/

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(VERSION == oldVersion){
            VERSION = newVersion;
            db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            onCreate(db);
        }
    }

    public void addEntry(String ndb, String name, String measure, String unit, String value) throws SQLiteException {
        db = getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put("NDB",    ndb);
        cv.put("NAME",    name);
        cv.put("MEASURE",    measure);
        cv.put("UNIT",    unit);
        cv.put("VALUE",   value);
        db.insert(TABLE_NAME, null, cv );
    }

    public Cursor foodQuery( String filter) {
        SQLiteDatabase db = getReadableDatabase();


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                "NDB",
                "NAME",
                "MEASURE",
                "UNIT",
                "VALUE"
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                "NAME" + " LIKE ?",
                new String[]{"%" + filter + "%"},
                null, null, "NAME", null);
        return cursor;
    }

    public List<FoodItem> fillList(List<FoodItem> foodItems, String filter) {
        Cursor cursor = foodQuery(filter);

        if(cursor != null & cursor.getCount() > 0) {
            cursor.moveToFirst();
            int index;
            String ndb;
            String name;
            String measure;
            String unit;
            String value;

            do{
                index = cursor.getColumnIndex("NDB");
                ndb = cursor.getString(index);
                index = cursor.getColumnIndex("NAME");
                name = cursor.getString(index);
                index = cursor.getColumnIndex("MEASURE");
                measure = cursor.getString(index);
                index = cursor.getColumnIndex("UNIT");
                unit = cursor.getString(index);
                index = cursor.getColumnIndex("VALUE");
                value = cursor.getString(index);
                FoodItem fi = new FoodItem( ndb, name, measure, unit, value);
                foodItems.add(fi);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return foodItems;
    }

    public void firstEntry(){
        this.db = getWritableDatabase();
        Cursor c = db.rawQuery("Select * FROM " + TABLE_NAME + ";", null);
        if(c.getCount() == 0){
            MainActivity.testMethod();
        }
    }

}

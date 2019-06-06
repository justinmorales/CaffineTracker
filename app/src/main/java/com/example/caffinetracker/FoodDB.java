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
    //private static String DB_PATH = "/data/data/com.example.caffinetracker/databases/";
    static String FDB_NAME = "FOOD_DB";
    static String FTABLE_NAME = "FOOD_TABLE";
    static int VERSION = 1;
    //APICalls please = new APICalls();



    public FoodDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        FDB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE  TABLE_NAME (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);
        db.execSQL("CREATE TABLE " + FTABLE_NAME + " (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);");

    }
    public int count() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("Select * FROM " + FTABLE_NAME + ";", null);
        return c.getCount();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(VERSION == oldVersion){
            VERSION = newVersion;
            db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + FTABLE_NAME + ";");
            onCreate(db);
        }
    }

    public void addEntry(String name, String measure, String value) throws SQLiteException {
        db = getWritableDatabase();
        long count = count();
        if (count < 876) {
            ContentValues cv = new ContentValues();
            cv.put("NAME", name);
            cv.put("MEASURE", measure);
            cv.put("VALUE", value);
            db.insert(FTABLE_NAME, null, cv);
        }
    }


    public Cursor foodQuery( String filter) {
        SQLiteDatabase db = getReadableDatabase();


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                "NAME",
                "MEASURE",
                "VALUE"
        };

        Cursor cursor = db.query(
                FTABLE_NAME,
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
            String name;
            String measure;
            String value;

            do{
                index = cursor.getColumnIndex("NAME");
                name = cursor.getString(index);
                index = cursor.getColumnIndex("MEASURE");
                measure = cursor.getString(index);
                index = cursor.getColumnIndex("VALUE");
                value = cursor.getString(index);
                FoodItem fi = new FoodItem(name, measure, value);
                foodItems.add(fi);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return foodItems;
    }

}
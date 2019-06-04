package com.example.caffinetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FoodDB extends SQLiteOpenHelper {

    Context ctx;
    SQLiteDatabase db;
    static String DB_NAME = "FOOD_DB";
    static String TABLE_NAME = "FOOD_TABLE";
    static int VERSION = 1;

    public FoodDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        DB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE  TABLE_NAME (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);
        db.execSQL("CREATE TABLE "+ TABLE_NAME + " (_id INTEGER PRIMARY KEY, NDB STRING, NAME STRING, MEASURE STRING, UNIT STRING, VALUE STRING);");

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

    public void addEntry( String ndb, String name, String measure, String unit, String value) throws SQLiteException {
        db = getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put("NDB",    ndb);
        cv.put("NAME",    name);
        cv.put("MEASURE",    measure);
        cv.put("UNIT",    unit);
        cv.put("VALUE",   value);
        db.insert(TABLE_NAME, null, cv );
    }

    public  Cursor foodQuery(String filter){
        SQLiteDatabase db = getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                //"NDB",
                "NAME",
                "MEASURE",
                "UNIT",
                "VALUE"
        };

// Filter results WHERE "title" = 'My Title'
        /*String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );*/

        Cursor cursor = db.query(true,
                TABLE_NAME,
                projection,
                "NAME" + " LIKE ?",
                new String[] {"%"+ filter+ "%" }, null, null, null,
                null);

        return cursor;

    }

}

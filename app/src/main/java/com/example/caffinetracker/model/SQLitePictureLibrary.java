package com.example.caffinetracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLitePictureLibrary extends SQLiteOpenHelper {
    Context ctx;
    SQLiteDatabase db;
    static public String DB_NAME = "IMAGE_DB";
    static String TABLE_NAME = "IMAGE_TABLE";
    static int VERSION = 1;
    public SQLitePictureLibrary(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
    }

    @Override
    public void onCreate(SQLiteDatabase da) {
        //"CREATE TABLE" + TABLE_NAME (_id INTEGER PRIMARY KEY, HYPERLINK_NAME STRING, PICTURE_TITLE  STRING);
        da.execSQL("CREATE TABLE " + TABLE_NAME+ " (_id INTEGER PRIMARY KEY, DATE_RECORDED STRING, CAFFEINE  INTEGER);");
        //Toast.makeText(ctx,"TABLE IS CREATED", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase da, int oldVersion, int newVersion) {
        if(VERSION == oldVersion){
            VERSION = newVersion;
            da.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+ ";");
            onCreate(da);
        }

    }
    //update table_name  SET CAFFEIne where string EQUALS DATE_SELECTED

    public void insert(Integer amount) throws SQLException {
        this.db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        long count = -1;
        try {
            count = DatabaseUtils.queryNumEntries(db, TABLE_NAME, "DATE_RECORDED = ?", new String[]{date});
        }
        finally {
            cv.put("CAFFEINE", amount);
            cv.put("DATE_RECORDED", date);
            if (count > 0)
                db.update(TABLE_NAME, cv, "DATE_RECORDED = ?", new String[]{date});
            else
                this.db.insert(TABLE_NAME, null, cv);
        }
    }
    public void view(){
        this.db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DATE_RECORDED FROM "+TABLE_NAME+";",null);
        StringBuilder sb = new StringBuilder();
        while(((Cursor) c).moveToNext()){
            sb.append(c.getString(0)+"\n");
        }
        Toast.makeText(ctx,sb.toString(),Toast.LENGTH_LONG).show();
    }
    public int count(){
        db = getReadableDatabase();
        Cursor c = db.rawQuery("Select * FROM " + TABLE_NAME + ";", null);
        return c.getCount();
    }

    //public byte[] fetchImage(int i) {
     //   Cursor cursor = db.rawQuery("SELECT PICTURE_SAVED FROM " + TABLE_NAME +";",null);
     //   cursor.moveToPosition(i);
     //   byte[] biter = cursor.getBlob(0);

     //   return biter;
    //}
    public int fetchInt(String myString)
    {
        //SELECT CAFFEINE FROM IMAGE_TABLE WHERE DATE_RECORDED EQUALS mystring
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT CAFFEINE FROM " + TABLE_NAME + " WHERE DATE_RECORDED EQUALS " + myString +";",null);
        return c.getInt(0);
    }

    public String fetchString(int i) {
        Cursor cursor = db.rawQuery("SELECT DATE_RECORDED FROM "+TABLE_NAME+";",null);
        StringBuffer sb = new StringBuffer();
        cursor.moveToPosition(i);
        sb.append(cursor.getString(0));
        return sb.toString();
    }

    public void removeKey(int id) {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = \"" + id + "\";");
    }

    public void removeName(String string) {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE DATE_RECORDED = \"" + string + "\";" );
    }
}

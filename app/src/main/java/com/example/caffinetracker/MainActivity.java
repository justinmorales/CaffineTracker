package com.example.caffinetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.caffinetracker.model.FoodItem;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.caffinetracker.SettingsActivity.mSharedPrefs;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<FoodItem> consumed;
    static public int totalCaffeine;
    static public FoodDB fdb;

    /*public static SharedPreferences mPreferences;
    public static SharedPreferences.Editor mEditor;

    private
    private String sharedPrefFile = "com.example.android.caffinetracker_DB";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consumed = new ArrayList<>();
        totalCaffeine = 0;
        TextView textView = findViewById(R.id.totalCaffeineText);
        textView.setText(Integer.toString(totalCaffeine) + " mg");

        fdb = new FoodDB(this,FoodDB.DB_NAME,null,1);
        fdb.firstEntry();
        //fdb.getWritableDatabase();
    }

    public void AddButton(View view)
    {
        Intent switcher = new Intent(this,APIMainActivity.class);
        startActivity(switcher);
    }
    public void StatsButton(View view)
    {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        TextView textView = findViewById(R.id.totalCaffeineText);
        textView.setText(Integer.toString(totalCaffeine)+ " mg");
    }

}

package com.example.caffinetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.caffinetracker.model.FoodItem;
import com.example.caffinetracker.model.SQLitePictureLibrary;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.caffinetracker.SettingsActivity.mSharedPrefs;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<FoodItem> consumed;
    static public int totalCaffeine;
    static  public SQLitePictureLibrary db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLitePictureLibrary(this,SQLitePictureLibrary.DB_NAME,null,1);
        consumed = new ArrayList<>();
        totalCaffeine = 0;
        TextView textView = findViewById(R.id.totalCaffeineText);
        textView.setText(Integer.toString(totalCaffeine) + " mg");
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
        db.insert(totalCaffeine);
        db.view();
    }

}

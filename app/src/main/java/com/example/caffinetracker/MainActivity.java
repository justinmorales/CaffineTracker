package com.example.caffinetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.caffinetracker.model.FoodItem;
import com.example.caffinetracker.model.SQLitePictureLibrary;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.caffinetracker.SettingsActivity.mSharedPrefs;

public class MainActivity extends AppCompatActivity {

    DonutProgress donutG, donutY, donutO, donutR;

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

        db.insertFakeValues(100, "2019-05-28");
        db.insertFakeValues(110, "2019-05-29");
        db.insertFakeValues(120, "2019-05-30");
        db.insertFakeValues(130, "2019-05-31");
        db.insertFakeValues(140, "2019-06-01");
        db.insertFakeValues(150, "2019-06-02");
        db.insertFakeValues(160, "2019-06-03");
        db.insertFakeValues(170, "2019-06-04");

    }

    public void AddButton(View view)
    {
        Intent switcher = new Intent(this,APIMainActivity.class);
        startActivity(switcher);
    }
    public void StatsButton(View view)
    {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
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
        db.insert(totalCaffeine);
        TextView textView = findViewById(R.id.totalCaffeineText);
        textView.setText(Integer.toString(totalCaffeine) + " mg");
        db.view();

        SetUpDonut();
    }

    private void SetUpDonut()
    {
        donutG = findViewById(R.id.donut_progress_green);
        donutY = findViewById(R.id.donut_progress_yellow);
        donutO = findViewById(R.id.donut_progress_orange);
        donutR = findViewById(R.id.donut_progress_red);
        donutG.setVisibility(View.INVISIBLE);
        donutY.setVisibility(View.INVISIBLE);
        donutO.setVisibility(View.INVISIBLE);
        donutR.setVisibility(View.INVISIBLE);
        float maxCaf = getMaxCaf();
        float currCaf = getCurrCaf();
        if(currCaf < maxCaf)
        {
            if(currCaf / maxCaf < .5)
            {
                donutG.setMax((int)maxCaf);
                donutG.setProgress(currCaf);
                donutG.setText((int)currCaf + "/" + (int)maxCaf);
                donutG.setVisibility(View.VISIBLE);
            }
            else if(currCaf / maxCaf < .75)
            {
                donutY.setMax((int)maxCaf);
                donutY.setProgress(currCaf);
                donutY.setText((int)currCaf + "/" + (int)maxCaf);
                donutY.setVisibility(View.VISIBLE);
            }
            else
            {
                donutO.setMax((int)maxCaf);
                donutO.setProgress(currCaf);
                donutO.setText((int)currCaf + "/" + (int)maxCaf);
                donutO.setVisibility(View.VISIBLE);
            }
        }
        else if(currCaf < maxCaf * 2)
        {
            donutR.setMax((int)maxCaf * 2);
            donutR.setProgress(currCaf);
            donutR.setText((int)currCaf + "/" + (int)maxCaf);
            donutR.setVisibility(View.VISIBLE);
        }
    }
    private float getMaxCaf()
    {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String weight = shared.getString("userWeight", "150");
        float age = Float.parseFloat(weight);

        return (float)(age * 2.7);
    }
    private float getCurrCaf()
    {
        return totalCaffeine;
    }
}

package com.example.caffinetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AddButton(View view)
    {
    }
    public void StatsButton(View view)
    {

    }
    public void SettingsButton(View view)
    {
        Intent switcher = new Intent(this,Settings.class);
        startActivity(switcher);
    }


}

package com.example.caffinetracker;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.caffinetracker.model.SQLitePictureLibrary;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {

    GraphView graph;

    static  public SQLitePictureLibrary db;

    private int getYear(String date)
    {
        String year = date.substring(0,4);
        return Integer.parseInt(year);
    }
    private int getMonth(String date)
    {
        String month = date.substring(5,7);
        return Integer.parseInt(month);
    }
    private int getDay(String date)
    {
        String day = date.substring(8,10);
        return Integer.parseInt(day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = new SQLitePictureLibrary(this,SQLitePictureLibrary.DB_NAME,null,1);

        graph = findViewById(R.id.graph);

        ArrayList<String> newDates = new ArrayList<>();
        ArrayList<Integer> newValues = new ArrayList<>();

        int bla = db.count();
        DataPoint[] allData = new DataPoint[bla];

        Date d1 = new Date(2019, 01, 01);
        Date d2 = new Date(2019, 02, 02);
        for(int i = 0; i < db.count(); i++)
        {
            String date = db.fetchString(i);
            int year = getYear(date);
            int month = getMonth(date);
            int day = getDay(date);
            newDates.add(date);
            int value = db.fetchInt(date);
            newValues.add(value);

            Date d = new Date(year, month, day);
            if(i == 0)
            {
                d1 = d;
            }
            if(i == db.count() - 1)
            {
                d2 = d;
            }
            DataPoint data = new DataPoint(d, value);
            allData[i] = data;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(allData);

        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(StatisticsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d2.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
    }
}

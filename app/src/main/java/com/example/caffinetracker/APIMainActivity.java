package com.example.caffinetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.caffinetracker.model.FoodItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.example.caffinetracker.MainActivity.consumed;
import static com.example.caffinetracker.MainActivity.totalCaffeine;

public class APIMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private MyAdapter m;

    private RecyclerView.LayoutManager layoutManager;

    //private static final int SINGLE_ITEM_REQUEST = 100;

    //public static final String RETURN_QUANTITY = "RETURN_QUANTITY";

    // out arguments (return)
    //public static final String RETURN_FOOD_NAME = "RETURN_FOOD_NAME";
    //public static final String RETURN_FOOD_CATEGORY = "RETURN_FOOD_CATEGORY";
    //public static final String RETURN_FOOD_NDB = "RETURN_FOOD_NDB";
    //public static final String RETURN_FOOD_MEASURE = "RETURN_FOOD_MEASURE";
    //public static final String RETURN_FOOD_QUANTITY = "RETURN_FOOD_QUANTITY";

    private final String TAG_SEARCH_MEASURE = "USDAQuery-SearchMeasure";
    private final String TAG_SEARCH_NAME = "USDAQuery-SearchName";

    private List<FoodItem> foodItems,TotalfoodItems;
    private CSVReader csvReader;
    private RequestQueue requestQueue;
    private int chosenItem;
    private List myEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apimain);

        foodItems = new ArrayList<>();
        TotalfoodItems = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.caffeineinformer);
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            String[] nextLine;
            
            csvReader.readNext(); //gets rid of the first line

            while ((nextLine = csvReader.readNext()) != null) {
                FoodItem fi = new FoodItem(nextLine[0], nextLine[1], nextLine[2]);
                foodItems.add(fi);
            }
//            myEntries = csvReader.readAll();
//            int size = myEntries.size();
//            for (int i = 1; i < size; i++)
//            {
//                FoodItem food = new FoodItem();
//                food.setItemName(myEntries[i].get(0));
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);

        //JsonObjectRequest objectRequest = new JsonObjectRequest()

    }



    public void visualize(View view) {
        requestQueue.cancelAll(TAG_SEARCH_NAME);
        TextView textView = findViewById(R.id.SearchingText);

        changeView();


    }
    public void changeView() {
        setContentView(R.layout.activity_scroll_down);
        recyclerView = (RecyclerView) findViewById((R.id.rvContacts));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter myAdapter = new MyAdapter(foodItems);
        recyclerView.setAdapter(myAdapter);

    }


    public void addToList(View view) {
        TextView nameText = findViewById(R.id.textFoodTItle);
        String name = nameText.getText().toString();
        FoodItem caffeine = new FoodItem();
        for(int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getItemName() == name) {
                caffeine = foodItems.get(i);
                break;
            }
        }
        if (consumed.size() == 0) {
            consumed.add(caffeine);
            int serving = Integer.parseInt(caffeine.getItemValue());
            totalCaffeine += serving;
        }
        else{
            for (int i = 0; i < consumed.size(); i++)
            {
                if (i == consumed.size() - 1 && caffeine != consumed.get(i))
                {
                    consumed.add(caffeine);
                    int serving = Integer.parseInt(caffeine.getItemValue());
                    totalCaffeine += serving;
                }
                else if(consumed.get(i) == caffeine)
                {
                    int serving =Integer.parseInt( caffeine.getItemValue());
                    serving += serving;
                    totalCaffeine += serving;
                    consumed.get(i).setItemValue(Integer.toString(serving));
                    break;
                }
            }
        }
        finish();
    }

}

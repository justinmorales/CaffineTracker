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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.caffinetracker.model.FoodItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

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

    private List<FoodItem> foodItems;
    private RequestQueue requestQueue;
    private int chosenItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apimain);

        foodItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //JsonObjectRequest objectRequest = new JsonObjectRequest()

    }

    private StringRequest searchNameStringRequest(String nameSearch) {

        //DOCUMENTATION FOR THIS STRING REQUEST CAN BE FOUND AT https://ndb.nal.usda.gov/ndb/doc/apilist/API-NUTRIENT-REPORT.md

        //key
        final String API = "&api_key=spkVUDxuyvhsVBJTwpF53KaGNaytyLFPQRVWZePq";
        //not used in nutrient search
        //final String NAME_SEARCH = "&q=";
        //not used in nutrient search
        //final String DATA_SOURCE = "&ds=Standard Reference";
        //not in use
        //final String FOOD_GROUP = "&fg=";
        final String NDBNO = "&ndbno=";
        //gives measure by serving size
        final String MEASUREBY = "&measureby=m";
        //nutrient content caffeine
        final String NUTRIENT = "&nutrients=262";
        //sort by nutrition content
        final String SORT = "&sort=c";
        //self explanatory, max total items is 1500
        final String MAX_ROWS = "&max=25";
        //self explanatory
        final String BEGINNING_ROW = "&offset=0";
        //nutrient search in json format
        final String URL_PREFIX = "https://api.nal.usda.gov/ndb/nutrients/?format=json";

        String url = URL_PREFIX + API + NUTRIENT + SORT + MAX_ROWS + BEGINNING_ROW + MEASUREBY + NDBNO + nameSearch ;

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response).getJSONObject("report");
                            int maxItems = result.getInt("end");
                            JSONArray resultFood = result.getJSONArray("foods");

                            for (int i = 0; i < maxItems; i++) {
                                JSONObject resultNutrients = resultFood.getJSONObject(i);
                                JSONArray nutrientsArray = resultNutrients.getJSONArray("nutrients");
                                FoodItem fi = new FoodItem(
                                        resultFood.getJSONObject(i).getString("ndbno").trim(),
                                        resultFood.getJSONObject(i).getString("name").trim(),
                                        resultFood.getJSONObject(i).getString("measure").trim(),
                                        nutrientsArray.getJSONObject(0).getString("unit").trim(),
                                        nutrientsArray.getJSONObject(0).getString("value").trim()
                                );
                                foodItems.add(fi);
                            }

                            //This call show call a function to an adapter. Create that function in this class.
                            //showListOfItems();

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(APIMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(APIMainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void visualize(View view) {
        //recyclerView = (RecyclerView) findViewById((R.id.rvContacts));
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        MyAdapter myAdapter = new MyAdapter(foodItems);
        requestQueue.cancelAll(TAG_SEARCH_NAME);
        TextView textView = findViewById(R.id.SearchingText);
        StringRequest stringRequest = searchNameStringRequest(textView.getText().toString());
        stringRequest.setTag(TAG_SEARCH_NAME);
        requestQueue.add(stringRequest);
    }
}

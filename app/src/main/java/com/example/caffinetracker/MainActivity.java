package com.example.caffinetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caffinetracker.model.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.caffinetracker.SettingsActivity.mSharedPrefs;

public class MainActivity extends AppCompatActivity {

    //-------------------------------------------------------------------------

    // API SHIT

    //-------------------------------------------------------------------------

    static ConnectivityManager connectivityManager;
    static NetworkInfo networkInfo;

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

    private RequestQueue requestQueue;

    //----------------------------------------------------------------------

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

        requestQueue = Volley.newRequestQueue(this);

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
        final String MAX_ROWS = "&max=20";
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

                                fdb.addEntry(
                                        resultFood.getJSONObject(i).getString("ndbno").trim(),
                                        resultFood.getJSONObject(i).getString("name").trim(),
                                        resultFood.getJSONObject(i).getString("measure").trim(),
                                        nutrientsArray.getJSONObject(0).getString("unit").trim(),
                                        nutrientsArray.getJSONObject(0).getString("value").trim()
                                );
                            }

                            //This call show call a function to an adapter. Create that function in this class.
                            //showListOfItems();

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void callAPI() {
        requestQueue.cancelAll(TAG_SEARCH_NAME);
        //TextView textView = findViewById(R.id.SearchingText);
        //StringRequest stringRequest = searchNameStringRequest(textView.getText().toString());
        StringRequest stringRequest = searchNameStringRequest("");
        stringRequest.setTag(TAG_SEARCH_NAME);
        requestQueue.add(stringRequest);
    }


}

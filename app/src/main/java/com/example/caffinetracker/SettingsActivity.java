package com.example.caffinetracker;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    static public SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


    /*private List<Subject> loadSubjects() {
        String order = mSharedPrefs.getString(PREFERENCE_SUBJECT_ORDER, "1");
        switch (Integer.parseInt(order)) {
            case 0: return mStudyDb.getSubjects(ALPHABETIC);
            case 1: return mStudyDb.getSubjects(UPDATE_DESC);
            default: return mStudyDb.getSubjects(UPDATE_ASC);
        }
    }*/
}
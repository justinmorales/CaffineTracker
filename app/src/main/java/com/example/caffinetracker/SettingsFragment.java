package com.example.caffinetracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{

    public static String PREFERENCE_NAME = "userName";
    public static String PREFERENCE_AGE = "userAge";
    public static String PREFERENCE_HEIGHT = "userHeight";
    public static String PREFERENCE_WEIGHT = "userWeight";
    public static String PREFERENCE_SEX = "userSex";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Access the default shared prefs
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setPrefSummaryName(sharedPrefs);
        setPrefSummaryAge(sharedPrefs);
        setPrefSummaryHeight(sharedPrefs);
        setPrefSummaryWeight(sharedPrefs);
        setPrefSummarySex(sharedPrefs);
    }


    private void setPrefSummaryName(SharedPreferences sharedPrefs)
    {
        String name = sharedPrefs.getString(PREFERENCE_NAME, "NO NAME ENTERED");
        Preference namePref = findPreference(PREFERENCE_NAME);
        namePref.setSummary(name);
    }
    private void setPrefSummaryAge(SharedPreferences sharedPrefs)
    {
        String age = sharedPrefs.getString(PREFERENCE_AGE, "0");
        Preference agePref = findPreference(PREFERENCE_AGE);
        agePref.setSummary(age);
    }
    private void setPrefSummaryHeight(SharedPreferences sharedPrefs)
    {
        String height = sharedPrefs.getString(PREFERENCE_HEIGHT, "0");
        Preference heightPref = findPreference(PREFERENCE_HEIGHT);
        heightPref.setSummary(height);
    }
    private void setPrefSummaryWeight(SharedPreferences sharedPrefs)
    {
        String weight = sharedPrefs.getString(PREFERENCE_WEIGHT, "0");
        Preference weightPref = findPreference(PREFERENCE_WEIGHT);
        weightPref.setSummary(weight);
    }
    private void setPrefSummarySex(SharedPreferences sharedPrefs)
    {
        String sex = sharedPrefs.getString(PREFERENCE_SEX, "1");
        String[] sexList = getResources().getStringArray(R.array.pref_sex);
        Preference sexPref = findPreference(PREFERENCE_SEX);
        sexPref.setSummary(sexList[Integer.parseInt(sex)]);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREFERENCE_NAME)) {
            setPrefSummaryName(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_AGE)) {
            setPrefSummaryAge(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_HEIGHT)) {
            setPrefSummaryHeight(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_WEIGHT)) {
            setPrefSummaryWeight(sharedPreferences);
        }
        else if (key.equals(PREFERENCE_SEX)) {
            setPrefSummarySex(sharedPreferences);
        }
    }



}
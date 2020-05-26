package com.example.see_u_festival.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.see_u_festival.Activity.SettingsActivity;
import com.example.see_u_festival.R;
import com.example.see_u_festival.Utils.LocaleHelper;

public class PreferenceFragment extends SettingsActivity.SettingsFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_LANGUAGE = "key_lang";

    public PreferenceFragment() {
        // Required
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        return view;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_PREF_LANGUAGE:
                LocaleHelper.setLocale(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, ""));
                getActivity().recreate();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
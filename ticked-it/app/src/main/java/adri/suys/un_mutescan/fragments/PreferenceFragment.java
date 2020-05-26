package adri.suys.un_mutescan.fragments;

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
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.activities.SettingsActivity;
import adri.suys.un_mutescan.utils.LocaleHelper;

public class PreferenceFragment extends SettingsActivity.SettingsFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_LANGUAGE = "key_lang";
    public static final String KEY_DARK_MODE = "switch_dark_mode";

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


    /** Detect the language selected in the settings activity
     * @param key id of the item selected
     * **/
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
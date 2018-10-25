package com.brix.store;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    /**
     * Checks which theme is preferred and sets it
     * gets param from SettingsActivity.class
     * run before setContentView
     */
    public void currentTheme() {
        //Preferences for Theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean value = (sharedPreferences.getBoolean("themekey", true));

        if (value) {
            //Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_AppBarOverlay); // set theme

        } else {
            //Toast.makeText(this, "dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_Dark); // set theme
        }
    }
}

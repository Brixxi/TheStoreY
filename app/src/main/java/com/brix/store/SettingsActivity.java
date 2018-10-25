package com.brix.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentTheme();
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    private static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"brix@protonmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

    /**
     * Checks which theme is preferred and sets it
     * gets param from MainPrefFragment
     * run before setContentView
     */
    public void currentTheme() {
        //Preferences for Theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean value = (sharedPreferences.getBoolean("themekey", true));

        if (value) {
            //Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme); // set theme

        } else {
            //Toast.makeText(this, "dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_Dark); // set theme
        }
    }




    // PreferenceFragment
    public static class MainPreferenceFragment extends PreferenceFragment {

        private Activity mActivity;
        private Context mContext;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            // DefaultOnCreate
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });
            // init the theme preference
            changeTheme();
        }


        /**
         * Preference Theme
         * sets the theme and shares the switchPreference for ex. the MainActivity
         * -> user needs to restart the app for the applied changes to take effect
         */
        private void changeTheme() {
            // Get the application context
            mActivity = this.getActivity();
            mContext = this.getActivity();

            // Get the preference widgets reference
            final SwitchPreference theme = (SwitchPreference) findPreference(this.getResources()
                    .getString(R.string.switch_theme));

            // SwitchPreference preference change listener
            theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(theme.isChecked()){
                        //Test Toast
                        Toast.makeText(mActivity,
                                "Please restart the app for the applied changes to take effect.",
                                Toast.LENGTH_SHORT).show();
                        // setDefault for other Activities
                        setDefaultsBoolean("themekey",theme.isChecked(), mContext);
                        // Checked the switch programmatically
                        theme.setChecked(false);
                    }else {
                        //Test Toast
                        Toast.makeText(mActivity,
                                "Please restart the app for the applied changes to take effect.",
                                Toast.LENGTH_SHORT).show();
                        // setDefault for other Activities
                        setDefaultsBoolean("themekey",theme.isChecked(), mContext);
                        // Unchecked the switch programmatically
                        theme.setChecked(true);
                    }
                    return false;
                }
            });
        }

        /**
         * setDefaultsBoolean
         * initialize the shared preference editor
         * Sets the default preferences (boolean) for ex. the MainActivity
         *
         * @param key       name of the to set preference
         * @param value     value of the preference
         * @param context   context of the activity
         */
        private static void setDefaultsBoolean(String key, Boolean value, Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }

    }

}
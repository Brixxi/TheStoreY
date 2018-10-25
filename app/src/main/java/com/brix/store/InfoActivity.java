package com.brix.store;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void bounce(View view) {
        final ImageView img = findViewById(R.id.imageView);
        SpringAnimation springAnimation = new SpringAnimation(img, DynamicAnimation.X);
        SpringAnimation springAnimation2 = new SpringAnimation(img, DynamicAnimation.ROTATION);

        //spring behaviour -> set spring force
        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition(0);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springForce.setStiffness(SpringForce.STIFFNESS_MEDIUM);

        //associate the spring
        springAnimation.setSpring(springForce);
        springAnimation2.setSpring(springForce);

        //initial velocity
        double d = Math.random() * ((15000 - 10000) +1);
        float f = (float)d;
        springAnimation.setStartVelocity(6000);
        springAnimation.start();
        springAnimation2.setStartVelocity(f);
        springAnimation2.start();
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
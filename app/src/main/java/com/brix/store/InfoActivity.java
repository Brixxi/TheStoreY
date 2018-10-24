package com.brix.store;

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


}
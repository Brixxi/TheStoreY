package com.brix.store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog mProgressDialog;
    private String url = "https://www.hs-offenburg.de/";
    private ArrayList<String> mNewsHeadlineList = new ArrayList<>();
    private ArrayList<Bitmap> mNewsImageContainerList = new ArrayList<>();
    private ArrayList<String> mNewsDescriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set the current preferred theme on start up
        currentTheme();

        // Default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar Preferences
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation bar drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Jsoup
        new Description().execute();


    }

    /**
     * Checks which theme is preferred and sets it
     * gets param from SettingsActivity.class
     * run before setContentView
     * //Different from SettingsMethodCurrentTheme!!
     */
    public void currentTheme() {
        //Preferences for Theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean value = (sharedPreferences.getBoolean("themekey", true));

        if(value) {
            Toast.makeText(this, "false light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_NoActionBar);
        } else  {
            Toast.makeText(this, "true dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override //->Small menu (ex.Info, Settings, Help)
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override //->Info, Settings, Help Menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if(id == R.id.action_settings){
            //open settings activity
            startActivity(new Intent(MainActivity.this,
                    SettingsActivity.class));
            return true;
        }
        if(id == R.id.action_help){
            //open help activity
            startActivity(new Intent(this, HelpActivity.class));
        }
        if(id == R.id.action_info){
            //open info activity
            startActivity(new Intent(this, InfoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override //-> NavigationDrawer (ex. camera, gallery...)
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class Description extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // ProgressDialog -> waiting for app loads up correctly
            mProgressDialog = new ProgressDialog(MainActivity.this).show(MainActivity.this, "Hold on!",
                    "Loading. Please wait...");
            mProgressDialog.setIndeterminate(false);

        }

        /**
         * parse the website information to the mainActivity (ex. text and images)
         * locate src attribute of img downloads image from url and decodes it to bitmap
         * adds this information in to array lists
         *
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document mWebsiteDocument = Jsoup.connect(url).get();

                // Using Elements to get the Meta data
                Elements mElementDataSize = mWebsiteDocument.select("article[class=news-list-item]");

                // Locate the content attribute
                int mElementSize = mElementDataSize.size();

                for (int i = 0; i < mElementSize; i++) {
                    Elements mElementNewsHeadline = mWebsiteDocument.select("h2[class=news-headline]").select("a").eq(i);
                    String mNewsHeadline = mElementNewsHeadline.text();

                    Elements mElementImageContainer = mWebsiteDocument.select("figure[class=news-image-container]").select("img").eq(i);
                    // locate src attribute
                    String mImageContainer = mElementImageContainer.attr("src");
                    // download image from url
                    InputStream input = new java.net.URL(mImageContainer).openStream();
                    // decode bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    Elements mElementNewsDescription = mWebsiteDocument.select("div[class=news-content]").select("p").eq(i);
                    String mNewsDescription = mElementNewsDescription.text();

                    mNewsHeadlineList.add(mNewsHeadline);
                    mNewsImageContainerList.add(bitmap);
                    mNewsDescriptionList.add(mNewsDescription);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * takes advantage from dataAdapter.class
         * waits for the progress finish and starts to dismiss the progress dialog message
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView

            RecyclerView mRecyclerView = findViewById(R.id.content_main_view);

            DataAdapter mDataAdapter = new DataAdapter(MainActivity.this, mNewsHeadlineList, mNewsImageContainerList, mNewsDescriptionList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);

            mProgressDialog.dismiss();
        }
    }
}

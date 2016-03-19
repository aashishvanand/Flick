package com.aashish.flick;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    static int activeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (activeId == 0){
            activeId = R.id.action_sort_popularity;
        } else {
            menu.findItem(activeId).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MainActivityFragment fragment = MainActivityFragment.instance;

        if (id == R.id.action_sort_rating) {
            MainActivityFragment.sortOrder = "vote_average.desc";
            MainActivityFragment.moreParams = "vote_count.gte=50&include_video=false";
        }
        else if (id == R.id.action_sort_popularity) {
            MainActivityFragment.sortOrder = "popularity.desc";
            MainActivityFragment.moreParams = "";
        }
        else if (id == R.id.action_revenue) {
            MainActivityFragment.sortOrder = "revenue.desc";
            MainActivityFragment.moreParams = "";
        }
        else if (id == R.id.action_now_playing) {
            MainActivityFragment.sortOrder = "now_playing";
            MainActivityFragment.moreParams = "";
        }



        item.setChecked(true);
        if (id == R.id.action_sort_popularity || id == R.id.action_sort_rating || id == R.id.action_revenue || id == R.id.action_now_playing){
            fragment.updateUI(false);
            activeId = id;
        } else if (id == R.id.action_favorites){
            fragment.updateUI(true);
            activeId = id;
        }
        return super.onOptionsItemSelected(item);
    }
}

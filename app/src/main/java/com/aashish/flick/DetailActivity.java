package com.aashish.flick;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView header = (ImageView) findViewById(R.id.headerimage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        movie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        DetailActivityFragment detailFragment = (DetailActivityFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        detailFragment.movie = movie;
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        assert collapsingToolbar != null;
        collapsingToolbar.setTitle(movie.display_name);
        Picasso.with(getApplicationContext()).load(movie.backdrop_url).into(header);

    }
}

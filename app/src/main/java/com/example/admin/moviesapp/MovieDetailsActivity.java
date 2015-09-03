package com.example.admin.moviesapp;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private int selectedMovieId_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedMovieId_ = extras.getInt("movieId",0);
        Timber.v(Integer.toString(selectedMovieId_));


        String itemTitle = "Item Name";
        collapsingToolbarLayout_ = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout_.setTitle(itemTitle);

        collapsingToolbarLayout_.setExpandedTitleColor(getResources().getColor(R.color.background_floating_material_dark));

        collapsingToolbarLayout_.setCollapsedTitleTextColor(getResources().getColor(R.color.accent_material_dark));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

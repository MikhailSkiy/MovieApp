package com.example.admin.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.moviesapp.adapters.TrailersAdapter;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.PlayBtnClickListener;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Video;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity implements UpdateListener {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private long selectedMovieId_;
    private ImageView cover_;
    private CardView movieCard_;
    private TextView title_;
    private TextView description_;

    private TrailersAdapter trailersAdapter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedMovieId_ = extras.getLong("movieId", 0);
        Timber.v(Long.toString(selectedMovieId_));

        // Send MovieDetailsRequest
        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);
        manager.sendMessage(manager.obtainMessage(States.MOVIES_DETAILS_REQUEST, selectedMovieId_));

        cover_ = (ImageView)findViewById(R.id.image);
        movieCard_ = (CardView)findViewById(R.id.movie_card);
        title_ = (TextView)findViewById(R.id.title);
        description_ = (TextView)findViewById(R.id.description);

        String itemTitle = "Item Name";
        collapsingToolbarLayout_ = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout_.setTitle(itemTitle);
        collapsingToolbarLayout_.setExpandedTitleColor(getResources().getColor(R.color.background_floating_material_dark));
        collapsingToolbarLayout_.setCollapsedTitleTextColor(getResources().getColor(R.color.accent_material_dark));

        // Trailers adapter
        List<Video> trailers = new ArrayList<>();
        Video trailer = new Video();
        trailer.setName("Traile Name");
        trailer.setKey("sdsd");


        Video trailer1 = new Video();
        trailer.setName("Traile Name2");
        trailer.setKey("sdsd");


        Video trailer2 = new Video();
        trailer.setName("Traile Name3");
        trailer.setKey("sdsd");

        trailers.add(trailer);
        trailers.add(trailer1);
        trailers.add(trailer2);

        

//        RecyclerView trailerRecyclerView = (RecyclerView)findViewById(R.id.trailer_list);
//        trailersAdapter_ = new TrailersAdapter(trailers, R.layout.item_trailer, this, new PlayBtnClickListener() {
//            @Override
//            public void onPlayTrailerClick(String trailerId) {
//                Toast.makeText(getApplicationContext(),"Hi Play Trailer",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        trailerRecyclerView.setAdapter(trailersAdapter_);
//        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {

        List<MovieDetails> movies = (List<MovieDetails>)resultList;
        cover_.setImageBitmap(Util.getBitmapFromBytes(movies.get(0).getCover()));
        collapsingToolbarLayout_.setTitle(movies.get(0).getOriginalTitle());

        title_.setText(movies.get(0).getOriginalTitle());
        description_.setText(movies.get(0).getOverview());
    }

    @Override
    public void onErrorRaised(String errorMsg){
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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

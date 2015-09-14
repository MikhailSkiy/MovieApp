package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.adapters.MoviesAdapter;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.MovieItemClickListener;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.Trailer;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements UpdateListener {

    private MoviesAdapter moviesAdapter_;
    private DbHelper helper_ = new DbHelper(this);
    private List<Movie> moviesList_ = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        moviesAdapter_ = new MoviesAdapter(moviesList_, R.layout.item_movie_card, this, new MovieItemClickListener() {
            @Override
            public void onMovieItemClick(final long movieId) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(moviesAdapter_);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Check if there are any movies in database
        // If so get list of movies and update recyclerView
        moviesList_ = helper_.getAllMovies();
        if (moviesList_.size() != 0) {
            Timber.v(Integer.toString(moviesList_.size()));
            onUpdate(moviesList_);
        } else {
            // Get instance of RequestManger
            RequestManager manager = RequestManager.getInstance();
            // Initialize it by UpdateListener
            manager.init(this);
            manager.sendMessage(manager.obtainMessage(States.MOVIES_REQUEST));
        }
    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {
        List<Movie> movies = (List<Movie>) resultList;
        for (int i = 0; i < movies.size(); i++) {
            moviesAdapter_.addMovie(movies.get(i));
            // Add items into database
            helper_.insertMovie(movies.get(i));
        }

    }

    @Override
    public void UpdateTrailers(List<Trailer> trailers) {
    }

    @Override
    public void UpdateCasts(List<? extends CommonMovie> casts) {
    }

    @Override
    public void onErrorRaised(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

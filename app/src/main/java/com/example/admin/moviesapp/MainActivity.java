package com.example.admin.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.moviesapp.adapters.MoviesAdapter;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.MovieItemClickListener;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.Result;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UpdateListener {

    private MoviesAdapter moviesAdapter_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);

        manager.sendMessage(manager.obtainMessage(States.MOVIES_REQUEST));

        List<Movie> moviesList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        moviesAdapter_ = new MoviesAdapter(moviesList,R.layout.item_movie_card,this,new MovieItemClickListener(){
            @Override
        public void onMovieItemClick(final long movieId){
                Intent intent = new Intent(MainActivity.this,MovieDetailsActivity.class);
                intent.putExtra("movieId",movieId);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(moviesAdapter_);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onUpdate(List<? extends Result> resultList) {
        List<Movie> movies = (List<Movie>)resultList;
        moviesAdapter_.addMovie(movies.get(0));
    }

    @Override
    public void onErrorRaised(String errorMsg){
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

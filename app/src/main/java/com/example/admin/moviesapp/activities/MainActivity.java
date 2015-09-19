package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

    private static final String FIRST_TIME = "first_time";
    private MoviesAdapter moviesAdapter_;
    private DbHelper helper_ = new DbHelper(this);
    private List<Movie> moviesList_ = new ArrayList<>();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView filterNavigationMenu_;
    private boolean mUserSawDrawer = false;
    private int mSelectedId;
    private boolean isActionSelected = false;


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


        filterNavigationMenu_ = (NavigationView) findViewById(R.id.filter_navigation_menu);
        filterNavigationMenu_.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                menuItem.setChecked(true);
                mSelectedId = menuItem.getItemId();
                updateUI(mSelectedId);
                return true;
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        // navigate


        setSupportActionBar(mToolbar);

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
            helper_.addMovie(movies.get(i));
        }

    }


    private void updateUI(int mSelectedId) {
        Intent intent = null;
        if ((mSelectedId == R.id.action_btn_off)||(mSelectedId == R.id.action_btn_on)) {
            if (filterNavigationMenu_.getMenu().findItem(R.id.action_btn_off).isChecked()) {
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_off).setVisible(false);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_on).setVisible(true);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_off).setChecked(false);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_on).setChecked(true);
            } else {
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_off).setVisible(true);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_on).setVisible(false);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_off).setChecked(true);
                filterNavigationMenu_.getMenu().findItem(R.id.action_btn_on).setChecked(false);

            }

            // Tricky solution http://stackoverflow.com/questions/31181024/cant-change-icons-for-subitems-in-navigationview
            // So resetting the title of top level item will update UI
            filterNavigationMenu_.getMenu().getItem(0).setTitle(filterNavigationMenu_.getMenu().getItem(0).getTitle());

        }
        if (mSelectedId == R.id.action_btn_on) {
            filterNavigationMenu_.getMenu().findItem(R.id.action_btn_on).setVisible(true);
        }

    }

    private void markAsSelected(){
        isActionSelected = true;
    }

    private void markAsUnSelected(){
        isActionSelected = false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}

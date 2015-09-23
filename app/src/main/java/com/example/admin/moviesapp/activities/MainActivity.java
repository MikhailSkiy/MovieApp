package com.example.admin.moviesapp.activities;

import android.content.Context;
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
import com.example.admin.moviesapp.helpers.GenresMap;
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
    private LinearLayoutManager layoutManager_;
    private static Context contextOfApplication_;
    private boolean mUserSawDrawer = false;
    private int mSelectedId;
    private boolean isActionSelected = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page_= 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the context
        contextOfApplication_ = getApplicationContext();

        layoutManager_ = new LinearLayoutManager(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        moviesAdapter_ = new MoviesAdapter(moviesList_, R.layout.item_movie_card, this, new MovieItemClickListener() {
            @Override
            public void onMovieItemClick(final long movieId) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
        public void onScrolled(RecyclerView recyclerView1,int dx,int dy){
                visibleItemCount = layoutManager_.getChildCount();
                totalItemCount = layoutManager_.getItemCount();
                pastVisiblesItems = layoutManager_.findFirstVisibleItemPosition();
                if ((visibleItemCount +pastVisiblesItems)>=totalItemCount){
                    page_++;
                    sendMovieRequest();
                }
            }
        });


        filterNavigationMenu_ = (NavigationView) findViewById(R.id.filter_navigation_menu);
        filterNavigationMenu_.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                menuItem.setChecked(true);
                mSelectedId = menuItem.getItemId();
                updateUI(mSelectedId);
                moviesList_.clear();
                moviesAdapter_.notifyDataSetChanged();

                sendMovieRequest();
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
        recyclerView.setLayoutManager(layoutManager_);

        // Check if there are any movies in database
        // If so get list of movies and update recyclerView
//        moviesList_ = helper_.getAllMovies();
//        if (moviesList_.size() != 0) {
//            Timber.v(Integer.toString(moviesList_.size()));
//            onUpdate(moviesList_);
//        } else {
//            sendMovieRequest();
//        }
        sendMovieRequest();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication_;
    }

    public void sendMovieRequest(){
        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);
        manager.sendMessage(manager.obtainMessage(States.MOVIES_REQUEST,page_));
    }


    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {
        List<Movie> movies = (List<Movie>) resultList;
        for (int i = 0; i < movies.size(); i++) {
            moviesAdapter_.addMovie(movies.get(i));
            // Add items into database
            //helper_.addMovie(movies.get(i));
        }

    }

    private int getGenreValue(String genreName, boolean mode) {
        GenresMap map = new GenresMap();
        int id = (mode == true) ? map.genresMap.get(genreName) : 0;
        return id;
    }

    private void setFilterPreferences(int selectedBtnId, boolean mode) {
        switch (selectedBtnId) {
            case R.id.action_btn:
                setSelectedGenreId("Action", getGenreValue("Action", mode));
                break;
            case R.id.adventure_btn:
                setSelectedGenreId("Adventure", getGenreValue("Adventure", mode));
                break;
            case R.id.animation_btn:
                setSelectedGenreId("Animation", getGenreValue("Animation", mode));
                break;
            case R.id.comedy_btn:
                setSelectedGenreId("Comedy", getGenreValue("Comedy", mode));
                break;
            case R.id.crime_btn:
                setSelectedGenreId("Crime", getGenreValue("Crime", mode));
                break;
            case R.id.documentary_btn:
                setSelectedGenreId("Documentary", getGenreValue("Documentary", mode));
                break;
            case R.id.drama_btn:
                setSelectedGenreId("Drama", getGenreValue("Drama", mode));
                break;
            case R.id.family_btn:
                setSelectedGenreId("Family", getGenreValue("Family", mode));
                break;
            case R.id.fantasy_btn:
                setSelectedGenreId("Fantasy", getGenreValue("Fantasy", mode));
                break;
            case R.id.foreign_btn:
                setSelectedGenreId("Foreign", getGenreValue("Foreign", mode));
                break;
            case R.id.history_btn:
                setSelectedGenreId("History", getGenreValue("History", mode));
                break;
            case R.id.horror_btn:
                setSelectedGenreId("Horror", getGenreValue("Horror", mode));
                break;
            case R.id.music_btn:
                setSelectedGenreId("Music", getGenreValue("Music", mode));
                break;
            case R.id.mystery_btn:
                setSelectedGenreId("Mystery", getGenreValue("Mystery", mode));
                break;
            case R.id.romance_btn:
                setSelectedGenreId("Romance", getGenreValue("Romance", mode));
                break;
            case R.id.science_fiction_btn:
                setSelectedGenreId("Science Fiction", getGenreValue("Science Fiction", mode));
                break;
            case R.id.tv_movie_btn:
                setSelectedGenreId("TV Movie", getGenreValue("TV Movie", mode));
                break;
            case R.id.thriller_btn:
                setSelectedGenreId("Thriller", getGenreValue("Thriller", mode));
                break;
            case R.id.war_btn:
                setSelectedGenreId("War", getGenreValue("War", mode));
                break;
            case R.id.western_btn:
                setSelectedGenreId("Western", getGenreValue("Western", mode));
                break;
            default:
                break;
        }

    }

    /**
     * Puts selected genreId and appropriate key into Shared Preferences
     *
     * @param key             The name of selected genre
     * @param selectedGenreId The id of selected genre
     */
    private void setSelectedGenreId(String key, int selectedGenreId) {
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.filter_preferences), this.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putInt(key, selectedGenreId);
        preferenceEditor.commit();
    }

    private void updateUI(int mSelectedId) {
        // If it is not such items, change genre items
        if ((mSelectedId != R.id.popularity_filter_menu_btn) &&
                (mSelectedId != R.id.rating_filter_menu_btn) &&
                (mSelectedId != R.id.revenue_filter_menu_btn)) {
            changeGenreBtnState(mSelectedId);
            // Otherwise change sort_by items
        } else {
            changeSortBtnState(mSelectedId);
        }


        // Tricky solution http://stackoverflow.com/questions/31181024/cant-change-icons-for-subitems-in-navigationview
        // So resetting the title of top level item will update UI
        filterNavigationMenu_.getMenu().getItem(0).setTitle(filterNavigationMenu_.getMenu().getItem(0).getTitle());

    }

    private void changeGenreBtnState(int btnId) {
        boolean isBtnChecked = filterNavigationMenu_.getMenu().findItem(btnId).isChecked();
        MenuItem selectedMenuItem = filterNavigationMenu_.getMenu().findItem(btnId);
        if (!isBtnChecked) {
            turnOnBtn(selectedMenuItem);
            setFilterPreferences(btnId, true);
        } else {
            turnOffBtn(selectedMenuItem);
            setFilterPreferences(btnId, false);
        }
    }

    private void turnOffBtn(MenuItem menuItem) {
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_checkbox_blank_circle_outline_grey));
        menuItem.setChecked(false);
    }

    private void turnOnBtn(MenuItem menuItem) {
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_checkbox_marked_circle_outline_grey));
        menuItem.setChecked(true);
    }

    private void changeSortBtnState(int btnId) {
        boolean isBtnChecked = filterNavigationMenu_.getMenu().findItem(btnId).isChecked();
        MenuItem selectedMenuItem = filterNavigationMenu_.getMenu().findItem(btnId);
        if (!isBtnChecked) {
            turnOnDescSortBtn(selectedMenuItem);
        } else {
            turnOnAscSortBtn(selectedMenuItem);
        }
    }

    private void turnOnDescSortBtn(MenuItem menuItem) {
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_sort_descending_grey600_24dp));
        menuItem.setChecked(true);
    }

    private void turnOnAscSortBtn(MenuItem menuItem) {
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_sort_ascending_grey600_24dp));
        menuItem.setChecked(false);
    }


    private void markAsSelected() {
        isActionSelected = true;
    }

    private void markAsUnSelected() {
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

        if (id == R.id.action_filter) {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }

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

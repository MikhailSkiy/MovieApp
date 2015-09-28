package com.example.admin.moviesapp.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
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
import com.example.admin.moviesapp.database.Contract;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.GenresMap;
import com.example.admin.moviesapp.helpers.MovieRequestHelper;
import com.example.admin.moviesapp.helpers.SortTypeMap;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.MovieItemClickListener;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.ui.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UpdateListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String FIRST_TIME = "first_time";
    private static final int MOVIES_LOADER = 0;
    private MoviesAdapter moviesAdapter_;
    private DbHelper helper_ = new DbHelper(this);
    private List<Movie> moviesList_ = new ArrayList<>();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView filterNavigationMenu_;
    private LinearLayoutManager layoutManager_;
    private RecyclerViewEmptySupport recyclerView_;
    private Cursor cursor_ = null;
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

        recyclerView_ = (RecyclerViewEmptySupport) findViewById(R.id.recycler_view);
        recyclerView_.setEmptyView(this.findViewById(R.id.listview_empty));

        // Update UI in case of bad connection
        // updateEmptyView();

        getLoaderManager().initLoader(MOVIES_LOADER,null,this);

        recyclerView_.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

        recyclerView_.setAdapter(moviesAdapter_);

        recyclerView_.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView_.setLayoutManager(layoutManager_);


        restoreFilterMenu();


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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        List<Integer> genres = MovieRequestHelper.getGenres();
        String sortBy = MovieRequestHelper.getSortTypeFromPreferences();
        sortBy = sortBy.replace(".", " ");
        final String selectQuery2 = " SELECT * FROM " + Contract.MoviesEntry.TABLE_NAME;
//        for (Integer genreId : genres) {
//            String selectQuery = " SELECT * FROM " + Contract.MovieGenreEntry.TABLE_NAME + " WHERE " + Contract.MovieGenreEntry.COLUMN_GENRE_ID + " = " + genreId;
//            Cursor cursor = helper_.getReadableDatabase().rawQuery(selectQuery,null);
//        }

        return new CursorLoader(MainActivity.this){
            public Cursor loadInBackground(){
                return helper_.getReadableDatabase().rawQuery(selectQuery2,null);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        cursor_ = cursor;
        moviesAdapter_ = new MoviesAdapter(cursor_, R.layout.item_movie_card, this, new MovieItemClickListener() {
            @Override
            public void onMovieItemClick(final long movieId) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        moviesAdapter_.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){}

    private void updateEmptyView() {
        if (moviesList_.size() == 0) {
            int status = Util.getConnectionStatus(this);
            switch (status) {
                case Constants.CONNECTION_STATUS_OK:
                    break;
                case Constants.NO_CONNECTION:
                    recyclerView_.setEmptyView(this.findViewById(R.id.listview_empty));
                    break;
                default:
                    break;
            }

        }
    }

    public static Context getContextOfApplication() {
        return contextOfApplication_;
    }

    public void sendMovieRequest(){
        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);
        // TODO make reset page when genre is changed!
        manager.sendMessage(manager.obtainMessage(States.MOVIES_REQUEST,page_));
    }


    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {
        List<Movie> movies = (List<Movie>) resultList;
//        if (movies.size() == 0){
//            recyclerView_.setEmptyView(this.findViewById(R.id.listview_empty));
//        }
        for (int i = 0; i < movies.size(); i++) {
            helper_.addMovie(movies.get(i));
        }

        final String selectQuery2 = " SELECT * FROM " + Contract.MoviesEntry.TABLE_NAME;
        moviesAdapter_.swapCursor(helper_.getReadableDatabase().rawQuery(selectQuery2,null));
        moviesAdapter_.notifyDataSetChanged();

    }

    private int getGenreValue(String genreName, boolean mode) {
        GenresMap map = new GenresMap();
        int id = (mode == true) ? map.genresMap.get(genreName).genreId : 0;
        return id;
    }

    private String getSortType(String sortName, boolean mode){
        String type = (mode == true) ? "desc" : "asc";
        return type;
    }

    private void setFilterPreferences(int selectedBtnId, boolean mode) {
        String genreName="";
        switch (selectedBtnId) {
            case R.id.action_btn:
                genreName = Util.getStringResource(R.string.action_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.adventure_btn:
                genreName = Util.getStringResource(R.string.adventure_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.animation_btn:
                genreName = Util.getStringResource(R.string.animation_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.comedy_btn:
                genreName = Util.getStringResource(R.string.comedy_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.crime_btn:
                genreName = Util.getStringResource(R.string.crime_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.documentary_btn:
                genreName = Util.getStringResource(R.string.documentary_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.drama_btn:
                genreName = Util.getStringResource(R.string.drama_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.family_btn:
                genreName = Util.getStringResource(R.string.family_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.fantasy_btn:
                genreName = Util.getStringResource(R.string.fantasy_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.foreign_btn:
                genreName = Util.getStringResource(R.string.foreign_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.history_btn:
                genreName = Util.getStringResource(R.string.history_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.horror_btn:
                genreName = Util.getStringResource(R.string.horror_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.music_btn:
                genreName = Util.getStringResource(R.string.music_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.mystery_btn:
                genreName = Util.getStringResource(R.string.mystery_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.romance_btn:
                genreName = Util.getStringResource(R.string.romance_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
                break;
            case R.id.science_fiction_btn:
                genreName = Util.getStringResource(R.string.romance_key);
                setSelectedGenreId(genreName, getGenreValue(genreName, mode));
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

    private void setSortPreferences(int selectedBtnId, boolean mode) {
        switch (selectedBtnId) {
            case R.id.popularity_filter_menu_btn:
                setSelectedSortingType("popularity", getSortType("popularity", mode));
                break;
            case R.id.rating_filter_menu_btn:
                setSelectedSortingType("vote_average", getSortType("vote_average", mode));
                break;
            case R.id.revenue_filter_menu_btn:
                setSelectedSortingType("revenue",getSortType("revenue",mode));
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

    private void setSelectedSortingType(String key, String sortType) {
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.filter_preferences), this.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString("Sort_type",key);
        preferenceEditor.putString(key, sortType);
        preferenceEditor.commit();
    }

    private void restoreFilterMenu(){
        restoreGenresSubMenu();
        restoreSortingSubMenu();
    }

    private void restoreGenresSubMenu() {
        Menu menu = filterNavigationMenu_.getMenu();

        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);

        GenresMap map = new GenresMap();
        for (String s : map.genresMap.keySet()) {
            int genreId = sharedPreferences.getInt(s, 0);
            GenresMap.GenreContainer itemCntr = map.genresMap.get(s);
            if (genreId != 0) {
                MenuItem menuItem = menu.findItem(itemCntr.itemId);
                turnOnBtn(menuItem);
            }
        }
    }

    private void restoreSortingSubMenu(){
        Menu menu = filterNavigationMenu_.getMenu();

        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.filter_preferences), Context.MODE_PRIVATE);

        String sortType = sharedPreferences.getString("Sort_type","popularity");
        String sortValue = sharedPreferences.getString(sortType,"desc");

        if (sortType.equals("popularity")) {
            MenuItem item = menu.findItem(R.id.popularity_filter_menu_btn);
            if (sortValue == "desc") {
                turnOnDescSortBtn(item);
            } else {
                turnOnAscSortBtn(item);
            }
        } else if (sortType.equals("vote_average")) {
            MenuItem item = menu.findItem(R.id.rating_filter_menu_btn);
            if (sortValue == "desc") {
                turnOnDescSortBtn(item);
            } else {
                turnOnAscSortBtn(item);
            }
        } else if (sortType.equals("revenue")) {
            MenuItem item = menu.findItem(R.id.revenue_filter_menu_btn);
            if (sortValue == "desc"){
                turnOnDescSortBtn(item);
            } else {
                turnOnAscSortBtn(item);
            }
        }

    }

    private void updateUI(int mSelectedId) {
        // reset number page if filter settings were changed
        page_ = 1;
        // If it is not such items, change genre items
        if ((mSelectedId != R.id.popularity_filter_menu_btn) &&
                (mSelectedId != R.id.rating_filter_menu_btn) &&
                (mSelectedId != R.id.revenue_filter_menu_btn)) {
            changeGenreBtnState(mSelectedId);
            // Otherwise change sort_by items
        } else {
            changeSortBtnState(mSelectedId);
        }


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

        // Tricky solution http://stackoverflow.com/questions/31181024/cant-change-icons-for-subitems-in-navigationview
        // So resetting the title of top level item will update UI
        filterNavigationMenu_.getMenu().getItem(0).setTitle(filterNavigationMenu_.getMenu().getItem(0).getTitle());
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
            setSortPreferences(btnId,true);
        } else {
            turnOnAscSortBtn(selectedMenuItem);
            setSortPreferences(btnId,false);
        }

        // Tricky solution http://stackoverflow.com/questions/31181024/cant-change-icons-for-subitems-in-navigationview
        // So resetting the title of top level item will update UI
        filterNavigationMenu_.getMenu().findItem(R.id.navigation_subheader).setTitle(filterNavigationMenu_.getMenu().findItem(R.id.navigation_subheader).getTitle());
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

package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.adapters.CastRecyclerViewAdapter;
import com.example.admin.moviesapp.adapters.CastViewPagerAdapter;
import com.example.admin.moviesapp.events.UpdateCastDetailsImageEvent;
import com.example.admin.moviesapp.events.UpdateCastDetailsUI;
import com.example.admin.moviesapp.events.UpdateMovieCreditsListEvent;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CastDetails;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.MovieCredits;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.requests.MovieCreditsRequest;
import com.example.admin.moviesapp.ui.CustomDividerItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class CastDetailsActivity extends AppCompatActivity implements UpdateListener{

    // Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;
    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private CastViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ImageView profileImage_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_details);

        //Register EventBus
        EventBus.getDefault().register(this);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String selectedCastId = extras.getString("castId");
        Timber.v(selectedCastId);

        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);

        manager.sendMessage(manager.obtainMessage(States.CAST_DETAILS_REQUEST, selectedCastId));
        manager.sendMessage(manager.obtainMessage(States.MOVIE_CREDITS_REQUEST,selectedCastId));

        profileImage_ = (ImageView)findViewById(R.id.profile_image);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Lalal");
        supportPostponeEnterTransition();
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mAdapter = new CastViewPagerAdapter(getSupportFragmentManager(),this);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    public void onEvent(UpdateCastDetailsImageEvent e){
        Timber.v("Event in activity");
        updateImage(e.getImage());
    }

    private void updateImage(byte[] image){
        profileImage_.setImageBitmap(Util.getBitmapFromBytes(image));
        profileImage_.setVisibility(View.VISIBLE);
    }

    public void onUpdate(List<? extends CommonMovie> resultList){}
    public void UpdateTrailers(List<Trailer> trailersList){}
    public void UpdateCasts(List<? extends CommonMovie> casts){}
    public void onErrorRaised(String errorMsg){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cast_details, menu);
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

    @Override
    protected void onDestroy() {
        // Unregister this activity
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        private CastRecyclerViewAdapter adapter_;
        private RecyclerView listView_;
        private TextView bioDescription_;
        private ImageView profileImage_;
        RequestManager manager_ ;



        private List<MovieCredits> movieCreditsList_ = new ArrayList<>();

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//            manager_ = RequestManager.getInstance();
//            manager_.init((CastDetailsActivity)getActivity());
//            manager_.sendMessage(manager_.obtainMessage(States.MOVIE_CREDITS_REQUEST,selectedCastId));
//        }

        @Override
        public void onCreate(Bundle savedInstance){
            super.onCreate(savedInstance);
            // Register EventBus
            EventBus.getDefault().register(this);
        }


        public void onEvent(UpdateMovieCreditsListEvent e){
            movieCreditsList_.add(e.getMovieCredits());
            adapter_.notifyDataSetChanged();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            adapter_ = new CastRecyclerViewAdapter(getActivity(),movieCreditsList_);

//            RecyclerView recyclerView = new RecyclerView(getActivity());
//            recyclerView.setAdapter(new CastRecyclerViewAdapter(getActivity()));
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            View view = inflater.inflate(R.layout.fragment_movies_credits,container,false);
            listView_ = (RecyclerView)view.findViewById(R.id.movies_credits_list);
            listView_.addItemDecoration(new CustomDividerItemDecoration(getResources()));
            listView_.setAdapter(adapter_);
            listView_.setItemAnimator(new DefaultItemAnimator());
            listView_.setLayoutManager(new LinearLayoutManager(getActivity()));
            return view;
        }


//        public void onEvent(UpdateCastDetailsImageEvent e){
//            Timber.v("Event in fragment");
//            Timber.v("UpdateCastDetailsImageEvent");
//            for (int i=0;i<3;i++){
//
//                if ( movieCreditsList_.get(i).getPosterPath() == e.getPath()){
//                    movieCreditsList_.get(i).setCover(e.getImage());
//
//                }
//            }
//
//        }

//        public void onEvent(UpdateCreditsImageEvent e){
//            Timber.v("Event in fragment");
//            Timber.v("UpdateCastDetailsImageEvent");
//            for (int i=0;i<3;i++){
//
//                if ( movieCreditsList_.get(i).getPosterPath() == e.getPath()){
//                    movieCreditsList_.get(i).setCover(e.getImage());
//
//                }
//            }
//
//        }

        private void setRecyclerView(List<MovieCredits> movies){
            adapter_ = new CastRecyclerViewAdapter(getActivity(),movieCreditsList_);

        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            // Unregister EventBus
            EventBus.getDefault().unregister(this);
        }

        private void updateCastDetailsInfo(CastDetails castDetails){
            bioDescription_.setText(castDetails.getBiography());
        }

        private void updateImage(byte[] image){

        }
    }
}



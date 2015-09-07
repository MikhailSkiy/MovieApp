package com.example.admin.moviesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.moviesapp.adapters.CastRecyclerViewAdapter;
import com.example.admin.moviesapp.adapters.CastViewPagerAdapter;


public class CastDetailsActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout_;
    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private Toolbar toolbar_;
    private DrawerLayout drawerLayout_;
    private ViewPager viewPager_;
    private CastViewPagerAdapter viewPagerAdapter_;
    private TabLayout tabLayout_;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_details);

        // mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

//        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Lalal2");
//        supportPostponeEnterTransition();

        coordinatorLayout_ = (CoordinatorLayout)findViewById(R.id.root_coordinator);
        collapsingToolbarLayout_ = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_ = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar_);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar_, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        tabLayout_ = (TabLayout)findViewById(R.id.tab_layout);
        viewPagerAdapter_ = new CastViewPagerAdapter(getSupportFragmentManager());

        viewPager_ = (ViewPager)findViewById(R.id.view_pager);
        viewPager_.setAdapter(viewPagerAdapter_);
        //Notice how the Tab Layout links with the Pager Adapter
        tabLayout_.setTabsFromPagerAdapter(viewPagerAdapter_);

        //Notice how The Tab Layout adn View Pager object are linked
        tabLayout_.setupWithViewPager(viewPager_);
        viewPager_.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_));

        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        collapsingToolbarLayout_.setTitle(getResources().getString(R.string.hello_world));
    }

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

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setAdapter(new CastRecyclerViewAdapter(getActivity()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }
    }
}

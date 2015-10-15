package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.ViewServer;
import com.example.admin.moviesapp.adapters.MovieDetailsViewPager;
import com.example.admin.moviesapp.adapters.TrailersAdapter;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.events.UpdateMovieDetailsImageEvent;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity implements UpdateListener {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private long selectedMovieId_;
    private ImageView cover_;
    private DbHelper helper_ = new DbHelper(this);

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private MovieDetailsViewPager mAdapter;
    private TabLayout mTabLayout;
    private CoordinatorLayout mCoordinator;
    private FloatingActionButton mainFloatingActionButton_;
    private FloatingActionButton addToFavoriteFloatingActionSubButton_;
    private FloatingActionButton addToWatchlistFloatingActionSubButton_;
    private FloatingActionButton addToListFloatingActionSubButton_;


    private CardView movieCard_;

    private TrailersAdapter trailersAdapter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Lalal");
        supportPostponeEnterTransition();
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Register EventBus
        EventBus.getDefault().register(this);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedMovieId_ = extras.getLong("movieId", 0);
        Timber.v(Long.toString(selectedMovieId_));

        cover_ = (ImageView) findViewById(R.id.coverImage);
        movieCard_ = (CardView) findViewById(R.id.movie_card);
        setupFAB();


        String itemTitle = "Item Name";
        collapsingToolbarLayout_ = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbarLayout_.setTitle(itemTitle);
        collapsingToolbarLayout_.setExpandedTitleColor(getResources().getColor(R.color.background_floating_material_dark));
        collapsingToolbarLayout_.setCollapsedTitleTextColor(getResources().getColor(R.color.accent_material_dark));

        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mAdapter = new MovieDetailsViewPager(getSupportFragmentManager(),this,  selectedMovieId_);
        mPager = (ViewPager) findViewById(R.id.movie_details_view_pager);
        mPager.setAdapter(mAdapter);

        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        ViewServer.get(this).addWindow(this);
    }


    private void setupFAB(){
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(this.getDrawable(R.drawable.ic_add_black));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(this.getDrawable(R.drawable.ic_playlist_play_grey));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(this.getDrawable(R.drawable.ic_wunderlist_grey));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(this.getDrawable(R.drawable.ic_heart_outline_grey));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();
    }

//    private void setupFAB() {
//        mainFloatingActionButton_ = (FloatingActionButton) findViewById(R.id.main_fab);
//        // Init all subbuttons
//        addToFavoriteFloatingActionSubButton_ = (FloatingActionButton)findViewById(R.id.favorite_fab);
//        addToWatchlistFloatingActionSubButton_ = (FloatingActionButton)findViewById(R.id.watchlist_fab);
//        addToListFloatingActionSubButton_ = (FloatingActionButton)findViewById(R.id.list_fab);
//
//        mainFloatingActionButton_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToFavoriteFloatingActionSubButton_.setVisibility(View.VISIBLE);
//                addToWatchlistFloatingActionSubButton_.setVisibility(View.VISIBLE);
//                addToListFloatingActionSubButton_.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

    public void onEvent(UpdateMovieDetailsImageEvent e){
        Timber.v("Event in activity");
        updateImage(e.getMovieDetails());
    }

    private void updateImage(MovieDetails movieDetails){
        cover_.setImageBitmap(Util.getBitmapFromBytes(movieDetails.getCover()));
        cover_.setVisibility(View.VISIBLE);
        collapsingToolbarLayout_.setTitle(movieDetails.getTitle());
    }

    private void createCastItem(Cast cast) {

        //region Create Cast Layout
        // Find Cast Layout
        LinearLayout castLayout = (LinearLayout) findViewById(R.id.cast_layout);
        //endregion

        //region Create cast item (Profile Name)
        // And set params
        RelativeLayout castItem = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams castItemParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        castItemParams.setMargins(0, 0, 0, 16);
        //endregion

        //region Create FrameLayout and set params
        // Set size for FrameLayout width and height
        int frameLayoutSize = getResources().getDimensionPixelOffset(R.dimen.frame2_layout_size);
        // Create FrameLayout width size (width and height)
        FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        frameLayout.setId(R.id.frameId2);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(frameLayoutSize, frameLayoutSize);

        // Set margins for FrameLayout
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_large);
        int marginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
        frameParams.setMargins(marginLeft, marginTop, marginRight, 0);
        //endregion

        //region Create CastProfile
        final ImageView castProfileImage = new ImageView(getApplicationContext());
        castProfileImage.setImageBitmap(Util.getRoundedCroppedBitmap(Util.getBitmapFromBytes(cast.getCover()), Constants.DEFAULT_CROPPING_RADIUS));
        castProfileImage.setTag(cast.getId());
        // Set onClickListener
        castProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this, CastDetailsActivity.class);
                intent.putExtra("castId", castProfileImage.getTag().toString());
                startActivity(intent);
            }
        });
        //endregion

        // Add PlayButton into FrameLayout with appropriate params (margins etc)
        frameLayout.addView(castProfileImage, frameParams);


        //region Create TextView for name of cast
        TextView textView = new TextView(getApplicationContext());
        textView.setId(R.id.cast_name);
        textView.setText(cast.getName());
        textView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
        textView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        // Set margins for textView
        int textViewMarginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        int textViewMarginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int textViewMarginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall);

        // Create params for textView
        RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        textViewLayoutParams.setMargins(textViewMarginLeft, textViewMarginRight, textViewMarginTop, 0);
        // textViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, frameLayout.getId());
        //endregion

        //region Create TextView for character of cast
        TextView characterTextView = new TextView(getApplicationContext());
        characterTextView.setText(cast.getCharacter());
        characterTextView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
        characterTextView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        characterTextView.setMaxLines(1);
        characterTextView.setEllipsize(TextUtils.TruncateAt.END);

        // Set margins for textView
        int characterTextViewMarginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        int characterTextViewMarginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int characterTextViewMarginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall);

        // Create params for textView
        RelativeLayout.LayoutParams characterTextViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        characterTextViewLayoutParams.setMargins(characterTextViewMarginLeft, characterTextViewMarginRight, 0, 0);
        characterTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, frameLayout.getId());
        characterTextViewLayoutParams.addRule(RelativeLayout.BELOW, textView.getId());

        //endregion


        // Add frameLayout with playButton into trailerItem
        castItem.addView(frameLayout);
        // Add trailer name into trailerItem
        castItem.addView(textView, textViewLayoutParams);
        castItem.addView(characterTextView, characterTextViewLayoutParams);

        // Add trailerItem into Layout
        castLayout.addView(castItem);
    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {
        List<MovieDetails> movies = (List<MovieDetails>) resultList;
        cover_.setImageDrawable(Util.getDrawable(movies.get(0).getCover()));
        cover_.setVisibility(View.VISIBLE);

        helper_.addMovieDetails(movies.get(0));

        collapsingToolbarLayout_.setTitle(movies.get(0).getTitle());
    }



    @Override
    public void UpdateTrailers(List<Trailer> trailers) {
    }

    @Override
    public void UpdateCasts(List<? extends CommonMovie> casts) {
        List<Cast> castList = (List<Cast>) casts;
        for (int i = 0; i < casts.size(); i++) {
            helper_.addCast(castList.get(i),selectedMovieId_);
            createCastItem(castList.get(i));
        }
    }


    @Override
    public void onErrorRaised(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
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

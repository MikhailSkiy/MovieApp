package com.example.admin.moviesapp.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.ViewServer;
import com.example.admin.moviesapp.adapters.MovieDetailsViewPager;
import com.example.admin.moviesapp.adapters.TrailersAdapter;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.events.SuccessfullAlert;
import com.example.admin.moviesapp.events.UpdateMovieDetailsImageEvent;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.SharedPrefUtil;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.Movie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.ui.ScrollAwareFABBehavior;


import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity implements UpdateListener {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private long selectedMovieId_;
    private ImageView cover_;
    private DbHelper helper_ = new DbHelper(this);
    private boolean expanded = false;

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
    private RatingBar ratingBar_;
    private Dialog ratingDialog_;
    private RequestManager manager_;
    private MovieDetails movieDetails_;
    private double movieRatingGivenByUser_;
    private Button cancelBtn_;
    private TextView text_;

    private float offset1;
    private float offset2;
    private float offset3;


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

        // Get instance of RequestManger
        manager_ = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager_.init(this);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedMovieId_ = extras.getLong("movieId", 0);
        Timber.v(Long.toString(selectedMovieId_));

        cover_ = (ImageView) findViewById(R.id.coverImage);
        movieCard_ = (CardView) findViewById(R.id.movie_card);

        // Create new dialog
        setupRatingDialog();


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
        mAdapter = new MovieDetailsViewPager(getSupportFragmentManager(), this, selectedMovieId_);
        mPager = (ViewPager) findViewById(R.id.movie_details_view_pager);
        mPager.setAdapter(mAdapter);

        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        ViewServer.get(this).addWindow(this);
    }

    private void setupRatingBar(View v){
        ratingBar_ = (RatingBar)v.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable)ratingBar_.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        ratingBar_.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary_dark), PorterDuff.Mode.SRC_ATOP);

                movieRatingGivenByUser_ = rating;
                text_.setText(Double.toString(rating));
            }
        });

    }

    private void setupRatingDialog(){
        ratingDialog_ = new Dialog(MovieDetailsActivity.this);
        ratingDialog_.setContentView(R.layout.dialog_rate_movies);
        text_ = (TextView)ratingDialog_.findViewById(R.id.rating_value);

        cancelBtn_ = (Button) ratingDialog_.findViewById(R.id.btnSubmit);
        cancelBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar_.getRating();
                Toast.makeText(getApplicationContext(), "Your Selected Ratings  : " + String.valueOf(rating), Toast.LENGTH_LONG).show();
                text_.setText(Double.toString(rating));
                ratingDialog_.dismiss();
            }
        });

        ratingBar_ = (RatingBar)ratingDialog_.findViewById(R.id.ratingBar);
        ratingBar_.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                movieRatingGivenByUser_ = rating;
                text_.setText( Double.toString(2*rating));
            }
        });

    }


    private void setupFAB() {
        mainFloatingActionButton_ = (FloatingActionButton) findViewById(R.id.main_fab);
        // Init all subbuttons
        addToFavoriteFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.favorite_fab);
        addToWatchlistFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.watchlist_fab);
        addToListFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.list_fab);

        final ViewGroup fabContainer = (ViewGroup) findViewById(R.id.root_coordinator);

        // Mark current movie if user clicked on favorite button
        addToFavoriteFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks is the user loged in
                if (SharedPrefUtil.isUserLogedIn()) {
                    manager_.sendMessage(manager_.obtainMessage(States.MARK_AS_FAVORITE, movieDetails_.getId()));
                }
                // Otherwise send request for login
                else {
                    manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
                }

            }
        });

        // Add current movie into user watchlist when he ckicked to button
        addToWatchlistFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefUtil.isUserLogedIn()) {
                    manager_.sendMessage(manager_.obtainMessage(States.ADD_TO_WATCHLIST, movieDetails_.getId()));
                } else {
                    manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
                }
            }
        });

        // Rate current movie
        addToListFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //showRatingDialog();
               // setupDialog();
                ratingDialog_.show();
            }
        });

        mainFloatingActionButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                if (expanded) {
                    expandFab();
                } else {
                    collapseFab();
                }
            }
        });

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = mainFloatingActionButton_.getY() - addToListFloatingActionSubButton_.getY();

                addToListFloatingActionSubButton_.setTranslationY(offset1);
                offset2 = mainFloatingActionButton_.getY() - addToWatchlistFloatingActionSubButton_.getY();
                addToWatchlistFloatingActionSubButton_.setTranslationY(offset2);

                offset3 = mainFloatingActionButton_.getY() - addToFavoriteFloatingActionSubButton_.getY();
                addToFavoriteFloatingActionSubButton_.setTranslationY(offset3);
                return true;
            }
        });

    }

    private void collapseFab() {
        mainFloatingActionButton_.setImageResource(R.drawable.animated_minus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(addToListFloatingActionSubButton_, offset1),

                createCollapseAnimator(addToWatchlistFloatingActionSubButton_, offset2),
                createCollapseAnimator(addToFavoriteFloatingActionSubButton_, offset3));
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        mainFloatingActionButton_.setImageResource(R.drawable.animated_plus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(addToListFloatingActionSubButton_, offset1),
                createExpandAnimator(addToWatchlistFloatingActionSubButton_, offset2),
                createExpandAnimator(addToFavoriteFloatingActionSubButton_, offset3));
        animatorSet.start();
        animateFab();
    }

    private static final String TRANSLATION_Y = "translationY";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void animateFab() {
        Drawable drawable = mainFloatingActionButton_.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    // Show user that the "mark movie as favorite" request was succefully completed
    public void onEvent(SuccessfullAlert e){
        Timber.v("SuccessfullAlert");
        Toast.makeText(this,e.getAlertText(),Toast.LENGTH_LONG).show();
        // Change the image of button

        switch (Integer.parseInt(e.getAlertStatus())){
            case Constants.FAVORITE_SUCCESS_CODE :
                    addToFavoriteFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_heart));
                break;

            case Constants.WATCHLIST_SUCCESS_CODE :
                addToWatchlistFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_playlist_remove_grey));
                break;

            default:break;

        }

    }

    public void onEvent(UpdateMovieDetailsImageEvent e) {
        Timber.v("Event in activity");
        updateImage(e.getMovieDetails());
        movieDetails_ = e.getMovieDetails();
    }

    private void updateImage(MovieDetails movieDetails) {
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
            helper_.addCast(castList.get(i), selectedMovieId_);
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

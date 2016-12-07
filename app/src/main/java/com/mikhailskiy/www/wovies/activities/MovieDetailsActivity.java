package com.mikhailskiy.www.wovies.activities;

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
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.ViewServer;
import com.mikhailskiy.www.wovies.adapters.MovieDetailsViewPager;
import com.mikhailskiy.www.wovies.adapters.TrailersAdapter;
import com.mikhailskiy.www.wovies.database.DbHelper;
import com.mikhailskiy.www.wovies.events.RedirectionEvent;
import com.mikhailskiy.www.wovies.events.successfullResponse.SuccessfullAlert;
import com.mikhailskiy.www.wovies.events.UpdateMovieDetailsImageEvent;
import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.SharedPrefUtil;
import com.mikhailskiy.www.wovies.helpers.States;
import com.mikhailskiy.www.wovies.helpers.Util;
import com.mikhailskiy.www.wovies.interfaces.UpdateListener;
import com.mikhailskiy.www.wovies.managers.RequestManager;
import com.mikhailskiy.www.wovies.models.Cast;
import com.mikhailskiy.www.wovies.models.CommonMovie;
import com.mikhailskiy.www.wovies.models.MovieDetails;
import com.mikhailskiy.www.wovies.models.Trailer;


import java.util.ArrayList;
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
    private FloatingActionButton rateMovieFloatingActionSubButton_;
    private RatingBar ratingBar_;
    private Dialog ratingDialog_;
    private RequestManager manager_;
    private MovieDetails movieDetails_;
    private double movieRatingGivenByUser_;
    private Button cancelBtn_;
    private TextView text_;
    private long currentMovieId_;
    ProgressBar progressBar;

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

        // Set the progress bar for movie cover placeholder
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration(5000); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        // Create new dialog
        setupRatingDialog();
       // setupFAB();
       // setInitialFabState();


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


    private void setupRatingBar(View v) {
        ratingBar_ = (RatingBar) v.findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar_.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        ratingBar_.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.primary_dark), PorterDuff.Mode.SRC_ATOP);

                movieRatingGivenByUser_ = rating;
                //text_.setText(Double.toString(rating));
            }
        });

    }


    private void setupRatingDialog() {
        ratingDialog_ = new Dialog(MovieDetailsActivity.this);
        ratingDialog_.setContentView(R.layout.dialog_rate_movies);
        // text_ = (TextView)ratingDialog_.findViewById(R.id.rating_value);

        cancelBtn_ = (Button) ratingDialog_.findViewById(R.id.btnSubmit);
        cancelBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar_.getRating();
                movieRatingGivenByUser_ = rating * 2;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.rate_success) + String.valueOf(rating * 2), Toast.LENGTH_LONG).show();
                ratingDialog_.dismiss();

                List<String> attr = new ArrayList<String>();
                attr.add(String.valueOf(selectedMovieId_));
                attr.add(String.valueOf(movieRatingGivenByUser_));
                manager_.sendMessage(manager_.obtainMessage(States.RATE_MOVIE_REQUEST, attr));

            }
        });


        ratingBar_ = (RatingBar) ratingDialog_.findViewById(R.id.ratingBar);
        ratingBar_.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                movieRatingGivenByUser_ = rating;
                //text_.setText( Double.toString(2*rating));
            }
        });

    }

    // Sets initial state for all buttons in fab menu
    private void setInitialFabState() {
        setInitialFavoriteBtnState();
        setInitialWatchlistBtnState();
        setInitialRateBtnState();
    }

    // Sets initial state for Favorite button
    private void setInitialFavoriteBtnState() {
        if (helper_.isMovieIsFavorite(selectedMovieId_)) {
            setFullIconForFavoriteBtn();
        } else {
            setOutlineIconForFavoriteBtn();
        }
    }

    // Sets initial state for Watchlist button
    private void setInitialWatchlistBtnState() {
        if (helper_.isMovieInWatchlist(selectedMovieId_)) {
            setRemoveIconForWatchlistBtn();
        } else {
            setPlusIconForWatchlistBtn();
        }
    }

    private void setInitialRateBtnState() {
        if (helper_.isRatingGivenByUserExist(selectedMovieId_)) {

        } else {
            setFullIconForRateBtn();
        }
    }


//    private void setupFAB() {
//        mainFloatingActionButton_ = (FloatingActionButton) findViewById(R.id.main_fab);
//        // Init all subbuttons
//        addToFavoriteFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.favorite_fab);
//        addToWatchlistFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.watchlist_fab);
//        rateMovieFloatingActionSubButton_ = (FloatingActionButton) findViewById(R.id.list_fab);
//
//        final ViewGroup fabContainer = (ViewGroup) findViewById(R.id.root_coordinator);
//
//
//        // Mark current movie if user clicked on favorite button
//        addToFavoriteFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Checks is the user loged in
//                if (SharedPrefUtil.isUserLogedIn()) {
//                    // If there are no such movie in favorite list, add it. Otherwise delete it.
//                    if (!helper_.isMovieIsFavorite(movieDetails_.getId())) {
//                        manager_.sendMessage(manager_.obtainMessage(States.MARK_AS_FAVORITE, selectedMovieId_));
//                    } else {
//                        manager_.sendMessage(manager_.obtainMessage(States.DELETE_FROM_FAVORITS, selectedMovieId_));
//                    }
//                }
//                // Otherwise send request for login
//                else {
//                    manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
//                }
//
//            }
//        });
//
//        // Add current movie into user watchlist when he ckicked to button
//        addToWatchlistFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (SharedPrefUtil.isUserLogedIn()) {
//                    // If there are no such movie in watchlist, add it. Otherwise delete it
//                    if (!helper_.isMovieInWatchlist(movieDetails_.getId())) {
//                        manager_.sendMessage(manager_.obtainMessage(States.ADD_TO_WATCHLIST, selectedMovieId_));
//                    } else {
//                        manager_.sendMessage(manager_.obtainMessage(States.DELETE_FROM_WATCHLIST, selectedMovieId_));
//                    }
//                } else {
//                    manager_.sendMessage(manager_.obtainMessage(States.LOGIN_REQUEST));
//                }
//            }
//        });
//
//        // Rate current movie
//        rateMovieFloatingActionSubButton_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO ADD LOGIN FOR RATING
//                ratingDialog_.show();
//            }
//        });
//
//        mainFloatingActionButton_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expanded = !expanded;
//                if (expanded) {
//                    expandFab();
//                } else {
//                    collapseFab();
//                }
//            }
//        });
//
//        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
//                offset1 = mainFloatingActionButton_.getY() - rateMovieFloatingActionSubButton_.getY();
//
//                rateMovieFloatingActionSubButton_.setTranslationY(offset1);
//                offset2 = mainFloatingActionButton_.getY() - addToWatchlistFloatingActionSubButton_.getY();
//                addToWatchlistFloatingActionSubButton_.setTranslationY(offset2);
//
//                offset3 = mainFloatingActionButton_.getY() - addToFavoriteFloatingActionSubButton_.getY();
//                addToFavoriteFloatingActionSubButton_.setTranslationY(offset3);
//                return true;
//            }
//        });
//
//    }

    private void collapseFab() {
        mainFloatingActionButton_.setImageResource(R.drawable.animated_minus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(rateMovieFloatingActionSubButton_, offset1),

                createCollapseAnimator(addToWatchlistFloatingActionSubButton_, offset2),
                createCollapseAnimator(addToFavoriteFloatingActionSubButton_, offset3));
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        mainFloatingActionButton_.setImageResource(R.drawable.animated_plus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(rateMovieFloatingActionSubButton_, offset1),
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


    // Methods for changing button state visually
    private void setFullIconForFavoriteBtn() {
        getButtonIcon(R.drawable.ic_heart);
        //addToFavoriteFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_heart));
    }

    private void setOutlineIconForFavoriteBtn() {
        getButtonIcon(R.drawable.ic_heart_outline_grey);
        //addToFavoriteFloatingActionSubButton_.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_outline_grey));
    }

    private void setRemoveIconForWatchlistBtn() {
        getButtonIcon(R.drawable.ic_playlist_remove_grey);
        //addToWatchlistFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_playlist_remove_grey));
    }

    private void setPlusIconForWatchlistBtn() {
        getButtonIcon(R.drawable.ic_playlist_plus_grey);
        //addToWatchlistFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_playlist_plus_grey));
    }

    private void setFullIconForRateBtn() {
        getButtonIcon(R.drawable.ic_star_grey600_24dp);
        //rateMovieFloatingActionSubButton_.setImageDrawable(getDrawable(R.drawable.ic_star_grey600_24dp));
    }

    private Drawable getButtonIcon(int iconId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(iconId, getApplicationContext().getTheme());
        } else {
            return getResources().getDrawable(iconId);
        }
    }

    public void onEvent(RedirectionEvent e) {
        Timber.v("Redirection Event");
        Intent loginIntent = new Intent(MovieDetailsActivity.this, LoginActivity.class);
        loginIntent.putExtra("link", e.getUrl());
        startActivity(loginIntent);
    }

    // Show user that the "mark movie as favorite" request was succefully completed
    public void onEvent(SuccessfullAlert e) {
        Timber.v("SuccessfullAlert");
        // Change the image of button
        switch (e.currentType) {
            case SuccessfullAlert.FavoriteMovieRequestType:
                // If movie was successfully inserted to favorites
                if (!helper_.isMovieIsFavorite(selectedMovieId_)) {
                    helper_.addMovieToFavorites(selectedMovieId_);
                    setFullIconForFavoriteBtn();
                    Toast.makeText(this, e.getAlertText(), Toast.LENGTH_LONG).show();
                } else {
                    // Delete from table if such id is exist
                    helper_.deleteFavoriteMovie(selectedMovieId_);
                    setOutlineIconForFavoriteBtn();
                    Toast.makeText(this, getResources().getString(R.string.deleted_from_favorits), Toast.LENGTH_LONG).show();
                }

                break;

            case SuccessfullAlert.WatchlistMovieRequestType:
                // Check is movie was succesfully added to watchlist
                if (!helper_.isMovieInWatchlist(selectedMovieId_)) {
                    helper_.addMovieToWatchlist(selectedMovieId_);
                    setRemoveIconForWatchlistBtn();
                    Toast.makeText(this, e.getAlertText(), Toast.LENGTH_LONG).show();
                } else {
                    // Delete from watchlist table if such id exist
                    helper_.deleteMovieFromWatchlist(selectedMovieId_);
                    setPlusIconForWatchlistBtn();
                    Toast.makeText(this, getResources().getString(R.string.deleted_from_watchlist), Toast.LENGTH_LONG).show();
                }

                break;

            case SuccessfullAlert.RateMovieRequestType:
                setFullIconForRateBtn();
                // Insert to database rating value
                helper_.updateMovieRatingGivenByUser(selectedMovieId_, movieRatingGivenByUser_);
                break;

            default:
                break;
        }
    }

    public void onEvent(UpdateMovieDetailsImageEvent e) {
        Timber.v("Event in activity");
        progressBar.clearAnimation();
        updateImage(e.getMovieDetails());
        movieDetails_ = e.getMovieDetails();
        currentMovieId_ = movieDetails_.getId();
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
//        List<MovieDetails> movies = (List<MovieDetails>) resultList;
//        cover_.setImageDrawable(Util.getDrawable(movies.get(0).getCover()));
//        cover_.setVisibility(View.VISIBLE);
//
//        helper_.addMovieDetails(movies.get(0));
//        collapsingToolbarLayout_.setTitle(movies.get(0).getTitle());
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
        //Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, createReview());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share text to..."));
        }

        return super.onOptionsItemSelected(item);
    }

    private String createReview() {
        String message;
        if (movieDetails_ != null) {
            message = getResources().getText(R.string.i_just_saw) +
                    movieDetails_.getTitle() +
                    getResources().getText(R.string.a_great_movie) +
                    getResources().getText(R.string.download_suggestion) +
                    getResources().getText(R.string.link);


        } else {
            message = getResources().getText(R.string.download_suggestion) +
                    getResources().getText(R.string.link).toString();
        }

        return message;
    }
}

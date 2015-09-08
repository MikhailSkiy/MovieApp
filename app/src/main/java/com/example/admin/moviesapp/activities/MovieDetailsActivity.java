package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.ViewServer;
import com.example.admin.moviesapp.adapters.TrailersAdapter;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;

import java.util.List;

import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity implements UpdateListener {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private long selectedMovieId_;
    private ImageView cover_;

    private CardView movieCard_;
    private TextView title_;
    private TextView description_;
    private TextView released_;
    private TextView runtime_;
    private TextView genres_;
    private TextView language_;

    private TrailersAdapter trailersAdapter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Lalal");
        supportPostponeEnterTransition();
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get selected movie Id
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        selectedMovieId_ = extras.getLong("movieId", 0);
        Timber.v(Long.toString(selectedMovieId_));

        // Send MovieDetailsRequest
        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init(this);
        manager.sendMessage(manager.obtainMessage(States.MOVIES_DETAILS_REQUEST, selectedMovieId_));
        manager.sendMessage(manager.obtainMessage(States.TRAILERS_REQUEST, selectedMovieId_));
        manager.sendMessage(manager.obtainMessage(States.CASTS_REQUEST,selectedMovieId_));

        cover_ = (ImageView) findViewById(R.id.coverImage);

         movieCard_ = (CardView) findViewById(R.id.movie_card);
         title_ = (TextView) findViewById(R.id.title);
         description_ = (TextView) findViewById(R.id.description);
        runtime_ = (TextView)findViewById(R.id.runtime_value);
        genres_ = (TextView)findViewById(R.id.genre_value);
        language_ = (TextView)findViewById(R.id.language_value);
        released_ = (TextView) findViewById(R.id.released_value);

        String itemTitle = "Item Name";
        collapsingToolbarLayout_ = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbarLayout_.setTitle(itemTitle);
        collapsingToolbarLayout_.setExpandedTitleColor(getResources().getColor(R.color.background_floating_material_dark));
        collapsingToolbarLayout_.setCollapsedTitleTextColor(getResources().getColor(R.color.accent_material_dark));

        ViewServer.get(this).addWindow(this);
    }

    private void createTrailerItem(Trailer trailer){

        //region Create Trailer Layout
        // Find Trailer Layout
        LinearLayout trailerLayout = (LinearLayout) findViewById(R.id.trailers_layout);
        //endregion

        //region Create trailer item (PlayBtn TrailerName)
        // And set params
        RelativeLayout trailerItem = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams trailerItemParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        trailerItemParams.setMargins(0, 0, 0, 16);
        //endregion

        //region Create FrameLayout and set params
        // Set size for FrameLayout width and height
        int frameLayoutSize = getResources().getDimensionPixelOffset(R.dimen.frame_layout_size);
        // Create FrameLayout width size (width and height)
        FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        frameLayout.setId(R.id.frameId);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(frameLayoutSize, frameLayoutSize);

        // Set margins for FrameLayout
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_large);
        int marginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
        frameParams.setMargins(marginLeft, marginTop, marginRight, 0);
        //endregion

        //region Create PlayButton
        final Button playButton = new Button(getApplicationContext());
        playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play_big));
        playButton.setTag(trailer.getKey());
        // Set onClickListener
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser((String)playButton.getTag());
            }
        });
        //endregion

        // Add PlayButton into FrameLayout with appropriate params (margins etc)
        frameLayout.addView(playButton,frameParams);

        //region Create TextView for Trailer name
        TextView textView = new TextView(getApplicationContext());
        textView.setText(trailer.getName());
        textView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
        textView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        // Set margins for textView
        int textViewMarginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        int textViewMarginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int textViewMarginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        // Create params for textView
        RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        textViewLayoutParams.setMargins(textViewMarginLeft, textViewMarginRight, textViewMarginTop, 0);
        textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF,frameLayout.getId());
        //endregion

        // Add frameLayout with playButton into trailerItem
        trailerItem.addView(frameLayout);
        // Add trailer name into trailerItem
        trailerItem.addView(textView, textViewLayoutParams);

        // Add trailerItem into Layout
        trailerLayout.addView(trailerItem);
    }

    private void createCastItem(Cast cast){

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
                Intent intent = new Intent(MovieDetailsActivity.this,CastDetailsActivity.class);
                intent.putExtra("castId",castProfileImage.getTag().toString());
                startActivity(intent);
            }
        });
        //endregion

        // Add PlayButton into FrameLayout with appropriate params (margins etc)
        frameLayout.addView(castProfileImage,frameParams);



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
        textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF,frameLayout.getId());
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
        characterTextViewLayoutParams.addRule(RelativeLayout.RIGHT_OF,frameLayout.getId());
        characterTextViewLayoutParams.addRule(RelativeLayout.BELOW, textView.getId());

        //endregion


        // Add frameLayout with playButton into trailerItem
        castItem.addView(frameLayout);
        // Add trailer name into trailerItem
        castItem.addView(textView, textViewLayoutParams);
        castItem.addView(characterTextView,characterTextViewLayoutParams);

        // Add trailerItem into Layout
        castLayout.addView(castItem);
    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {
        List<MovieDetails> movies = (List<MovieDetails>) resultList;
        cover_.setImageDrawable(Util.getDrawable(movies.get(0).getCover()));
        cover_.setVisibility(View.VISIBLE);

        collapsingToolbarLayout_.setTitle(movies.get(0).getTitle());
        title_.setText(movies.get(0).getTitle());
        description_.setText(movies.get(0).getOverview());

        released_.setText(Util.getUIFriendlyData(movies.get(0).getReleaseDate()));
        runtime_.setText(Util.getUserFriendlyRuntime(Integer.toString(movies.get(0).getRuntime()), this));
        genres_.setText(Util.getGenres(movies.get(0).getGenres()));
        language_.setText(Util.getUserFriendlyOrLanguage(movies.get(0).getOriginalLanguage(),this));
    }

    private void openBrowser(String key){
        Uri url = createYoutubeUrl(key);
        Intent intent = new Intent(Intent.ACTION_VIEW, url);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else {
            Timber.v("Couldn't call because no receiving apps installed!");
        }
    }

    private Uri createYoutubeUrl(String key){
        String BaseUrl = "http://www.youtube.com/watch";
        Uri url = Uri.parse(BaseUrl).buildUpon()
                .appendQueryParameter("v",key)
                .build();
        return url;
    }

    @Override
    public void UpdateTrailers(List<Trailer> trailers){
        for (int i=0;i<trailers.size();i++){
            createTrailerItem(trailers.get(i));
        }
    }

    @Override
    public void UpdateCasts(List<? extends CommonMovie> casts){
        List<Cast> castList = (List<Cast>)casts;
        for (int i=0;i<casts.size();i++){
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

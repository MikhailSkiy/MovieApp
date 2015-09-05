package com.example.admin.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import com.example.admin.moviesapp.adapters.TrailersAdapter;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.UpdateListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.CommonMovie;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Video;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MovieDetailsActivity extends AppCompatActivity implements UpdateListener {

    private CollapsingToolbarLayout collapsingToolbarLayout_;
    private long selectedMovieId_;
    private ImageView cover_;
    private ImageView cover2_;
    private CardView movieCard_;
    private TextView title_;
    private TextView description_;

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

        cover_ = (ImageView) findViewById(R.id.coverImage);

         movieCard_ = (CardView) findViewById(R.id.movie_card);
         title_ = (TextView) findViewById(R.id.title);
         description_ = (TextView) findViewById(R.id.description);

        String itemTitle = "Item Name";
        collapsingToolbarLayout_ = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbarLayout_.setTitle(itemTitle);
        collapsingToolbarLayout_.setExpandedTitleColor(getResources().getColor(R.color.background_floating_material_dark));
        collapsingToolbarLayout_.setCollapsedTitleTextColor(getResources().getColor(R.color.accent_material_dark));

        // Trailers adapter
        List<Video> trailers = new ArrayList<>();
        Video trailer = new Video();
        trailer.setName("Traile Name");
        trailer.setKey("sdsd");


        Video trailer1 = new Video();
        trailer.setName("Traile Name2");
        trailer.setKey("sdsd");


        Video trailer2 = new Video();
        trailer.setName("Traile Name3");
        trailer.setKey("sdsd");

        trailers.add(trailer);
        trailers.add(trailer1);
        trailers.add(trailer2);

        LinearLayout trailerLayout = (LinearLayout) findViewById(R.id.trailers_layout);

        LinearLayout trailerItem = new LinearLayout(getApplicationContext());
        trailerItem.setOrientation(LinearLayout.HORIZONTAL);


        int width = 50;
        int height = 50;
        FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        FrameLayout.LayoutParams frameParams =
                new FrameLayout.LayoutParams(width, height);

        // Set margins for FrameLayout
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_large);
        int marginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        frameParams.setMargins(marginLeft, marginTop, marginRight, 0);

        // Create PlayButton
        Button playButton = new Button(getApplicationContext());
        playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play_mid));

        frameLayout.addView(playButton,frameParams);


        FrameLayout.LayoutParams frameParams2 =
                new FrameLayout.LayoutParams(width, height);

        // Set margins for FrameLayout
        int marginLeft2 = getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        int marginRight2 = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int marginTop2 = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        frameParams2.setMargins(marginLeft2, marginTop2, marginRight2, 0);



        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Text1");

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);




        frameLayout.addView(textView,layoutParams);

        trailerLayout.addView(frameLayout);


//        RecyclerView trailerRecyclerView = (RecyclerView)findViewById(R.id.trailer_list);
//        trailersAdapter_ = new TrailersAdapter(trailers, R.layout.item_trailer, this, new PlayBtnClickListener() {
//            @Override
//            public void onPlayTrailerClick(String trailerId) {
//                Toast.makeText(getApplicationContext(),"Hi Play Trailer",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        trailerRecyclerView.setAdapter(trailersAdapter_);
//        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onUpdate(List<? extends CommonMovie> resultList) {

        List<MovieDetails> movies = (List<MovieDetails>) resultList;
        cover_.setImageDrawable(Util.getDrawable(movies.get(0).getCover()));
        cover_.setVisibility(View.VISIBLE);

        collapsingToolbarLayout_.setTitle(movies.get(0).getOriginalTitle());
        title_.setText(movies.get(0).getOriginalTitle());
        description_.setText(movies.get(0).getOverview());

    }

    @Override
    public void onErrorRaised(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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

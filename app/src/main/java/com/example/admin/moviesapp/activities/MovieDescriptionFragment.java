package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.events.UpdateMovieDescriptionUI;
import com.example.admin.moviesapp.events.UpdateMovieDetailsImageEvent;
import com.example.admin.moviesapp.events.UpdateMovieTrailersUI;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.MovieDetails;
import com.example.admin.moviesapp.models.Trailer;
import com.example.admin.moviesapp.network.NetworkOperations;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDescriptionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static final java.lang.String ARG_PAGE = "arg_page";

    private TextView tagline_;
    private TextView description_;
    private TextView released_;
    private TextView runtime_;
    private TextView genres_;
    private TextView language_;
    private long movieId = 0;
    private DbHelper helper_;
    private CardView emptyCard_;
    private CardView titleCard_;
    private CardView detailsCard_;
    private CardView tvCard_;


    public static MovieDescriptionFragment newInstance(int pageNumbaer) {
        MovieDescriptionFragment fragment = new MovieDescriptionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumbaer + 1);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        movieId = args.getLong("Id");
        // Register EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int pageNumber = getArguments().getInt(ARG_PAGE);
        View view = inflater.inflate(R.layout.fragment_movie_description, container, false);
        tagline_ = (TextView) view.findViewById(R.id.movie_tagline);
        description_ = (TextView) view.findViewById(R.id.movie_description_text);
        runtime_ = (TextView) view.findViewById(R.id.runtime_value);
        genres_ = (TextView) view.findViewById(R.id.genre_value);
        language_ = (TextView) view.findViewById(R.id.language_value);
        released_ = (TextView) view.findViewById(R.id.released_value);

        emptyCard_ = (CardView) view.findViewById(R.id.empty_card);
        titleCard_ = (CardView) view.findViewById(R.id.title_card);
        detailsCard_ = (CardView) view.findViewById(R.id.details_card);
        tvCard_ = (CardView) view.findViewById(R.id.tv_card);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init((MovieDetailsActivity) getActivity());

        helper_ = new DbHelper(getActivity());
        // Get Movie details Object
        MovieDetails movieDetails = new MovieDetails();
        movieDetails = helper_.getMovieDetails(movieId);
        if (movieDetails != null) {
            Timber.v(movieDetails.getOriginalTitle());
            updateMovieDescription(movieDetails);
        } else {
            manager.sendMessage(manager.obtainMessage(States.MOVIES_DETAILS_REQUEST, movieId));
        }

        // Get trailers
        List<Trailer> trailerDetails = new ArrayList<>();
        trailerDetails = helper_.getAllTrailers(movieId);
        if (trailerDetails.size() != 0) {
            Timber.v("Trailers in database");
            for (Trailer t : trailerDetails) {
                updateMovieTrailers(t);
            }
        } else {
            manager.sendMessage(manager.obtainMessage(States.TRAILERS_REQUEST, movieId));
        }

//        // Get cast
//        List<Cast> castList = new ArrayList<>();
//        castList = helper_.getAllCast(movieId);
//        if (castList.size() != 0){
//            Timber.v("Casts in database");
//            UpdateCasts(castList);
//        } else {
//            manager.sendMessage(manager.obtainMessage(States.CASTS_REQUEST, selectedMovieId_));
//        }


    }

    public void onEvent(UpdateMovieDescriptionUI e) {
        updateMovieDescription(e.getMovieDescription());
    }

    private void updateMovieDescription(MovieDetails result) {
        hideEmptyCard();
        makeCardsVisible();
        MovieDetails movie = (MovieDetails) result;
        helper_.addMovieDetails(movie);
        movieId = movie.getId();
        helper_.addMovieDetails(movie);
        tagline_.setText(movie.getTagline());
        description_.setText(movie.getOverview());

        released_.setText(Util.getUIFriendlyData(movie.getReleaseDate()));
        runtime_.setText(Util.getUserFriendlyRuntime(Integer.toString(movie.getRuntime()), getActivity()));
        genres_.setText(Util.getGenres(movie.getGenres()));
        language_.setText(Util.getUserFriendlyOrigLanguage(movie.getOriginalLanguage(), getActivity()));

        // Sent to the activity event for updating cover ad title
        EventBus.getDefault().post(new UpdateMovieDetailsImageEvent(movie));
    }

    private void makeCardsVisible() {
        titleCard_.setVisibility(View.VISIBLE);
        detailsCard_.setVisibility(View.VISIBLE);
        tvCard_.setVisibility(View.VISIBLE);
    }

    private void hideEmptyCard() {
        emptyCard_.setVisibility(View.GONE);
    }

    public void onEvent(UpdateMovieTrailersUI e) {
        updateMovieTrailers(e.getMovieTrailer());
    }

    public void updateMovieTrailers(Trailer trailer) {
        // TODO change movieId and get it from Activity
        helper_.addTrailer(trailer, movieId);
        createTrailerItem(trailer);
    }

    private void createTrailerItem(Trailer trailer) {

        //region Create Trailer Layout
        // Find Trailer Layout
        LinearLayout trailerLayout = (LinearLayout) getActivity().findViewById(R.id.trailers_layout);
        //endregion

        //region Create trailer item (PlayBtn TrailerName)
        // And set params
        RelativeLayout trailerItem = new RelativeLayout(getActivity().getApplicationContext());
        RelativeLayout.LayoutParams trailerItemParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        trailerItemParams.setMargins(0, 0, 0, 16);
        //endregion

        //region Create FrameLayout and set params
        // Set size for FrameLayout width and height
        int frameLayoutSize = getResources().getDimensionPixelOffset(R.dimen.frame_layout_size);
        // Create FrameLayout width size (width and height)
        FrameLayout frameLayout = new FrameLayout(getActivity().getApplicationContext());
        frameLayout.setId(R.id.frameId);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(frameLayoutSize, frameLayoutSize);

        // Set margins for FrameLayout
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.spacing_large);
        int marginRight = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
        frameParams.setMargins(marginLeft, marginTop, marginRight, 0);
        //endregion

        //region Create PlayButton
        final Button playButton = new Button(getActivity().getApplicationContext());
        playButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_play_big));
        playButton.setTag(trailer.getKey());
        // Set onClickListener
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser((String) playButton.getTag());
            }
        });
        //endregion

        // Add PlayButton into FrameLayout with appropriate params (margins etc)
        frameLayout.addView(playButton, frameParams);

        //region Create TextView for Trailer name
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setText(trailer.getName());
        textView.setTextAppearance(getActivity().getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
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
        textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, frameLayout.getId());
        //endregion

        // Add frameLayout with playButton into trailerItem
        trailerItem.addView(frameLayout);
        // Add trailer name into trailerItem
        trailerItem.addView(textView, textViewLayoutParams);

        // Add trailerItem into Layout
        trailerLayout.addView(trailerItem);
    }

    private void openBrowser(String key) {
        Uri url = createYoutubeUrl(key);
        Intent intent = new Intent(Intent.ACTION_VIEW, url);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Timber.v("Couldn't call because no receiving apps installed!");
        }
    }

    private Uri createYoutubeUrl(String key) {
        String BaseUrl = "http://www.youtube.com/watch";
        Uri url = Uri.parse(BaseUrl).buildUpon()
                .appendQueryParameter("v", key)
                .build();
        return url;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

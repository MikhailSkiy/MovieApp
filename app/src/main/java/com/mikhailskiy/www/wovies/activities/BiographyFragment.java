package com.mikhailskiy.www.wovies.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.events.UpdateCastDetailsUI;
import com.mikhailskiy.www.wovies.managers.AppController;
import com.mikhailskiy.www.wovies.models.CastDetails;

import de.greenrobot.event.EventBus;
import timber.log.Timber;


public class BiographyFragment extends Fragment {

    public static final java.lang.String ARG_PAGE = "arg_page";

    private TextView bioDescription_;
    private ImageView profileImage_;
    private Tracker mTracker;

    public BiographyFragment() {
    }

    public static BiographyFragment newInstance(int pageNumber) {
        BiographyFragment myFragment = new BiographyFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber + 1);
        myFragment.setArguments(arguments);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        // Register EventBus
        EventBus.getDefault().register(this);
        AppController application = (AppController) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int pageNumber = arguments.getInt(ARG_PAGE);
//            RecyclerView recyclerView = new RecyclerView(getActivity());
//            recyclerView.setAdapter(new CastRecyclerViewAdapter(getActivity()));
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View view = inflater.inflate(R.layout.fragment_biography,container,false);
        bioDescription_ = (TextView)view.findViewById(R.id.biography_description);
        return view;
    }

    public void onEvent(UpdateCastDetailsUI e){
        updateCastDetailsInfo(e.getCastDetails());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("Setting screen name: " + "Biography fragment");
        mTracker.setScreenName("Screen name" + "Biography fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void updateCastDetailsInfo(CastDetails castDetails){
        bioDescription_.setText(castDetails.getBiography());
    }

    private void updateImage(byte[] image){

    }
}

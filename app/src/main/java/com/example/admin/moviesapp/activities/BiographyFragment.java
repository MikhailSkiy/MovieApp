package com.example.admin.moviesapp.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.events.UpdateCastDetailsUI;
import com.example.admin.moviesapp.models.CastDetails;

import de.greenrobot.event.EventBus;


public class BiographyFragment extends Fragment {

    public static final java.lang.String ARG_PAGE = "arg_page";

    private TextView bioDescription_;
    private ImageView profileImage_;

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

    private void updateCastDetailsInfo(CastDetails castDetails){
        bioDescription_.setText(castDetails.getBiography());
    }

    private void updateImage(byte[] image){

    }
}

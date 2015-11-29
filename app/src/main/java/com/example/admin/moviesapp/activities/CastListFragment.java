package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.adapters.CastListRecyclerViewAdapter;
import com.example.admin.moviesapp.database.DbHelper;
import com.example.admin.moviesapp.events.UpdateCastListEvent;
import com.example.admin.moviesapp.helpers.States;
import com.example.admin.moviesapp.interfaces.ExploreMoreBtnListener;
import com.example.admin.moviesapp.managers.RequestManager;
import com.example.admin.moviesapp.models.Cast;
import com.example.admin.moviesapp.ui.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class CastListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static final String ARG_PAGE = "arg_page";

    private CastListRecyclerViewAdapter adapter_;
    private RecyclerView listView_;
    private TextView name_;
    private TextView character_;
    private ImageView avatar_;
    private List<Cast> castList_ = new ArrayList<>();
    private DbHelper helper_;
    private long movieId_;

    public static CastListFragment newInstance(int pageNaumber) {
        CastListFragment fragment = new CastListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNaumber + 1);
        fragment.setArguments(args);
        return fragment;
    }

    public CastListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register EventBus
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            movieId_ = getArguments().getLong("Id");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get instance of RequestManger
        RequestManager manager = RequestManager.getInstance();
        // Initialize it by UpdateListener
        manager.init((MovieDetailsActivity) getActivity());

        helper_ = new DbHelper(getActivity());

        List<Cast> castList = new ArrayList<>();
        castList = helper_.getAllCast(movieId_);
        if (castList.size() != 0) {
            Timber.v("Casts in database");
            for (Cast c : castList) {
                updateList(c);
            }
        } else {
            manager.sendMessage(manager.obtainMessage(States.CASTS_REQUEST, movieId_));
        }
    }

    private void updateList(Cast cast) {
        castList_.add(cast);
        adapter_.notifyDataSetChanged();
    }

    public void onEvent(UpdateCastListEvent e) {
        updateList(e.getCast());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int pageNumber = arguments.getInt(ARG_PAGE);
        adapter_ = new CastListRecyclerViewAdapter(castList_, R.layout.item_cast_list, getActivity(), new ExploreMoreBtnListener() {
            @Override
            public void onCastItemClick(long castId) {
                Intent intent = new Intent(getActivity(), CastDetailsActivity.class);
                intent.putExtra("castId", Long.toString(castId));
                startActivity(intent);
            }
        });


        View view = inflater.inflate(R.layout.fragment_cast_list, container, false);
        listView_ = (RecyclerView) view.findViewById(R.id.cast_list);
        listView_.addItemDecoration(new CustomDividerItemDecoration(getResources()));
        listView_.setAdapter(adapter_);
        listView_.setItemAnimator(new DefaultItemAnimator());
        listView_.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

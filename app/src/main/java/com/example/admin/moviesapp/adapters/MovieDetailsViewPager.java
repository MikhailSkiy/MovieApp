package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.CastListFragment;
import com.example.admin.moviesapp.activities.MovieDescriptionFragment;

/**
 * Created by Mikhail on 18.09.2015.
 */
public class MovieDetailsViewPager extends FragmentPagerAdapter {

    Context context_;
    long movieId_;

    public MovieDetailsViewPager(FragmentManager fm,Context context,long id){
        super(fm);
        this.context_ = context;
        movieId_ = id;
    }

    @Override
    public Fragment getItem(int position){
        Bundle data = new Bundle();
        switch (position){
            case 0:
                MovieDescriptionFragment movieDescriptionFragment = MovieDescriptionFragment.newInstance(position);
                data.putLong("Id",movieId_);
                movieDescriptionFragment.setArguments(data);
                return movieDescriptionFragment;

            case 1:
                CastListFragment castListFragment = CastListFragment.newInstance(position);
                data.putLong("Id",movieId_);
                castListFragment.setArguments(data);
                return castListFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            /** Android tab is selected */
            case 0:
                title =  context_.getResources().getString(R.string.view_pager_movie_description_title);
                break;
            case 1:
                title = context_.getResources().getString(R.string.view_pager_cast_list_title);
                break;
            default:
                break;
        }
        return title;
    }

}

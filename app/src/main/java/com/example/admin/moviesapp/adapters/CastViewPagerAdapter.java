package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.admin.moviesapp.activities.BiographyFragment;
import com.example.admin.moviesapp.activities.CastDetailsActivity;
import com.example.admin.moviesapp.R;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastViewPagerAdapter extends FragmentStatePagerAdapter {

    Context context_;

    public CastViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context_ = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BiographyFragment biographyFragment = BiographyFragment.newInstance(position);
                return biographyFragment;

            case 1:
                CastDetailsActivity.MyFragment myFragment = CastDetailsActivity.MyFragment.newInstance(position);
                return myFragment;
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
                title =  context_.getResources().getString(R.string.view_pager_title_biography);
                break;
            case 1:
                title = context_.getResources().getString(R.string.view_pager_title_movies);
                break;
            default:
                break;
        }
        return title;
    }
}


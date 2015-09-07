package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.moviesapp.CastDetailsActivity;
import com.example.admin.moviesapp.R;

import java.util.ArrayList;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastViewPagerAdapter extends FragmentStatePagerAdapter {

    public CastViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CastDetailsActivity.MyFragment myFragment = CastDetailsActivity.MyFragment.newInstance(position);
        return myFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}


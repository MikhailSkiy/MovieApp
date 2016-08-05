package com.mikhailskiy.www.wovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.helpers.Constants;
import com.mikhailskiy.www.wovies.helpers.Util;
import com.mikhailskiy.www.wovies.models.MovieCredits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder> {
    private List<MovieCredits> list = new ArrayList<>();
    private LayoutInflater inflater;

    public static class CastViewHolder extends RecyclerView.ViewHolder {

        public TextView movieName;
        public TextView castCharacter;
        public ImageView movieCover;

        public CastViewHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movie_item_name);
            castCharacter = (TextView) itemView.findViewById(R.id.movie_item_character);
            movieCover = (ImageView) itemView.findViewById(R.id.movie_item_cover);
        }
    }

    public CastRecyclerViewAdapter(Context context, List<MovieCredits> values) {
        inflater = LayoutInflater.from(context);
        this.list = values;
    }


    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.item_cast_details, viewGroup, false);
        CastViewHolder holder = new CastViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(CastViewHolder yourRecyclerViewHolder, int i) {
        if (list.get(i).getCover() != null) {
            yourRecyclerViewHolder.movieCover.setImageBitmap(Util.getRoundedCroppedBitmap(Util.getBitmapFromBytes(list.get(i).getCover()), Constants.DEFAULT_CROPPING_RADIUS));
        }
        if (list.get(i).getTitle() != null) {
            yourRecyclerViewHolder.movieName.setText(list.get(i).getTitle());
        }
        if (list.get(i).getCharacter() != null) {
            yourRecyclerViewHolder.castCharacter.setText(list.get(i).getCharacter());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}


package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.models.MovieCredits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder> {
    private List<MovieCredits> list = new ArrayList<>();
    private LayoutInflater inflater;

    public static class CastViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public CastViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.movie_title);
            imageView = (ImageView) itemView.findViewById(R.id.coverImage);
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
            yourRecyclerViewHolder.imageView.setImageBitmap(Util.getRoundedCroppedBitmap(Util.getBitmapFromBytes(list.get(i).getCover()), Constants.DEFAULT_CROPPING_RADIUS));
        }
        if (list.get(i).getTitle() != null) {
            yourRecyclerViewHolder.textView.setText(list.get(i).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}


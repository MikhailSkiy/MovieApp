package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.activities.AboutUsActivity;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.CustomItemClickListener;
import com.example.admin.moviesapp.models.AboutItem;
import com.example.admin.moviesapp.models.Movie;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Mikhail on 29.11.15.
 */
public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.AboutUsViewHolder> {

    private static List<AboutItem> aboutItems_;
    private Context context_;
    private static CustomItemClickListener listener_;
    private int rowLayout_;

    public static class AboutUsViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public ImageView itemIcon;
        public CardView cardView_;

        public AboutUsViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.about_item_title);
            itemIcon = (ImageView) view.findViewById(R.id.about_item_icon);
            cardView_ = (CardView) view.findViewById(R.id.about_item_card);

            cardView_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        long itemId = aboutItems_.get(position).id;
                        if (listener_ != null) {
                            listener_.onCustomItemClick(itemId);
                        }
                    }
                }
            });

        }
    }


    public AboutUsAdapter(List<AboutItem> items, int rowLayout, Context context, CustomItemClickListener listener) {
        this.aboutItems_ = items;
        this.rowLayout_ = rowLayout;
        this.context_ = context;
        this.listener_ = listener;
    }

    @Override
    public AboutUsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("About" + "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout_, parent, false);
        return new AboutUsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AboutUsViewHolder viewHolder, int i) {
        Timber.d("About" + "onBindViewHolder");
        AboutItem item = aboutItems_.get(i);
        viewHolder.itemName.setText(item.getTitle());
        viewHolder.itemIcon.setImageDrawable(aboutItems_.get(i).getId());

    }

    @Override
    public int getItemCount() {
        Timber.d("About" + aboutItems_.size());
        return aboutItems_.size();
    }

}

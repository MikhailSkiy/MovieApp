package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.helpers.Constants;
import com.example.admin.moviesapp.helpers.Util;
import com.example.admin.moviesapp.interfaces.ExploreMoreBtnListener;
import com.example.admin.moviesapp.models.Cast;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 18.09.2015.
 */
public class CastListRecyclerViewAdapter extends RecyclerView.Adapter<CastListRecyclerViewAdapter.CastListViewHolder> {

    private static List<Cast> casts_;
    private int rowLayout_;
    private Context context_;
    private static ExploreMoreBtnListener exploreMoreBtnListener_;

    public static class CastListViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView character;
        public ImageView avatar;
        public RelativeLayout relativeLayout;

        public CastListViewHolder(View view){
            super(view);
            character = (TextView)view.findViewById(R.id.cast_item_character);
            name = (TextView)view.findViewById(R.id.cast_item_name);
            avatar = (ImageView)view.findViewById(R.id.cast_item_avatar);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.cast_list_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        long castId = casts_.get(position).getId();
                        if (exploreMoreBtnListener_ != null){
                            exploreMoreBtnListener_.onCastItemClick(castId);
                        }
                    }
                }
            });

        }
    }

    public CastListRecyclerViewAdapter(List<Cast> casts, int rowLayout, Context context,ExploreMoreBtnListener listener){
        this.casts_ = casts;
        this.rowLayout_ = rowLayout;
        this.context_ = context;
        this.exploreMoreBtnListener_ = listener;
    }

    @Override
    public CastListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout_,parent,false);
        return new CastListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastListViewHolder holder, int position) {
        Cast cast = casts_.get(position);
        holder.avatar.setImageBitmap( Util.getRoundedCroppedBitmap(Util.getBitmapFromBytes((casts_.get(position).getCover())), Constants.DEFAULT_CROPPING_RADIUS));
        holder.character.setText(casts_.get(position).getCharacter());
        holder.name.setText(casts_.get(position).getName());
    }

    @Override
    public int getItemCount(){
        return casts_.size();
    }
}

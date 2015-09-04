package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.interfaces.PlayBtnClickListener;
import com.example.admin.moviesapp.models.Video;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 04.09.2015.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private static List<Video> trailers_;
    private int rowLayout_;
    private Context context_;
    private static PlayBtnClickListener playBtnClickListener_;

    public static class TrailersViewHolder extends RecyclerView.ViewHolder{
        public TextView trailerName;
        public Button playTrailerBtn;

        public TrailersViewHolder(View view){
            super(view);
            trailerName = (TextView) view.findViewById(R.id.trailer_name);
            playTrailerBtn = (Button) view.findViewById(R.id.play_trailer_button);

//            playTrailerBtn.setOnClickListener();
        }
    }

    public TrailersAdapter(List<Video> trailers, int rowLayout, Context context, PlayBtnClickListener listener){
        this.trailers_ = trailers;
        this.rowLayout_ = rowLayout;
        this.context_ = context;
        this.playBtnClickListener_ = listener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout_,parent,false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder viewHolder, int i){
        Video trailer = trailers_.get(i);
        viewHolder.trailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount(){
        return trailers_.size();
    }
}

package com.mikhailskiy.www.wovies.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.interfaces.CustomItemClickListener;
import com.mikhailskiy.www.wovies.managers.AppController;
import com.mikhailskiy.www.wovies.models.Movie;

import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 01.09.2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static List<Movie> movies_;
    private int rowLayout_;
    private Context context_;
    private static CustomItemClickListener listener_;
    ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName;
        public ImageView movieCover;
        public CardView cardView_;
        public ImageView badgeView_;

        public MoviesViewHolder(View view) {
            super(view);
            movieName = (TextView) view.findViewById(R.id.movie_title);
            movieCover = (ImageView) view.findViewById(R.id.movie_cover);
            cardView_ = (CardView) view.findViewById(R.id.movie_card);
            badgeView_ = (ImageView) view.findViewById(R.id.movie_badge);

            cardView_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        long movieId = movies_.get(position).getId();
                        if (listener_ != null) {
                            listener_.onCustomItemClick(movieId);
                        }
                    }
                }
            });

        }
    }

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context, CustomItemClickListener listener) {
        this.movies_ = movies;
        this.rowLayout_ = rowLayout;
        this.context_ = context;
        this.listener_ = listener;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout_, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder viewHolder, int i) {
        Movie movie = movies_.get(i);
        viewHolder.movieName.setText(movie.getTitle());
        mImageLoader.get(movie.getImageUrl(), ImageLoader.getImageListener(viewHolder.movieCover, R.drawable.placeholder, R.drawable.placeholder));

//        if (movie.getVoteAverage() > 7.0) {
//            viewHolder.badgeView_.setVisibility(View.VISIBLE);
//        }
    }

    public void addMovie(Movie movie) {
        movies_.add(movie);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies_.size();
    }
}

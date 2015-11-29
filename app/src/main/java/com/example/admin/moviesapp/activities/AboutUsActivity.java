package com.example.admin.moviesapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.adapters.AboutUsAdapter;
import com.example.admin.moviesapp.adapters.MoviesAdapter;
import com.example.admin.moviesapp.interfaces.CustomItemClickListener;
import com.example.admin.moviesapp.models.AboutItem;
import com.example.admin.moviesapp.ui.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager_;
    private RecyclerView recyclerView_;
    private AboutUsAdapter aboutUsAdapter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        layoutManager_ = new LinearLayoutManager(this);
        recyclerView_ = (RecyclerView) findViewById(R.id.content);

        List<AboutItem> aboutItemList = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.about_titles);
        int[] links = getResources().getIntArray(R.array.about_ids);

        for (int i=0;i<titles.length;i++){
            aboutItemList.add(new AboutItem(titles[i],links[i]));
        }



        aboutUsAdapter_ = new AboutUsAdapter(aboutItemList, R.layout.item_about_us, this, new CustomItemClickListener() {
            @Override
            public void onCustomItemClick(final long movieId) {
//                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
//                intent.putExtra("movieId", movieId);
//                startActivity(intent);
            }
        });

        recyclerView_.setAdapter(aboutUsAdapter_);

        recyclerView_.setItemAnimator(new DefaultItemAnimator());
        recyclerView_.setLayoutManager(layoutManager_);

    }
}

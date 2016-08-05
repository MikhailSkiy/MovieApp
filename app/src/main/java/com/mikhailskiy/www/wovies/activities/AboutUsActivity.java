package com.mikhailskiy.www.wovies.activities;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.adapters.AboutUsAdapter;
import com.mikhailskiy.www.wovies.adapters.MoviesAdapter;
import com.mikhailskiy.www.wovies.interfaces.CustomItemClickListener;
import com.mikhailskiy.www.wovies.models.AboutItem;
import com.mikhailskiy.www.wovies.ui.RecyclerViewEmptySupport;

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
        int[] links = getResources().getIntArray(R.array.number_array);
        TypedArray icons = getResources().obtainTypedArray(R.array.about_ids);

        for (int i=0;i<titles.length-1;i++){
            aboutItemList.add(new AboutItem(links[i],titles[i],icons.getDrawable(i)));
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

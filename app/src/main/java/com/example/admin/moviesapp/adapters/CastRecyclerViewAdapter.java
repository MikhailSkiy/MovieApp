package com.example.admin.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.moviesapp.R;

import java.util.ArrayList;

/**
 * Created by Mikhail Valuyskiy on 07.09.2015.
 */
public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public static class CastViewHolder extends RecyclerView.ViewHolder {

      public TextView textView;

        public CastViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
        }
    }

    public CastRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list.add("A-Bomb (HAS)");
        list.add("A.I.M.");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");
        list.add("A-Bomb");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");

    }


    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.item_cast_details, viewGroup, false);
        CastViewHolder  holder = new CastViewHolder (root);
        return holder;
    }

    @Override
    public void onBindViewHolder(CastViewHolder  yourRecyclerViewHolder, int i) {
        yourRecyclerViewHolder.textView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}


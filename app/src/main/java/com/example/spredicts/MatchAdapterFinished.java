package com.example.spredicts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MatchAdapterFinished extends RecyclerView.Adapter<MatchAdapterFinished.MatchHolder> {
    private Context context;
    private List<Card> matchList;
    private DBHelper dbHelper = new DBHelper(this.context);
    private int count = 0;
    private SQLiteDatabase sqLiteDatabase;

    public MatchAdapterFinished(Context context, List<Card> matches) {
        this.context = context;
        matchList = matches;
    }

    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemfinished, parent, false);
        return new MatchHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        dbHelper = new DBHelper(recyclerView.getContext());
    }

    @Override //here in each card we check the user's prediction and compare it to the result
    public void onBindViewHolder(@NonNull MatchHolder holder, int position) {
        Card match = matchList.get(position);
        holder.result.setText(match.getResult());
        holder.homename.setText(match.getHomename());
        holder.awayname.setText(match.getAwayname());
        try {
            dbHelper.getReadableDatabase();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Predict p = dbHelper.getById(match.getId());
        if (p != null) {
            holder.tvAway.setText(String.valueOf(p.getAwayPredict()));
            holder.tvHome.setText(String.valueOf(p.getHomePredict()));
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder {
        TextView awayname, homename, result, tvHome, tvAway;
        ConstraintLayout constraintLayout;

        public MatchHolder(@NonNull View itemView) {
            super(itemView);

            result = itemView.findViewById(R.id.result);
            homename = itemView.findViewById(R.id.homenameee);
            awayname = itemView.findViewById(R.id.awaynameee);
            tvHome = itemView.findViewById(R.id.tvpredicthome);
            tvAway = itemView.findViewById(R.id.tvpredictaway);
            constraintLayout = itemView.findViewById(R.id.main_layFinished);
        }
    }
}

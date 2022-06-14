package com.example.spredicts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchAdapterScheduled extends RecyclerView.Adapter<MatchAdapterScheduled.MatchHolder> {
    private Context context;
    private List<Card> matchList;
    private DBHelper dbHelper = new DBHelper(this.context);
    public MatchAdapterScheduled(Context context , List<Card> matches){
        this.context = context;
        matchList = matches;
    }
    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item , parent , false);
        return new MatchHolder(view);
    }

    @Override //here we update the prediction in each card
    public void onBindViewHolder(@NonNull MatchHolder holder, int position) {
        Card match = matchList.get(position);
        holder.result.setText(match.getResult());
        holder.homename.setText(match.getHomename());
        holder.awayname.setText(match.getAwayname());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = holder.etHome.getText().toString();
                String a = holder.etAway.getText().toString();
                try {
                    int aa = Integer.parseInt(a);
                    int hh = Integer.parseInt(h);
                    Predict p = new Predict(match.getId(),hh,aa);
                    dbHelper.update(p);
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        dbHelper = new DBHelper(recyclerView.getContext());
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder{
        TextView awayname , homename , result, id;
        ConstraintLayout constraintLayout;
        public EditText etHome;
        public EditText etAway;
        public Button update;

    public MatchHolder(@NonNull View itemView){
        super(itemView);
        etHome = itemView.findViewById(R.id.ethomenave);
        etAway = itemView.findViewById(R.id.etawayyy);
        result = itemView.findViewById(R.id.result);
        homename = itemView.findViewById(R.id.homenameee);
        awayname = itemView.findViewById(R.id.awaynameee);
        update = itemView.findViewById(R.id.updatebt);

        constraintLayout = itemView.findViewById(R.id.main_lay);
    }
}
}

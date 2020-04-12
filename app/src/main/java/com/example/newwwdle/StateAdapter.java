package com.example.newwwdle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    String time[]; //用來存時間
    String state[]; //用來存狀態
    Context context;

    public StateAdapter(Context ct, String s1[], String s2[]) {
        context = ct;
        time = s1;
        state = s2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.student_state_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt1.setText(time[position]);
        holder.txt2.setText(state[position]);


    }

    @Override
    public int getItemCount() {
        return time.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt1, txt2;
        ConstraintLayout stateLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.dateView);
            txt2 = itemView.findViewById(R.id.stateView);
            stateLayout = itemView.findViewById(R.id.StateLayout);
        }
    }
}

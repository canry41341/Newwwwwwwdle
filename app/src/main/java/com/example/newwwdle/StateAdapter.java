package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    //顯示點名狀態的adapter
    String time[]; //用來存時間
    String state[]; //用來存狀態
    String CID;
    Context context;

    public StateAdapter(Context ct, String s1[], String s2[], String s3) {
        context = ct;
        time = s1;
        state = s2;
        CID = s3;
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
        if(Integer.parseInt(state[position]) == 1) {
            holder.txt2.setText("已簽到");
            holder.txt2.setTextColor(Color.GREEN);
        }else{
            holder.txt2.setText("未簽到");
            holder.txt2.setTextColor(Color.RED);
        }


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

            txt1 = itemView.findViewById(R.id.dateView); //點名時間
            txt2 = itemView.findViewById(R.id.stateView); //點名的狀態
            stateLayout = itemView.findViewById(R.id.StateLayout); //狀態欄的layout名稱
        }
    }

}

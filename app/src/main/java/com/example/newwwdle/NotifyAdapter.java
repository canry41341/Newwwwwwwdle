package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    //顯示通知欄的adapter
    String notify[]; //用來存通知本身的內容
    String date[]; //用來存所發通知的時間
    String MSG[];
    String CID;
    Context context;

    public NotifyAdapter(Context ct, String[] title/**/, String[] time, String[] msg, String class_id) {
        context = ct;
        notify = title;// title
        date = time; // time
        MSG = msg;
        CID = class_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notify_list, parent, false);
        return new NotifyAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.txt1.setText(notify[position]); // title
        holder.txt2.setText(date[position]); // time

        //看你是點選哪則通知
        holder.notifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowNotify.class);
                intent.putExtra("title", notify[position]);
                intent.putExtra("time", date[position]);
                intent.putExtra("msg", MSG[position]);
                intent.putExtra("CID",CID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notify.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt1, txt2;

        ConstraintLayout notifyLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.dateView); //顯示通知的時間
            txt2 = itemView.findViewById(R.id.textView2); // 顯示通知的內容
            notifyLayout = itemView.findViewById(R.id.notifyLayout);

        }
    }
}

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

public class TnotifyAdapter extends RecyclerView.Adapter<TnotifyAdapter.MyViewHolder> {

    String notify[]; //用來存通知本身的內容
    String date[]; //用來存所發通知的時間
    String MSG[];
    private Context context;
    int isPost = 1;

    public TnotifyAdapter(Context ct, String title[], String time[], String msg[]) {
        context = ct;
        notify = title;// title
        date = time; // time
        MSG = msg;
    }

    @NonNull
    @Override
    public TnotifyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.teacher_notify_layout, parent, false);

        return new TnotifyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TnotifyAdapter.MyViewHolder holder,final int position) {

        holder.myText1.setText(notify[position]);
        holder.myText2.setText(date[position]);

        //進入你點選的項目
        holder.teacherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowNotify.class);
                intent.putExtra("title", notify[position]);
                intent.putExtra("time", date[position]);
                intent.putExtra("msg", MSG[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notify.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2;
        ConstraintLayout teacherLayout; //get id of teacher_notify_list.xml

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.dateViewT);  //get class name
            myText2 = itemView.findViewById(R.id.textView2T);  //get class time
            teacherLayout = itemView.findViewById(R.id.notifyLayout_teacher);
        }
    }
}

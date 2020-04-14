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

    private String data1[], data2[];
    private Context context;
    int isPost = 1;

    public TnotifyAdapter(Context ct, String s1[], String s2[]) {
        data1 = s1;
        data2 = s2;
        context = ct;
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

        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);

        //進入你點選的項目
        holder.teacherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowNotify.class);
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.length;
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

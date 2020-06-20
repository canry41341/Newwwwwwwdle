package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.MyViewHolder> {

    String [][] data;
    String [] date; //用來存放要點選的日期
    String [] student;
    String [] check;
    String CID;
    Context context;

    public CheckAdapter(Context ct, String []s1, String[][] s2, String class_id) {
        context = ct;
        data = s2;
        date = s1;
        CID = class_id;
        student = new String[s2[0].length-1];
        for(int i = 0; i < student.length; i++){
            System.out.println("aaa: " + s2[0][i+1]);
            student[i] = s2[0][i+1];
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.teacher_check_date_list, parent, false);
        return new CheckAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {

        holder.dateTxt.setText(date[position]);

        //點選日期後 會進到對應日期的全班學生點名狀況
        holder.checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, TeacherState.class);
                Bundle bundle = new Bundle();
                bundle.putInt("today", date.length);
                bundle.putInt("choose", position+1);
                bundle.putString("date1",date[position]);
                bundle.putStringArray("student",student);
                check = new String[student.length];
                for(int i = 0; i < student.length; i++){

                    check[i] = data[position+1][i+1];
                }
                bundle.putStringArray("check",check);
                bundle.putString("CID",CID);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return date.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView dateTxt;
        LinearLayout checkLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTxt = itemView.findViewById(R.id.date_txt); //展示日期
            checkLayout = itemView.findViewById(R.id.check_layout); //點擊日期的layout名稱
        }
    }
}

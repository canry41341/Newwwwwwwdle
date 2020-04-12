package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Teacher_Adapter extends RecyclerView.Adapter<Teacher_Adapter.ViewHolder> {

        private List<String> mData;
        private String[] class_time;
        Context teacher_context;
        Teacher_Adapter(Context ct,List<String> data , String[] time) {
            mData = data;
            class_time = time;
            teacher_context = ct;
        }

        // 建立ViewHolder
        class ViewHolder extends RecyclerView.ViewHolder{
            // 宣告元件
            private TextView txtItem;
            private TextView classtime;
            LinearLayout teacher_layout;

            ViewHolder(View itemView) {
                super(itemView);
                txtItem = (TextView) itemView.findViewById(R.id.classname2);
                classtime = itemView.findViewById(R.id.classtimes);
                teacher_layout = itemView.findViewById(R.id.teacherlayout);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 連結項目布局檔list_item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.style_listview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 設置txtItem要顯示的內容
            holder.txtItem.setText(mData.get(position));
            holder.classtime.setText(class_time[position]);

            holder.teacher_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(teacher_context,Teacher_class.class);
                    intent.putExtra("data1", mData.get(position));
                    intent.putExtra("data2", class_time[position]);
                    teacher_context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
}


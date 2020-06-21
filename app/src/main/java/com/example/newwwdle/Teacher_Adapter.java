package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Teacher_Adapter extends RecyclerView.Adapter<Teacher_Adapter.ViewHolder> {

        private List<String> mData;
        private String[] class_time;
        private String[] CID;
        private long LastClickTime = System.currentTimeMillis();
        private long CLICK_TIME_INTERVAL = 1000;
        Context teacher_context;
        public static AlertDialog alertDialog;
        ProgressBar progressbar;

        Teacher_Adapter(Context ct,List<String> data , String[] time, String[] class_id) {
            mData = data;
            class_time = time;
            CID = class_id;
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
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // 設置txtItem要顯示的內容
            holder.txtItem.setText(mData.get(position));
            holder.classtime.setText(class_time[position]);
/*******************************data1=課程，data2=時間********************************/
            holder.teacher_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    if (now - LastClickTime < CLICK_TIME_INTERVAL){
                        return;
                    }
                    LastClickTime = now;

                    Toast.makeText(teacher_context, "進入中...", Toast.LENGTH_SHORT).show();
                    new ListTask().execute(position);
                }
            });
        }

    private class ListTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(teacher_context, R.style.CustomDialog);
            LayoutInflater inflater = LayoutInflater.from(teacher_context);
            View dialogView = inflater.inflate(R.layout.progress, null);
            attend_chooser.setView(dialogView);
            alertDialog = attend_chooser.create();
            progressbar = dialogView.findViewById(R.id.p_Bar);
            progressbar.setClickable(false);
            alertDialog.setCancelable(false);
            alertDialog.show();;

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer d){
            Intent intent = new Intent(teacher_context,Teacher_class.class);
            intent.putExtra("data1", mData.get(d));
            intent.putExtra("data2", class_time[d]);
            intent.putExtra("data3", CID[d]);
            teacher_context.startActivity(intent);
        }

    }
        @Override
        public int getItemCount() {
            return mData.size();
        }
}


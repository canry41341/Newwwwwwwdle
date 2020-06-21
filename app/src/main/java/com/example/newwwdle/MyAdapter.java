package com.example.newwwdle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String data1[]; //class name
    String data2[]; //class time
    String data3[]; //CID
    Context context;
    public static AlertDialog alertDialog;
    ProgressBar progressbar;
    private long LastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 1000;

    public MyAdapter(Context ct, String s1[], String s2[], String s3[]) {

        context = ct;
        data1 = s1;
        data2 = s2;
        data3 = s3;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.class_list, parent, false);

        return new MyViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        //alertDialog.dismiss();
/**************************data1 就是課程名稱，以此類推********************/
        //進入你點選的項目
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - LastClickTime < CLICK_TIME_INTERVAL){
                    return;
                }
                LastClickTime = now;

                Toast.makeText(context, "進入中...", Toast.LENGTH_SHORT).show();
                new ListTask().execute(position);

            }
        });
    }

    private class ListTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(context, R.style.CustomDialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.progress, null);
            attend_chooser.setView(dialogView);
            alertDialog = attend_chooser.create();
            progressbar = dialogView.findViewById(R.id.p_Bar);
            progressbar.setClickable(false);
            alertDialog.setCancelable(false);
            alertDialog.show();;

        }

        @Override
        protected void onPostExecute(Integer d){
            Intent intent = new Intent(context, SecondActivity.class);
            intent.putExtra("data1", data1[d]);
            intent.putExtra("data2", data2[d]);
            intent.putExtra("data3", data3[d]);
            context.startActivity(intent);
        }

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ConstraintLayout mainLayout; //get id of class_list.xml

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.myText1);  //get class name
            myText2 = itemView.findViewById(R.id.myText2);  //get class time
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

package com.example.newwwdle;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Teacher_State_Adapter extends RecyclerView.Adapter<Teacher_State_Adapter.MyViewHolder> {
    String time[]; //用來存時間
    String state[]; //用來存狀態
    String CID;
    int today, choose;
    Context context;
    Backend backend = new Backend();
    String result;
    AlertDialog alertDialog;
    ProgressBar progressbar;
    int parsing_integer;

    public Teacher_State_Adapter(Context ct, String s1[], String s2[], String class_id, int a, int b) {
        context = ct;
        time = s1;
        state = s2;
        CID = class_id;
        today = a;
        choose = b;
        if(Teacher_class.note == 1){
            Teacher_class.note = 0;
            TeacherCheck.act.finish();
        }
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.teacher_state_list, parent, false);

        return new Teacher_State_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {//學生點名狀態
        /*************那個位置的checkbox**************************/
        holder.txt1.setText(time[position]);
        holder.txt2.setText(state[position]);
        parsing_integer = position;
        /***********************************************************/
        if(state[position].compareTo("1") == 0) {
            holder.mcheckbox.setChecked(true);
        }else{
            holder.mcheckbox.setChecked(false);
        }
        if (today != choose){
            holder.mcheckbox.setEnabled(false);
        }


        holder.mcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /***************補簽到 我忘記要幹嘛了*********************/
                /*********************holder.mcheckbox.isChecked()是拿補簽到的，記得把資料傳回database***************/
                Log.d("MESSAGE",time[position]+"  "+holder.mcheckbox.isChecked());
                if(holder.mcheckbox.isChecked()){
                    new ListTask().execute("checked");
                }
                else{
                    new ListTask().execute("unchecked");
                }
            }
        });

    }

    private class ListTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(context, R.style.CustomDialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.progress, null);
            attend_chooser.setView(dialogView);
            alertDialog = attend_chooser.create();
            progressbar = dialogView.findViewById(R.id.p_Bar);
            alertDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            if(params[0].equals("checked")){
                result = backend.Communication(8,time[parsing_integer],CID);
            }else{
                result = backend.Communication(8,time[parsing_integer],CID);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void d){
            alertDialog.dismiss();
        }
    }

    @Override
    public int getItemCount() {
        return time.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt1, txt2;
        ConstraintLayout stateLayout;
        CheckBox mcheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.STUDID); //點名時間
            txt2 = itemView.findViewById(R.id.attend_state); //點名的狀態
            stateLayout = itemView.findViewById(R.id.StateLayout2); //狀態欄的layout名稱
            mcheckbox = itemView.findViewById(R.id.checkBox);
        }
    }
}

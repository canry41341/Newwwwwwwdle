package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StudentState extends AppCompatActivity {

    //這個class用的adapter是StateAdapter
    private String s1[]; //s1用來設定點名日期
    private String s2[]; //s2用來儲存當日點名狀態

    RecyclerView myRecyclerView;

    //
    private static Toast toast;

    private static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (toast == null) {
            //如果還沒有用過makeText方法，才使用
            toast = android.widget.Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }
    Backend backend = new Backend();
    String result;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_state);


        myRecyclerView = findViewById(R.id.stateRecyclerView);

        //
        result = backend.Communication(7,"ID1","CID1");
        //


        String[] tokens = result.split(";");
        makeTextAndShow(StudentState.this,tokens[0],toast.LENGTH_LONG);
        s1 = new String[tokens.length];
        s2 = new String[tokens.length];
        for (int i = 0; i<tokens.length; i++){
            String[] day_split = tokens[i].split("/");
            s1[i] = day_split[0].substring(5,6) + "月" + day_split[0].substring(7,9) + "號";
            s2[i] = day_split[1];
        }



        /*決定s1跟s2的來源
        s1 = getResources().getStringArray(R.array.date);
        s2 = getResources().getStringArray(R.array.state);
        */
        //設定adapter
        StateAdapter stateAdapter = new StateAdapter(this, s1, s2);
        myRecyclerView.setAdapter(stateAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

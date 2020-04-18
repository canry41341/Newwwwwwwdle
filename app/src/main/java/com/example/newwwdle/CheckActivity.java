package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CheckActivity extends AppCompatActivity {

    TextView dateTxt;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        dateTxt = findViewById(R.id.date_txt);
        getdate(); //get date from TeacherCheck Activity

        dateTxt.setText(date);
    }

    private void getdate() {
        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date1");
    }
}

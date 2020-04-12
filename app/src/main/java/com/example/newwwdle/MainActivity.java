package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText name, password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.edit_name); //username(student ID)
        password = findViewById(R.id.edit_id);//password

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                switch(name.getText().toString()) {
                    case "A":
                        intent.setClass(MainActivity.this, Student.class);
                        bundle.putString("name", name.getText().toString());//send student ID to next activity
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "B":
                        intent.setClass(MainActivity.this, teacher.class);
                        bundle.putString("name", name.getText().toString());//send student ID to next activity
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });

    }
}

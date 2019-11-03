package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {
    LinearLayout li_test;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        li_test=findViewById(R.id.li_test);
        Intent intent = getIntent(); //intent를 받는다.
        uid=intent.getStringExtra("uid");



        li_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(HomeActivity.this, TestActivity.class);
                test.putExtra("uid",uid);
                test.putExtra("member",true);
                startActivity(test);
            }
        });
    }
}

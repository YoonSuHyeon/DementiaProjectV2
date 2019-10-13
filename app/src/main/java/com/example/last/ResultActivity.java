package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    int score,age;//점수
    String result,gender,graduation,name; //결과


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textView = findViewById(R.id.textView);
        //점수를 받는다.
        Intent intent = getIntent(); //intent를 받는다.
        boolean member = getIntent().getBooleanExtra("member",true);
        if(member){ //회원이면 하는일
            score = intent.getIntExtra("score",0);
            textView.setText("member: "+ member);
        }else{//비회원 이면 하는일
            score = intent.getIntExtra("score",0);
            age = intent.getIntExtra("age",0);
            gender=intent.getStringExtra("gender");
            graduation=intent.getStringExtra("graduation");
            name = intent.getStringExtra("name");

            textView.setText("member: "+ member+"\n");
            textView.append("age: "+ age+"\n");
            textView.append("graduation: "+ graduation+"\n");
            textView.append("gender: "+ gender+"\n");
            textView.append("name: "+ name+"\n");
            textView.append("score:"+score+"\n");


        }

    }
    public String checkup(){

        return ("치매입니다");
    }
}

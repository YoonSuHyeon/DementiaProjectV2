package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {
     int score,age;//점수
     String result,gender,graduation; //결과


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //점수를 받는다.
        Intent intent = getIntent(); //intent를 받는다.

        if(intent.getBooleanExtra("member",true)){ //회원이면 하는일

        }else{//비회원 이면 하는일
            score = intent.getIntExtra("score",0);
            age = intent.getIntExtra("age",0);
            gender=intent.getStringExtra("gender");
            graduation=intent.getStringExtra("graduation");
            result=checkup();

        }

    }
    public String checkup(){

        return ("치매입니다");
    }
}

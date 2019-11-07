package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TrainingResultActivity extends AppCompatActivity {
    Intent intent;
    int score;
    String uid;
    TextView scoreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_result);
        scoreTextView=findViewById(R.id.score);


        intent = getIntent();
        score = intent.getIntExtra("score",0); //trainingTestActivity 에서 보낸 인텐트 score 의 값을 가져온다.
        uid = intent.getStringExtra("uid"); //사용자의 정보를 가져오기 위해서는 uid르 사용해서 Firebase에 접근한다.
        scoreTextView.setText("20점 만점 중 "+score+"점입니다.");

    }
}

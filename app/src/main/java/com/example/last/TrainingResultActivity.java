package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TrainingResultActivity extends AppCompatActivity {
    Intent intent;
    int score;
    String uid;
    TextView trainingTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_result);
        trainingTextView=findViewById(R.id.trainingTextView);


        intent = getIntent();
        score = intent.getIntExtra("score",0); //trainingTestActivity 에서 보낸 인텐트 score 의 값을 가져온다.
        uid = intent.getStringExtra("uid"); //사용자의 정보를 가져오기 위해서는 uid르 사용해서 Firebase에 접근한다.
        trainingTextView.setText("30점 만점"+score+"입니다.");

    }
}

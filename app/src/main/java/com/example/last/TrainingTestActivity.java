package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrainingTestActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_test);
        button = findViewById(R.id.trainingbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent training = new Intent(TrainingTestActivity.this, TrainingResultActivity.class);

                    int score =0 ;
                    String uid = getIntent().getStringExtra("uid");
                    training.putExtra("uid", uid);
                    training.putExtra("score", score);

                    startActivity(training);
            }
        });
    }
}

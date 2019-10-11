package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView txt_problem = findViewById(R.id.exampleTextView);
        Drawable drawable = getResources().getDrawable(R.drawable.logo);

        ImageView imageView = findViewById(R.id.exampleImageView);
        imageView.setImageDrawable(drawable);

        /* 만약 이미지 뷰가 없으면
            imageView.setVisibility(View.GONE);
         */
    }
}

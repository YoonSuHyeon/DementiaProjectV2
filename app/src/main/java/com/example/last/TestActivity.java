package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TestActivity extends AppCompatActivity {

    int problemsnum=0;
    ArrayList<Problem> problems;
    TextView exampleTextView;
    ImageView exampleImageView;
    EditText  answerEditText;
    Button exampleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AssetManager am = getResources().getAssets() ;
        InputStream is = null ;
        byte buf[] = new byte[1024] ;
        String text = "" ;
        StringTokenizer tokens;
        problems = new ArrayList<>();
         exampleTextView = (TextView) findViewById(R.id.exampleTextView) ; //문제 넣을 텍스트뷰
         exampleImageView =(ImageView) findViewById(R.id.exampleImageView);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        exampleButton = (Button) findViewById(R.id.exampleButton);



        try {
            is = am.open("test.txt") ;

            while (is.read(buf) > 0) {
                 text=text+new String(buf);

            }
            Log.d("TAG1", "string : " +text);
            tokens = new StringTokenizer(text);


            for(int i=0;tokens.hasMoreTokens();++i) {
                String num =tokens.nextToken("*");
                String example = tokens.nextToken("*");   // 문제
                String answer = tokens.nextToken("*");   // 답
                String url = tokens.nextToken("*");   // 성별
                Problem problem = new Problem(example, answer, url,num);
                problems.add(problem);
            }

            is.close() ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                is.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }
        Log.d("TAG1", "string : " +problems.size());
        exampleTextView.setText(problems.get(problemsnum).example);
        if(!problems.get(problemsnum).url.equals("null") ){
            //problems.get(1).url  이미지넣기
        }


        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(problems.get(problemsnum).num.equals("1")){

                }

                if(problems.get(problemsnum).num.equals("2")){

                }
                if(problems.get(problemsnum).num.equals("3")){

                }

                if(problemsnum<problems.size()){
                       problemsnum++;
                       exampleTextView.setText(problems.get(problemsnum).example);
                }
                else{
                       Intent loginIntent = new Intent(TestActivity.this, ResultActivity.class);
                       startActivity(loginIntent);
                   }

               }

        });





    }
}

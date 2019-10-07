package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.speech.tts.TextToSpeech.ERROR;

public class TestActivity extends AppCompatActivity {

    int problemsnum = 0;
    ArrayList<Problem> problems;
    TextView exampleTextView;
    ImageView exampleImageView;
    EditText answerEditText;
    Button exampleButton;
    TextToSpeech tts;
    String season = null, days = null;
    int year, month,day,week,score=0;
     AssetManager am;
     InputStream is = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        am = getResources().getAssets();

        byte buf[] = new byte[1024];
        String text = "";
        String num = null;
        StringTokenizer tokens;
        problems = new ArrayList<>();
        exampleTextView = (TextView) findViewById(R.id.exampleTextView); //문제 넣을 텍스트뷰
        exampleImageView = (ImageView) findViewById(R.id.exampleImageView);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        exampleButton = (Button) findViewById(R.id.exampleButton);

      // 캘린더 객체로  5번까지 답 생성 하기 .
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        week = calendar.get(Calendar.DAY_OF_WEEK);


        if (month == 3 || month == 4 || month == 5)
            season = "봄";
        else if (month == 6 || month == 7 || month == 8)
            season = "여름";
        else if (month == 9 || month == 10 || month == 11)
            season = "가을";
        else if (month == 12 || month == 1 || month == 2)
            season = "겨울";

        switch (week) {
            case 1:
                days = "일";
                break;
            case 2:
                days = "월";
                break;
            case 3:
                days = "화";
                break;
            case 4:
                days = "수";
                break;
            case 5:
                days = "목";
                break;
            case 6:
                days = "금";
                break;
            case 7:
                days = "토";
                break;
        }

        try {
            is = am.open("test.txt");

            while (is.read(buf) > 0) {
                text += new String(buf);

            }
            Log.d("TAG1", "string : " + text);
            tokens = new StringTokenizer(text, "*");

            while (tokens.hasMoreTokens()) {
                num = tokens.nextToken();       //번호
                num = num.replaceAll("\r\n", "");//토큰으로 문자열 자를 때 문장 끝에 \r\n이 인식됨
                String example = tokens.nextToken();   // 문제
                String answer = tokens.nextToken();   // 답
                String url = tokens.nextToken();   // 성별

                Problem problem = new Problem(example, answer, url, num);
                problems.add(problem);
            }


            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("TAG1", "size : " + problems.size());

       /* if (!problems.get(problemsnum).url.equals("null")) {
            //problems.get(1).url  이미지넣기
        }*/

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);

                    //tts.setPitch(0.8f);// 말하는 속도 조절  기본속도: 1.0f
                    String str = "테스트를 시작하겠습니다."+problems.get(problemsnum).example;//첫번째 문제를 String str에 저장
                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//str문자열 음성 출력 및 QUEUE_FLUSH: 음성출력 전 출력메모리 리셋

                }
            }
        });


        exampleTextView.setText(problems.get(problemsnum).num+"."+problems.get(problemsnum).example);//첫번째 문제의 문제 출력
        Log.d("TAG1", "sssskw " + problems.get(10).url);
       /*String resName="@drawable/"+problems.get(10).url;
        int resID = getResources().getIdentifier(resName,"drawable",getPackageName());
        exampleImageView.setImageResource(resID);이미지넣는거*/

        Log.d("TAG1", "sssskw " + year);


        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.d("TAG1", "zzzzzzzz : " + problemsnum);
                    Log.d("TAG1", "aaa : " + score);
                    if (problemsnum < problems.size()) {
                        String strnum = problems.get(problemsnum).num;
                        int num1 = Integer.parseInt(strnum);
                        switch (num1) {
                            case 1:
                                if (!answerEditText.getText().toString().equals("")&&Integer.parseInt(answerEditText.getText().toString())==year) {
                                    score += 1; //1번문제를 맞췄을시
                                }
                                problemsnum++;
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                String str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 2:
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(season)))
                                    score+=1;
                                problemsnum++;
                                answerEditText.setText("");

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 3:
                                if(!answerEditText.getText().toString().equals("")&&Integer.parseInt(answerEditText.getText().toString())==day)
                                    score += 1; //1번문제를 맞췄을시
                                answerEditText.setText("");
                                problemsnum++;

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 4:
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(days)))
                                    score += 1; //1번문제를 맞췄을시
                                answerEditText.setText("");
                                problemsnum++;

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 5:
                                if(!answerEditText.getText().toString().equals("")&&(Integer.parseInt(answerEditText.getText().toString())==month))
                                    score += 1; //1번문제를 맞췄을시

                                answerEditText.setText("");
                                problemsnum++;

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 6:
                            case 7:
                            case 8:
                                problemsnum++;
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                 str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                                Log.d("TAG1", "answer : " + problems.get(problemsnum).answer);
                                if(!answerEditText.getText().toString().equals("")&&
                                        (Integer.parseInt(answerEditText.getText().toString())==Integer.parseInt(problems.get(problemsnum).answer)))
                                   score += 1; //9번 정답 맞는지 확인하기 .

                                answerEditText.setText("");
                                problemsnum++;

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;

                            //10번 정답 맞는지 확인하기 .

                        }
                    } else {
                        Intent loginIntent = new Intent(TestActivity.this, ResultActivity.class);
                        startActivity(loginIntent);
                    }
                 }

        });

    }
    @Override
    protected void onDestroy () {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
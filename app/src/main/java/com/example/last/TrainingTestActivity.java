package com.example.last;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
public class TrainingTestActivity extends AppCompatActivity {
    ArrayList<Problem> problems;
    AssetManager am;
    InputStream is;
    String num=null;
    StringTokenizer tokens;
    Bitmap bm;
    int score = 0,count;
    TextView exampleTextView;
    ImageView exampleImageView;
    EditText answerEditText;
    Button exampleButton;
    ImageView iv;
    int[] a;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_test);
        Log.d("TAG", "dasddase1: ");

        exampleTextView = findViewById(R.id.textView);
        answerEditText = findViewById(R.id.answereditText);
        exampleButton = findViewById(R.id.button);
        exampleImageView = findViewById(R.id.exampleImageView);
        am = getResources().getAssets();
        Log.d("TAG", "dasddase2: ");
        problems = new ArrayList<>();
        int i, j;
        a = new int[16];
        for (i = 0; i < 16; i++) {//중복없는 랜덤함수
            Random r = new Random();
            a[i] = r.nextInt(16) ;//1~10번 문제
            if (i == 0)
                continue;
            else {
                for (j = 0; j < i; j++) {
                    if (a[i] == a[j])
                        i--;
                }
            }
        }
        for (i = 0; i < 16; i++){
            Log.d("TAG", "==a[]: "+a[i]);
        }

        try {
            is = am.open("traning.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String teasd = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ((teasd = br.readLine()) != null) {
                stringBuilder.append(teasd);
            }
            //Log.d("TAG", "teasd: "+teasd);
            // t.append(stringBuilder);

            tokens = new StringTokenizer(stringBuilder.toString(), "*");
            while (tokens.hasMoreTokens()) {
                num = tokens.nextToken();       //번호
                num = num.replaceAll("\r\n", "");//토큰으로 문자열 자를 때 문장 끝에 \r\n이 인식됨
                String example = tokens.nextToken();   // 문제
                String answer = tokens.nextToken();   // 답
                String url = tokens.nextToken();   // 이미지

                Problem problem = new Problem(example, answer, url, num);
                problems.add(problem);


            }
            Log.d("TAG", "rrrrrrfse123: "+ problems.size());

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


       /* tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);

                    //tts.setPitch(0.8f);// 말하는 속도 조절  기본속도: 1.0f
                    String str = "테스트를 시작하겠습니다."+problems.get(a[count]).example;//첫번째 문제를 String str에 저장
                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//str문자열 음성 출력 및 QUEUE_FLUSH: 음성출력 전 출력메모리 리셋

                }
            }
        });*/

        count = 0;
        Log.d("TAG", "a[count]: " + a[count]);
        Log.d("TAG", "problems.size(): " + problems.size());
        Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
        Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
        am = getResources().getAssets();
        try {
            exampleTextView.setText(problems.get(a[count]).num + "." + problems.get(a[count]).example);//첫번째 문제의 문제 출력
            is = am.open(problems.get(a[count]).url+".png");
            bm= BitmapFactory.decodeStream(is);
            exampleImageView.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("TAG", "problems.size(): " + problems.size());

        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < problems.size() - 1) {
                    int i = 0;
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);//키보드 강제 내리기
                    imm.hideSoftInputFromWindow(answerEditText.getWindowToken(), 0);
                    //String strnum = problems.get(a[count]-1).num;
                    //int num1 = Integer.parseInt(strnum);
                    Log.d("TAG", "a[count]: " + a[count]);
                    Log.d("TAG", "problems.size(): " + problems.size());
                    Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                    Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");

                    //Log.d("TAG", "텍스트 문제번호: " + num1);
                    switch (a[count]) {
                        case 1:
                        case 11://contains
                            if (problems.get(a[count]).answer.contains(answerEditText.getText().toString())) {
                                score += 1; //1번문제를 맞췄을시
                            }



                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            Log.d("TAG", "score1: " + score);
                            StringTokenizer token = new StringTokenizer(answerEditText.getText().toString(), " ");
                            String[] buffer = new String[token.countTokens()];
                            while(token.hasMoreTokens()){
                                buffer[i]=token.nextToken();
                                i++;
                            }
                            i=0;
                            Log.d("TAG", "buffer.length:///// " +buffer.length);
                            while (i < buffer.length) {
                                String str = buffer[i];
                                str = str.replaceAll("\r\n","");
                                Log.d("TAG", "str " + str+"=====");
                                if (problems.get(a[count]).answer.contains(str)) {
                                    i++;
                                }else
                                    Log.d("TAG", "falseeeeeeeeeeeeeeeeeeeeeeeeeee");  break;
                            }
                            count++;
                            if(i==buffer.length)
                                score+=1;
                            try {
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .
                                exampleTextView.setText(problems.get(a[count]).num + "." + problems.get(a[count]).example);//첫번째 문제의 문제 출력
                                is = am.open(problems.get(a[count]).url + ".png");
                                bm = BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 9://equals()
                            int temp = 0;
                            StringTokenizer token1 = new StringTokenizer(answerEditText.getText().toString(), " ");
                            String[] buffer1 = new String[token1.countTokens()];
                            StringTokenizer token2 = new StringTokenizer(problems.get(a[count]).answer, " ");
                            String[] buffer2 = new String[token1.countTokens()];
                            Log.d("TAG", "buffer111111111111111111111111: ");
                            while(token1.hasMoreTokens()){
                                buffer1[i] = token1.nextToken();
                                i++;
                            }
                            i=0;
                            Log.d("TAG", "buffer122222222222222222222222: ");
                            while(token2.hasMoreTokens()){
                                buffer2[i] = token2.nextToken();
                                i++;
                            }
                            i=0;
                            Log.d("TAG", "buffer13333333333333333333333: ");
                            while (i < buffer2.length) {
                                if (buffer1[i] .equals(buffer2[i])) {
                                    Log.d("TAG", "buffer1: " + buffer1[i]);
                                    Log.d("TAG", "buffer2: " + buffer2[i]);
                                    temp += 1; //1번문제를 맞췄을시
                                    i++;
                                } else break;
                            }
                            if (temp == buffer2.length)
                                score += 1;

                            Log.d("TAG", "buffer2.length: " + buffer2.length);
                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            Log.d("TAG", "score1: " + score);
                            count++;
                            try {
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .
                                exampleTextView.setText(problems.get(a[count]).num + "." + problems.get(a[count]).example);//첫번째 문제의 문제 출력
                                is = am.open(problems.get(a[count]).url + ".png");
                                bm = BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            if (problems.get(a[count]).answer.contains(answerEditText.getText().toString())) {
                                score += 1; //1번문제를 맞췄을시
                            }
                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            Log.d("TAG", "score1: " + score);
                            count++;
                            try {
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .
                                exampleTextView.setText(problems.get(a[count]).num + "." + problems.get(a[count]).example);//첫번째 문제의 문제 출력
                                is = am.open(problems.get(a[count]).url + ".png");
                                bm = BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            break;
                    }
                } else {////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    int i = 0;
                    switch (a[count]) {
                        case 1:
                        case 11://contains()
                            if (problems.get(a[count]).answer.contains(answerEditText.getText().toString())) {
                                score += 1; //1번문제를 맞췄을시
                            }
                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            Log.d("TAG", "score1: " + score);
                            StringTokenizer token = new StringTokenizer(answerEditText.getText().toString(), " ");
                            String[] buffer = new String[token.countTokens()];
                            while(token.hasMoreTokens()){
                                buffer[i]=token.nextToken();
                                Log.d("TAG", "buffer:///// " +buffer[i]);
                                i++;
                            }
                            Log.d("TAG", "buffer.length:///// " +buffer.length);
                            i=0;
                            while (i < buffer.length) {
                                Log.d("TAG", "buffer[i]: " +buffer[i]);
                                String str = buffer[i];
                                str = str.replaceAll("\r\n","");
                                if (problems.get(a[count]).answer.contains(str)) {
                                    score += 1; //1번문제를 맞췄을시
                                    i++;
                                } else
                                    break;
                            }
                            break;
                        case 9://equals()
                            int temp = 0;
                            StringTokenizer token1 = new StringTokenizer(answerEditText.getText().toString(), " ");
                            String[] buffer1 = new String[token1.countTokens()];
                            StringTokenizer token2 = new StringTokenizer(problems.get(a[count]).answer, " ");
                            String[] buffer2 = new String[token1.countTokens()];
                            Log.d("TAG", "buffer111111111111111111111111: ");
                            while(token1.hasMoreTokens()){
                                buffer1[i] = token1.nextToken();
                                i++;
                            }
                            i=0;
                            Log.d("TAG", "buffer122222222222222222222222: ");
                            while(token2.hasMoreTokens()){
                                buffer2[i] = token2.nextToken();
                                i++;
                            }
                            i=0;
                            Log.d("TAG", "buffer13333333333333333333333: ");
                            while (i < buffer2.length) {
                                if (buffer1[i] .equals(buffer2[i])) {
                                    Log.d("TAG", "buffer1: " + buffer1[i]);
                                    Log.d("TAG", "buffer2: " + buffer2[i]);
                                    temp += 1; //1번문제를 맞췄을시
                                    i++;
                                } else break;
                            }
                            if (temp == buffer2.length)
                                score += 1;

                            Log.d("TAG", "buffer2.length: " + buffer2.length);
                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            Log.d("TAG", "score1: " + score);
                            break;
                        default:
                            if (problems.get(a[count]).answer.contains(answerEditText.getText().toString())) {
                                score += 1; //1번문제를 맞췄을시
                            }
                            Log.d("TAG", "score1: " + score);
                            Log.d("TAG", "a[count]: " + a[count]);
                            Log.d("TAG", "problems.size(): " + problems.size());
                            Log.d("TAG", "answer: " + (problems.get(a[count]).answer) + "=====");
                            Log.d("TAG", "getText: " + answerEditText.getText().toString() + "=====");
                            break;
                    }
                    Intent loginIntent = new Intent(TrainingTestActivity.this, TrainingResultActivity.class);
                    loginIntent.putExtra("score",score);
                    startActivity(loginIntent);

                    //am.close();
                    //mainam.close();

                    //if 문을 걸어서 intent 이 member 를 false 비회원  이필요한거 다보낸다

                }
            }

        });
    }

}


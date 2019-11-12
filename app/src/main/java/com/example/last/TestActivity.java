package com.example.last;

import androidx.appcompat.app.AppCompatActivity;



import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import static android.speech.tts.TextToSpeech.ERROR;


public class TestActivity extends AppCompatActivity {


    ArrayList<Problem> problems;
    TextView exampleTextView;
    ImageView exampleImageView;
    EditText answerEditText;
    Button exampleButton;
    TextToSpeech tts;

    int problemsnum = 0;
    int year, month, day, week, score = 0;
    String state = null, city = null, town = null;  //state: 도, 특별시, 광역시  city: 시 군  town: 면/동/읍
    String season = null, days = null, speak = null;
    String[] buffer, buffer1;
    AssetManager am, mainam;
    InputStream is = null, MainImage = null;
    Double latitude = 0.0, longitude = 0.0;
    Bitmap bm, mainbm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        exampleButton=findViewById(R.id.exampleButton);



        am = getResources().getAssets();
        mainam = getResources().getAssets();
        String num = null;

        StringTokenizer tokens;

        problems = new ArrayList<>();
        exampleTextView = (TextView) findViewById(R.id.exampleTextView); //문제 넣을 텍스트뷰
        exampleImageView = (ImageView) findViewById(R.id.exampleImageView);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        exampleButton = (Button) findViewById(R.id.exampleButton);

        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());//음성 검색을 위한
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-kr");//인식할 언어 설정


        startLocationService();//내위치 경도 위도 값 추출하는 함수 호출
        String address = getAddress(latitude,longitude);//경도 위도값을 매개변수로 하여 주소를 가져옴

        Log.d("TAG", "latitude: "+latitude);
        Log.d("TAG", "longitude: "+longitude);
        tokens = new StringTokenizer(address," ");
        buffer1 = new String[tokens.countTokens()];
        int i = 0;
        while(tokens.hasMoreTokens()){
            buffer1[i] = tokens.nextToken();
            if(i==1)
                state=buffer1[i];
            if(i==2)
                city=buffer1[i];
            if(i==3)
                town=buffer1[i];
            i++;
        }
        Log.d("TAG", "state: "+state);
        Log.d("TAG", "city: "+city);
        Log.d("TAG", "town: "+town);

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

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String teasd= null;
            StringBuilder stringBuilder= new StringBuilder();

            while ((teasd=br.readLine()) != null) {
                stringBuilder.append(teasd);
            }


            tokens = new StringTokenizer(stringBuilder.toString(), "*");

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
        try {
            MainImage = mainam.open("android.png");//메인 이미지
            mainbm= BitmapFactory.decodeStream(MainImage);
            exampleImageView.setImageBitmap(mainbm);

        } catch (IOException e) {
            e.printStackTrace();
        }

        exampleTextView.setText(problems.get(problemsnum).num+"."+problems.get(problemsnum).example);//첫번째 문제의 문제 출력
        answerEditText.setHint("숫자만 적으세요.(년X , 연X)");

        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (problemsnum < problems.size()-1) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);//키보드 강제 내리기
                        imm.hideSoftInputFromWindow(answerEditText.getWindowToken(), 0);
                        exampleImageView.setImageBitmap(mainbm);
                        String strnum = problems.get(problemsnum).num;
                        int num1 = Integer.parseInt(strnum);
                        Log.d("TAG", "problemsnum: "+problemsnum);
                        Log.d("TAG", "problems.size(): "+problems.size());
                        Log.d("TAG", "텍스트 문제번호: "+num1);
                        Log.d("TAG", "답: "+problems.get(problemsnum).answer);
                        switch (num1) {
                            case 1:
                                if (!answerEditText.getText().toString().equals("")&&Integer.parseInt(answerEditText.getText().toString())==year) {
                                    score += 1; //1번문제를 맞췄을시
                                }
                                problemsnum++;
                                answerEditText.setText("");    //답맞는지 확인후 문제를 바꿔준다 .
                                Log.d("TAG", "score1: "+score);

                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                String str = problems.get(problemsnum).example;
                                answerEditText.setHint("계절을 입력하세요");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 2:
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(season)))
                                    score+=1;
                                problemsnum++;
                                answerEditText.setText("");
                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("숫자만 적으세요.(일X)");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 3:
                                if(!answerEditText.getText().toString().equals("")&&Integer.parseInt(answerEditText.getText().toString())==day)
                                    score += 1; //1번문제를 맞췄을시
                                answerEditText.setText("");
                                problemsnum++;
                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("?요일 - ?에 들어갈 단어를 입력하세요.(요일x)");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 4:
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(days)))
                                    score += 1; //1번문제를 맞췄을시
                                answerEditText.setText("");
                                problemsnum++;
                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("숫자만 적으세요.(월X)");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 5:
                                if(!answerEditText.getText().toString().equals("")&&(Integer.parseInt(answerEditText.getText().toString())==month))
                                    score += 1; //1번문제를 맞췄을시

                                answerEditText.setText("");
                                problemsnum++;
                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("자신의 해당하는 위치의 도를 입력하세요,");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 6:
                            case 7:
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(state))||(answerEditText.getText().toString().equals(city))||(answerEditText.getText().toString().equals(town)))
                                    score += 1; //1번문제를 맞췄을시
                                problemsnum++;
                                answerEditText.setText("");
                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                if(num1==6)
                                    answerEditText.setHint("자신의 해당하는 위치의 시/군를 입력하세요,");
                                else
                                    answerEditText.setHint("자신의 해당하는 위치의 동/면/읍을 입력하세요,");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 8:
                                if(!answerEditText.getText().toString().equals("")&&(answerEditText.getText().toString().equals(state))||(answerEditText.getText().toString().equals(city))||(answerEditText.getText().toString().equals(town)))
                                    score += 1; //1번문제를 맞췄을시
                                problemsnum++;
                                answerEditText.setText("");
                                //////다음문제 출력
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                exampleButton.setEnabled(false);//버튼 비활성화
                                answerEditText.setEnabled(false);//답안 적는 필드 비활성화
                                Log.d("TAG", "score1: "+score);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("단어를 듣고 기억한 후 말하는 문제입니다.");
                                str += "단어가 출력됩니다. 집중하세요.  " + problems.get(problemsnum).answer;
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                new Handler().postDelayed(new Runnable() {//tts출력후 음성인식 시작
                                    @Override
                                    public void run() {
                                        while(tts.isSpeaking())
                                            ;
                                        if(tts.isSpeaking()==false){
                                            Toast.makeText(getApplicationContext(), "음석인식이 활성화되었습니다. 말을 한 후에  확인버튼을 누르세요. ", Toast.LENGTH_SHORT).show();
                                            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                                            speechRecognizer.setRecognitionListener(listener);
                                            speechRecognizer.startListening(intent);
                                            exampleButton.setEnabled(true);
                                        }
                                    }
                                },100);


                                break;
                            case 9:
                                Log.d("TAG", "speak: "+speak);
                                answerEditText.setEnabled(true);
                                String answer = problems.get(problemsnum).answer;
                                StringTokenizer tokens = new StringTokenizer(answer,",");
                                buffer = new String[tokens.countTokens()];
                                int i=0;
                                while(tokens.hasMoreTokens()){
                                    buffer[i] = tokens.nextToken();
                                    i++;
                                }
                                for(i = 0; i<buffer.length;i++){
                                    if(speak==null)//음성인식을 못할경우
                                        continue;
                                    else if(speak.contains(buffer[i])){//무조건 음성호출을 입력받아야하고 안받고 버튼을 누르면 오류가 생김
                                        score+=1;
                                    }
                                }

                                Log.d("TAG", "score1: "+score);
                                //////다음문제 출력
                                problemsnum++;
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("100에서 7을 뺀 결과 값을 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;


                            case 10:
                            case 11:
                            case 12:
                            case 13:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                    score += 1;
                                answerEditText.setText("");
                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("전 문제의 결과 값에 7을 뺀 결과 값을 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 14:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                    score += 1;

                                Log.d("TAG", "score1: "+score);
                                Log.d("TAG", "answer1: "+problems.get(problemsnum).answer);
                                problemsnum++;
                                answerEditText.setText("");
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("세 가지 물건의 이름을 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 15:
                                String answer2 = problems.get(problemsnum).answer;
                                StringTokenizer token = new StringTokenizer(answer2,",");
                                String[] buffer = new String[token.countTokens()];
                                i=0;
                                int temp =0;
                                while(token.hasMoreTokens()) {
                                    buffer[i] = token.nextToken();
                                    i++;
                                }
                                Log.d("TAG", "bufferlength: "+buffer.length);
                                    for(i=0;i<buffer.length;i++){
                                        if(answerEditText.getText().toString().contains(buffer[i])){
                                            score+=1;
                                            Log.d("TAG", "temp: "+temp);
                                        }
                                        else
                                            continue;
                                    }


                                    Log.d("TAG", "score1: "+score);
                                    problemsnum++;
                                    is = am.open(problems.get(problemsnum).url+".png");
                                    bm= BitmapFactory.decodeStream(is);
                                    exampleImageView.setImageBitmap(bm);

                                    answerEditText.setText("");
                                    exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                    str = problems.get(problemsnum).example;
                                    answerEditText.setHint("그림의 이름을 입력하세요.");
                                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 16:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                    score += 1;

                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                is = am.open(problems.get(problemsnum).url+".png");
                                bm= BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);

                                answerEditText.setText("");
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("그림의 이름을 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 17:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                    score+=1;

                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                answerEditText.setText("");
                                exampleButton.setEnabled(false);
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                str += "단어가 출력됩니다. 집중하세요.  " + problems.get(problemsnum).answer;
                                answerEditText.setEnabled(false);
                                answerEditText.setHint("하는 말을 듣고 들으신 내용을 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                new Handler().postDelayed(new Runnable() {//tts출력후 음성인식 시작
                                    @Override
                                    public void run() {
                                        while(tts.isSpeaking())
                                            ;
                                        if(tts.isSpeaking()==false){
                                            Toast.makeText(getApplicationContext(), "음석인식이 활성화되었습니다. 말을 한 후에  확인버튼을 누르세요. ", Toast.LENGTH_SHORT).show();
                                            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                                            speechRecognizer.setRecognitionListener(listener);
                                            speechRecognizer.startListening(intent);
                                            exampleButton.setEnabled(true);
                                        }
                                    }
                                },100);

                                break;
                            case 18:
                                Log.d("TAG", "speak: "+speak);
                                answerEditText.setEnabled(true);

                                if(problems.get(problemsnum).answer.equals(speak))
                                    score+=1;

                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                answerEditText.setText("");

                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("사람이 위치하고 있는 층수를 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                is = am.open(problems.get(problemsnum).url+".png");
                                bm= BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);

                                break;


                            case 19:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                score+=1;

                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                answerEditText.setText("");
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("현재 계신곳의 장소를 입력하세요.");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                break;
                            case 20:
                            case 21:
                            case 22:
                                if(!answerEditText.getText().toString().equals("")&&
                                        (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                                    score+=1;

                                Log.d("TAG", "score1: "+score);
                                problemsnum++;
                                answerEditText.setText("");
                                exampleTextView.setText((num1+1) +"." + problems.get(problemsnum).example);
                                str = problems.get(problemsnum).example;
                                answerEditText.setHint("그림을 보고 해당 답을 입력하세요");
                                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);//첫 매개변수: 문장   두번째 매개변수:Flush 기존의 음성 출력 끝음 Add: 기존의 음성출력을 이어서 출력
                                is = am.open(problems.get(problemsnum).url+".png");
                                bm= BitmapFactory.decodeStream(is);
                                exampleImageView.setImageBitmap(bm);
                                break;
                        }
                    } else {
                        if(!answerEditText.getText().toString().equals("")&&
                                (answerEditText.getText().toString().equals(problems.get(problemsnum).answer)))
                            score+=1;

                        score +=3;//3점문제를 구현을 하지 않아 빈 점수 채움
                        Intent loginIntent = new Intent(TestActivity.this, ResultActivity.class);
                        boolean member = getIntent().getBooleanExtra("member", true);
                        if (member) {//회원
                            String uid = getIntent().getStringExtra("uid");
                            loginIntent.putExtra("member", member);
                            loginIntent.putExtra("score", score);
                            loginIntent.putExtra("uid", uid);
                            loginIntent.putExtra("state",state);
                            loginIntent.putExtra("city",city);
                            Log.d("TAG", "score============: "+score);

                            startActivity(loginIntent);
                            finish();
                        } else {//비회원
                            String name = getIntent().getStringExtra("name");
                            String gender = getIntent().getStringExtra("gender");
                            String graduation = getIntent().getStringExtra("graduation");
                            int age = getIntent().getIntExtra("age", 0);
                            //String Stringlatitude = latitude.toString();
                            //String Stringlongitude = longitude.toString();
                            loginIntent.putExtra("name", name);
                            loginIntent.putExtra("gender", gender);
                            loginIntent.putExtra("graduation", graduation);
                            loginIntent.putExtra("age", age);
                            loginIntent.putExtra("member", member);
                            loginIntent.putExtra("score", score);
                            loginIntent.putExtra("state",state);
                            loginIntent.putExtra("city",city);
                            Log.d("TAG", "speak: "+speak);
                            Log.d("TAG", "score============: "+score);
                            startActivity(loginIntent);
                        }

                        //if 문을 걸어서 intent 이 member 를 false 비회원  이필요한거 다보낸다

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private RecognitionListener listener = new RecognitionListener(){

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "음성인식 서비스가 과부하 되었습니다.";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 장애";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                default:
                    message = "알 수 없는 오류임";
                    tts.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//사용자가 말한 데이터를 ArrayList에 저장
            speak = list.get(0);//사용자의 말을 음성인식한 데이터를 String 형 speak변수에 저장
            speak = speak.replaceAll(" ","");// 사용자가 말한 문자열이 띄어쓰기가 있으면 공백 제거
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };
    private void startLocationService(){//내 GPS를 이용하여 위도 경도 얻는 함수
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);//위치관리자 생성

        GPSListener gpsListener = new GPSListener();
        long minTime = 100;//단위 millisecond
        float minDistance = 10;//단위 m

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);;//gps
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,gpsListener);//네트워크
            //5초 마다 or 10m 이동할떄마다 업데이트   network는 gps에 비해 정확도가 떨어짐

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d("TAG", "latitude: "+latitude);
                Log.d("TAG", "longitude: "+longitude);
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    private class GPSListener implements LocationListener{//위치리너스 클래스

        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }//위도와 경도를 주소로 변환하는 클래스

    public String getAddress(double lat, double lng){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address = null;
        try{
            address = geocoder.getFromLocation(lat,lng,3);//3은 최대 결과
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address1 = address.get(0);
        address.clear();
        return address1.getAddressLine(0).toString();
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


package com.example.last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ResultActivity extends AppCompatActivity {
    int score,age;//점수
    int[][] testscores={
            {20,24,25,26}, //60~69세 남자 0-3년,4-6년,7-12년,>13년
            {19,23,25,26}, //60~69세 여자 0-3년,4-6년,7-12년,>13년
            {20,23,25,26}, //70~74세 남자 0-3년,4-6년,7-12년,>13년
            {18,21,25,26}, //70~74세 여자 0-3년,4-6년,7-12년,>13년
            {20,22,25,25}, //75~79세 남자 0-3년,4-6년,7-12년,>13년
            {17,21,24,26}, //75~79세 여자 0-3년,4-6년,7-12년,>13년
            {18,22,24,25}, //80>=세 남자 0-3년,4-6년,7-12년,>13년
            {16,20,24,26}  //80>=세 여자 0-3년,4-6년,7-12년,>13년
    };
    String result,gender,graduation,name; //결과
    String uid;
    Intent intent;
    private DatabaseReference database;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        database= FirebaseDatabase.getInstance().getReference(); //파이어 베이스 초기화

        textView = (TextView)findViewById(R.id.textView);
        //점수를 받는다.
         intent = getIntent(); //intent를 받는다.
        boolean member = getIntent().getBooleanExtra("member",true);
        if(member){ //회원이면 하는일
            score = intent.getIntExtra("score",0);
            score=24;

            //score= score+4; // 시행점수 +4 (시간지남력(1), 주의집중력(2), 언어기능(1))

            uid = intent.getStringExtra("uid");

            DatabaseReference table_user = database.child("Users").child(uid);
            ValueEventListener vListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        //나의 데이타를 가져오는것

                    Log.d("TAG1", "dataSnapshot!! : " + dataSnapshot.child("age").getValue(Integer.class) );
                    Log.d("TAG1", "dataSnapshot!! : " + dataSnapshot.child("sex").getValue(String.class) );
                    Log.d("TAG1", "dataSnapshot!! : " + dataSnapshot.child("graduation").getValue(String.class) );
                    Log.d("TAG1", "dataSnapshot!! : " + dataSnapshot.child("name").getValue(String.class) );
                    age = dataSnapshot.child("age").getValue(Integer.class);
                    gender=dataSnapshot.child("sex").getValue(String.class);
                    graduation=dataSnapshot.child("graduation").getValue(String.class);
                    name = dataSnapshot.child("name").getValue(String.class);
                     result =checkup(); // 평가하는 함수

                    textView.setText(result);

                }
                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }

            };
            table_user.addListenerForSingleValueEvent(vListener);





        }
        else{//비회원 이면 하는일
            score = intent.getIntExtra("score",0);
            age = intent.getIntExtra("age",0);
            gender=intent.getStringExtra("gender");
            graduation=intent.getStringExtra("graduation");
            name = intent.getStringExtra("name");

            textView.setText("Score: "+ score);
            textView.append("age: "+ age);
            textView.append("graduation: "+ graduation);
            textView.append("gender: "+ gender);
            textView.append("name: "+ name);
            result=checkup();

            textView.setText(result);
        }

    }




    public String checkup(){ //평가하는 함수
       /* int[][] testscores={
                {20,24,25,26}, //60~69세 남자 0-3년,4-6년,7-12년,>13년
                {19,23,25,26}, //60~69세 여자 0-3년,4-6년,7-12년,>13년
                {20,23,25,26}, //70~74세 남자 0-3년,4-6년,7-12년,>13년
                {18,21,25,26}, //70~74세 여자 0-3년,4-6년,7-12년,>13년
                {20,22,25,25}, //75~79세 남자 0-3년,4-6년,7-12년,>13년
                {17,21,24,26}, //75~79세 여자 0-3년,4-6년,7-12년,>13년
                {18,22,24,25}, //80>=세 남자 0-3년,4-6년,7-12년,>13년
                {16,20,24,26}  //80>=세 여자 0-3년,4-6년,7-12년,>13년
        };*/
       int i,j; //점수를 가져오기 위한 인덱스

        /* i인덱스를 찾기위한 if 문 */
        if(age<=69 && gender.equals("남자")){//60~69세 남자 60보다 작은 사람 전부다 포함을 시킴.
           i=0;
       }else if(age<=69 && gender.equals("여자")){//60~69세 여자
            i=1;
       } else if(age<=74 && gender.equals("남자")){//70~74세 남자
            i=2;
       } else if(age<=74 && gender.equals("여자")){//70~74세 여자
            i=3;
       } else if(age<=79 && gender.equals("남자")){//75~79세 남자
            i=4;
       } else if(age<=79 && gender.equals("여자")){//75~79세 여자
            i=5;
       } else if(gender.equals("남자")){//80>=세 남자
            i=6;
       } else  {//80>=세 여자
            i=7;
       }

        /* j인덱스를 찾기위한 if 문 */
       if(graduation.equals("무학력")){
            j=0;
       }else if(graduation.equals("초졸")){
            j=1;
       }else if(graduation.equals("중졸")||graduation.equals("고졸")){
            j=2;
       }else { //대졸
            j=3;
       }
       if(testscores[i][j]<=score) return "정상입니다.";
       if(score>=24) return "확정적 정상입니다.";
       if(testscores[i][j]>score&& 20 <score ) return "치매의심입니다.";
       return"치매입니다.";


    }
}
package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TestActivity extends AppCompatActivity {
    Sqlite sqlite;
    SQLiteDatabase db;
    TextView textView,textView2;//SQLite 데이터베이스에 데이터를 추가 수정 삭제 조회를 하기 위해 사용.
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sqlite = new Sqlite(this,"TEST",null,4);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        try{//tex(문제 텍스트) 내용들들을 Token하는 과정//
            InputStream inputStream = getResources().openRawResource(R.raw.test);
            if(inputStream !=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String read;
                StringBuilder stringBuilder = new StringBuilder();//StringBuilder: 기존의 데이터에 더하는 방식을 사용하기 때문에 속도가 빠름

                while((read=bufferedReader.readLine())!=null){
                    stringBuilder.append(read);
                }
                inputStream.close();

                String str = stringBuilder.toString();
                StringTokenizer token1 = new StringTokenizer(str," ");

                String[] buffer = new String[3];
                while(token1.hasMoreTokens()){
                    StringTokenizer token2 = new StringTokenizer(token1.nextToken(),"/");
                    int i=0;
                    while(token2.hasMoreTokens()) {
                        buffer[i] = token2.nextToken();
                        i++;
                    }
                    insert(buffer[0],buffer[1],buffer[2]);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        select();
        //deleteAll();
    }
    public void insert(String Level, String Question ,String Picture){
        db = sqlite.getWritableDatabase();//getWritableDatabase 이 메소드는 DB를 연다라는 의미를 가짐
        ContentValues values = new ContentValues();
        values.put("Level",Level);
        values.put("Question",Question);
        values.put("Picture",Picture);
        db.insert("Test",null,values);
    }
    public void select(){
        db = sqlite.getReadableDatabase();//읽기 위해 디비를 연다. 없으면 onCreate 실행
        Cursor cursor;
        cursor = db.rawQuery("SELECT Level ,Question, Picture FROM Test",null);
        String Result="";
        while (cursor.moveToNext()){
            String Level = cursor.getString(0);
            String Question = cursor.getString(1);
            String Picture = cursor.getString(2);

            textView.setText(Question);

            if(Picture!=null) {
                try {
                    InputStream is =null;
                    AssetManager am = getResources().getAssets();
                    is = am.open(Picture+".png");
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bm);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Result += (Level+":"+Question + "=" + Ansewer +"\n");
            }
        }
    }
    public void deleteAll() {
        db.delete("Test",null, null);
    }


}
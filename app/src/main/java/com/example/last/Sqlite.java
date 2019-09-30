package com.example.last;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.*;
import android.database.sqlite.SQLiteOpenHelper;

public class Sqlite extends SQLiteOpenHelper{
    public Sqlite(Context context,  String name, CursorFactory factory, int version) {
        super(context, name, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TEST(_id INTEGER primary key autoincrement, Level TEXT not null, Question TEXT not null, Picture TEXT);");//Test 테이블을 생성
        //Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists TEST");
        onCreate(db);
    }
}

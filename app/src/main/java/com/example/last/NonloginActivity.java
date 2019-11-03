package com.example.last;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

public class NonloginActivity extends AppCompatActivity {
    Button nonlogin_picker_ymd,nonloginStartTextButton;
    EditText nonloginAgeEditText,nonloginNameEditText;
    Spinner nonloginGraduationSpinner;
    ArrayAdapter adapter;
    RadioButton nonlogin_genderWoman,nonlogin_genderMan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonlogin);
        nonloginStartTextButton=findViewById(R.id.nonloginStartTextButton);
        nonlogin_picker_ymd=findViewById(R.id.nonlogin_picker_ymd);

        nonloginAgeEditText=findViewById(R.id.nonloginAgeEditText);
        nonloginNameEditText=findViewById(R.id.nonloginNameEditText);
        nonloginGraduationSpinner=findViewById(R.id.nonloginGraduationSpinner);

        adapter = ArrayAdapter.createFromResource(this,R.array.graduation,android.R.layout.simple_spinner_dropdown_item);
        nonloginGraduationSpinner.setAdapter(adapter);

        nonlogin_genderMan=findViewById(R.id.nonlogin_genderMan);
        nonlogin_genderWoman=findViewById(R.id.nonlogin_genderWoman);
        nonloginAgeEditText.setEnabled(false);
        nonlogin_picker_ymd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //생년월일  picker
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(NonloginActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try{
                            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(year+"-"+(month+1)+"-"+dayOfMonth);
                            int m=month+1;
                            nonloginAgeEditText.setText(year + "." +m +"." + dayOfMonth);

                        }catch (Exception e){
                            e.printStackTrace();
                        }}
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.getDatePicker().setSpinnersShown(true);
                dialog.show();
            }
        });




        nonloginStartTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력 정보를 다 입력 했는지 확인


                if(nonloginAgeEditText.getText().toString().length()==0 || nonloginNameEditText.getText().toString().length() ==0){//이름 나이  입력이 됬는지 확인한다.
                    Toast.makeText(NonloginActivity.this, "모든 정보를 입력해주세요..", Toast.LENGTH_LONG).show();
                }else{
                    String name,gender,graduation,age; //


                    if(nonlogin_genderWoman.isChecked()){
                        gender ="여자";
                    }
                    else{
                        gender = "남자";
                    }

                    age=nonloginAgeEditText.getText().toString();
                    name=nonloginNameEditText.getText().toString();
                    graduation=nonloginGraduationSpinner.getSelectedItem().toString();

                    StringTokenizer tokens; // age가 생년월일로 되어있기 때문에 입력한 년도를 토큰으로 가져와 나이를 구한다.
                    tokens = new StringTokenizer(age,".");
                    age = tokens.nextToken();

                    int year; //현재 년도
                    Calendar calendar = new GregorianCalendar(Locale.KOREA);
                    year = calendar.get(Calendar.YEAR);

                    int myage; //실제 나이
                    myage = year-Integer.parseInt(age)+1; //나이를 구하기 위해서 현재 년을 구해서 차이를 구한후 1을 더한다.

                    Log.d("TAG", "name 등등"+name+gender+graduation+myage);
                    Intent intent = new Intent(NonloginActivity.this, TestActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("gender",gender);
                    intent.putExtra("graduation",graduation);
                    intent.putExtra("age",myage);
                    intent.putExtra("member",false);
                    startActivity(intent);
                }

            }
        });
        //비회원 정보 입력 받는 액티비티

    }
}

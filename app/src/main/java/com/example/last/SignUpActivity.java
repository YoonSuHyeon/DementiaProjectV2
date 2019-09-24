package com.example.last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText id,email,password,password2,name,age;
    RadioButton man,woman,userRadioButton,adminRadioButton;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private DatabaseReference database;
    String gendered;
    String authority;
    int checknum ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        database= FirebaseDatabase.getInstance().getReference();
        id=findViewById(R.id.idText);
        email = findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        password2=findViewById(R.id.passwordText2);
        name =findViewById(R.id.nameText);
        age=findViewById(R.id.ageText);

        man =findViewById(R.id.genderMan);
        woman=findViewById(R.id.genderWoman);

        userRadioButton =findViewById(R.id.userRadioButton);
        adminRadioButton=findViewById(R.id.adminRadioButton);

        Button register = findViewById(R.id.registerButton);

        spinner = (Spinner)findViewById(R.id.graduationSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.graduation,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                create_mem();
            }
        });


    }
private void create_mem()
{


    if(userRadioButton.isChecked()){
        authority ="사용자";
    }
    else{
        authority ="관리자";
    }
    if(woman.isChecked()){
        gendered ="여자";
    }
    else{
        gendered = "남자";
    }


    if(!password.getText().toString().equals(password2.getText().toString()))
    {
    Toast.makeText(this, "비밀번호가 일치 하지 않습니다..", Toast.LENGTH_LONG).show();
    }
    else if(id.getText().toString().length()==0 || email.getText().toString().length()==0 || password.getText().toString().length()==0||
        password2.getText().toString().length()==0|| name.getText().toString().length()==0 || age.getText().toString().length() ==0 )
        {
            Toast.makeText(this, "모든 정보를 입력해주세요..", Toast.LENGTH_LONG).show();
        }

        else if(!Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", id.getText().toString()))
            {
                Toast.makeText(this,"ID 형식이 바르지 않습니다\n영문,숫자 포함 5~12자.",Toast.LENGTH_SHORT).show();
            }
            else  if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(this,"이메일 형식이 올바르지 않습니다.\nex)abc@gmail.com",Toast.LENGTH_SHORT).show();
                }
                else {
                         checknum =0;
                        DatabaseReference table_user = database.child("Users");
                        ValueEventListener  vListener=new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                            Log.d("TAG1", "datattt : " + noteDataSnapshot);
                                            Log.d("TAG1", "dataaaa : " + noteDataSnapshot.getChildren());
                                            for (DataSnapshot da : dataSnapshot.getChildren()) {

                                                String checkId = id.getText().toString();
                                                String dbId = da.child("id").getValue(String.class);
                                                Log.d("TAG1", "string : " + dbId);
                                                if (checkId.equals(dbId)) {
                                                    Toast.makeText(SignUpActivity.this, "아이디가 이미 있습니다.", Toast.LENGTH_SHORT).show();
                                                    checknum = 1;
                                                    break;

                                                }
                                            }
                                        }
                                        if (checknum == 0) {
                                            Users user = new Users(age.getText().toString(), id.getText().toString(), password.getText().toString(), email.getText().toString(), gendered, spinner.getSelectedItem().toString(),
                                                    authority, "0", false);
                                            database.child("Users").push().setValue(user);
                                            Toast.makeText(SignUpActivity.this, "회원가입했습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                };
                                table_user.addListenerForSingleValueEvent(vListener);


                            }
                        }
 //약관 동의 했는지 확인하기 .

}

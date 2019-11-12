package com.example.last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class FindActivity extends AppCompatActivity {
    Button findIdEmailButton,findIdCertifyButton;
    EditText findIdEmailEditText,findIdCertifyEditText;
    GMailSender gMailSender;
    private DatabaseReference database;
    String checkemail ;
    String dbemail ;
    String dbid ; //ID
    String dbpassword  ; // PASSWORD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        findIdEmailButton=findViewById(R.id.findIdEmailButton);
        findIdCertifyButton =findViewById(R.id.findIdCertifyButton);
        findIdEmailEditText =findViewById(R.id.findIdEmailEditText);//Email EditText
        findIdCertifyEditText =findViewById(R.id.findIdCertifyEditText);//인증번호 EditText

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());  //이메일 사용을 위한 준비
        gMailSender = new GMailSender("tngus4753","youn4948!!"); //gmail 아이디 보내는사람
        database= FirebaseDatabase.getInstance().getReference(); //파이어베이스 설정



        findIdEmailButton.setOnClickListener(new View.OnClickListener() { // 이메일로 아이디를 찾기위한 버튼클릭시 하는일
            @Override
            public void onClick(View v) {

                //파이버베이스 접근후 이메일을 가지고있는 datasnapshot 을 찾아서  검사후 비밀번호와 아이디를 뽑아온다.
                DatabaseReference table_user = database.child("Users");
                ValueEventListener vListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean check =false; // 이메일 전송을 했는지 체크
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            Log.d("TAG1", "datattt : " + noteDataSnapshot);
                            Log.d("TAG1", "dataaaa : " + noteDataSnapshot.getChildren());
                            for (DataSnapshot da : dataSnapshot.getChildren()) {
                                if(check == true){
                                    break;
                                }
                                checkemail = findIdEmailEditText.getText().toString();
                                dbemail = da.child("email").getValue(String.class);

                                if (checkemail.equals(dbemail)) {
                                    try{

                                        check=true;
                                        dbid = da.child("id").getValue(String.class); //ID
                                        dbpassword = da.child("password").getValue(String.class); // PASSWORD
                                        gMailSender.sendMail("인증번호","인증번호는"+gMailSender.getEmailCode(),findIdEmailEditText.getText().toString());
                                        Log.d("TAG1", "d인증번호 : " + gMailSender.getEmailCode());
                                        Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                                        break;

                                    }catch(SendFailedException e){
                                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                    }catch(MessagingException e){
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }

                                }
                                else{
                                    Toast.makeText(FindActivity.this, "이메일로 등록된 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError databaseError){

                    }

                };
                table_user.addListenerForSingleValueEvent(vListener);


            }
        });

        findIdCertifyButton.setOnClickListener(new View.OnClickListener() { //인증확인하는 버튼 클릭시
            @Override
            public void onClick(View v) {
                if(gMailSender.getEmailCode().equals(findIdCertifyEditText.getText().toString())){ //이메일로보낸 인증코드와 입력한 인증코드가 같다면 아이디와 비밀번호를 보내준다.
                    try{
                        gMailSender.sendMail("사용자의 회원정보","사용자 iD="+dbid+"사용자 Password="+dbpassword,findIdEmailEditText.getText().toString());
                        Toast.makeText(FindActivity.this, "이메일로 아이디와 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();

                    }catch(SendFailedException e){
                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }catch(MessagingException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(FindActivity.this, "인증번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
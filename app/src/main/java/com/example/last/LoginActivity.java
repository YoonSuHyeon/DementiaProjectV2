package com.example.last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference database;
    EditText id,password;
    int checklogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database= FirebaseDatabase.getInstance().getReference();
        Button loginButton =findViewById(R.id.loginButton);
        TextView registerButton = findViewById(R.id.registerButton);
        id =findViewById(R.id.idText);
        password=findViewById(R.id.passwordText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(registerIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                login();


            }
        });

    }
    public  void login(){
        checklogin =0;
        DatabaseReference table_user = database.child("Users");
        ValueEventListener vListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot da : dataSnapshot.getChildren()) {
                        String checkId = id.getText().toString();
                        String checkpassword =password.getText().toString();

                        String dbId = da.child("id").getValue(String.class);
                        String dbpassword = da.child("password").getValue(String.class);

                        Log.d("TAG1", "string : " + dbId);
                        if (checkId.equals(dbId)&&checkpassword.equals(dbpassword)) {

                            checklogin=1;
                            break;

                        }
                    }
                }
                if(checklogin ==1 ) {
                    Toast.makeText(LoginActivity.this, "로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(loginIntent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "로그인 실패.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        table_user.addListenerForSingleValueEvent(vListener);

    }
}

package com.example.devide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText input_email, input_pw;
    Button contin;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        contin = (Button) findViewById(R.id.contin);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_email = (EditText) findViewById(R.id.input_id);
                input_pw = (EditText) findViewById(R.id.input_pw);

                String email = input_email.getText().toString();
                String pw = input_pw.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection("User")
                                    .whereEqualTo("email", email)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String report = document.getData().get("report").toString();
                                                    if (report.equals("0")) {
                                                        startToast("환영합니다!");
                                                    } else if (report.equals("3")) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startToast("규칙 위반으로 탈퇴당한 회원입니다.");
                                                            }
                                                        });
                                                        break;
                                                    } else {
                                                        startToast(report + "회 신고당하셨습니다.");
                                                    }

                                                    if (document.getData().get("college").toString().equals("영남대")) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.putExtra("college", "영남대");
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (document.getData().get("college").toString().equals("경북대")) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.putExtra("college", "경북대");
                                                        intent.putExtra("email", email);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                }
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, "이메일/비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

                startActivity(intent);
            }
        });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
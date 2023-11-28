package com.example.devide;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView mon, tue, wed, thu, fri, sat, sun;
    String collage;
    Dialog timepickDialog;
    String time1, time2, time, weekPlan = "";
    int ho1, ho2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_signup);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerBtn).setOnClickListener(onClickListener);
        RadioGroup radio = (RadioGroup) findViewById(R.id.radio);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio1) {
                    collage = "경북대";
                } else if (i == R.id.radio2) {
                    collage = "영남대";
                }
            }
        });

        TextView pwbutton = (TextView) findViewById(R.id.pwbutton);
        pwbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = ((EditText) findViewById(R.id.etPw)).getText().toString();
                String passwordCheck = ((EditText) findViewById(R.id.etPwck)).getText().toString();
                if (password.length() > 0 && passwordCheck.length() > 0) {
                    if (password.equals(passwordCheck)) {
                        startToast("비밀번호가 일치합니다.");
                    } else {
                        startToast("비밀번호가 일치하지 않습니다.");
                    }
                } else {
                    startToast("비밀번호를 입력해 주세요");
                }
            }
        });


        mon = (TextView) findViewById(R.id.mon);
        tue = (TextView) findViewById(R.id.tue);
        wed = (TextView) findViewById(R.id.wed);
        thu = (TextView) findViewById(R.id.thu);
        fri = (TextView) findViewById(R.id.fri);
        sat = (TextView) findViewById(R.id.sat);
        sun = (TextView) findViewById(R.id.sun);
        TextView[] week = {mon, tue, wed, thu, fri, sat, sun};


        timepickDialog = new Dialog(SignUpActivity.this);
        timepickDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timepickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickDialog.setCancelable(false);
        timepickDialog.setContentView(R.layout.timepick);

        for (TextView w : week) {
            w.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timepickDialog.show();
                    Button registerBtn = (Button) timepickDialog.findViewById(R.id.registerBtn);
                    TimePicker timep1 = timepickDialog.findViewById(R.id.time1);

                    // TimePicker 클릭 이벤트
                    timep1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                            ho1 = hour;
                            if (minute < 30)
                                minute = 0;
                            else if (minute < 59)
                                minute = 30;
                            time1 = String.format("%02d", hour) + "" + String.format("%02d", minute);
                        }
                    });

                    TimePicker timep2 = timepickDialog.findViewById(R.id.time2);
                    // TimePicker 클릭 이벤트
                    timep2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                            if (minute < 30) {
                                minute = 0;
                            } else if (minute < 59) {
                                minute = 30;
                            }

                            ho2 = hour;
                            time2 = String.format("%02d", hour) + "" + String.format("%02d", minute);
                        }
                    });
                    registerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (ho1 < 9 || ho1 > 18 || ho2 > 18)
                                startToast("오전 9시에서 오후6시 사이로 골라 주세요");
                            else if (ho1 > ho2)
                                startToast("시간을 다시 확인해 주세요");
                            else {
                                time = time1 + "/" + time2;
                                timepickDialog.dismiss();
                                String day = w.getText().toString();
                                String t = "/" + day + "/" + time;
                                weekPlan += t;
//                                startToast(weekPlan);
                                w.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.초록));
                            }
                        }
                    });
                }
            });
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }


    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.registerBtn:
                signUp();
                break;
        }
    };


    private void signUp() {
        String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPw)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.etPw)).getText().toString();
        String nickname = ((EditText) findViewById(R.id.etNickname)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0 && nickname.length() > 0) { //정보 전부 입력됐는지 확인
            if (password.equals(passwordCheck)) { //비밀번호 일치함
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, (task) -> {
                            if (task.isSuccessful()) { //Authentication에 등록 성공
                                FirebaseUser user = mAuth.getCurrentUser();

                                //firestore에 User database 저장---
                                User userInfo = new User(user.getUid(), email, password, nickname, collage, weekPlan, "0");

                                db.collection("User")
                                        .document(user.getUid())
                                        .set(userInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                startToast("로그인해 주세요!");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                startToast("erro: add User document");
                                            }
                                        });
                            } else { ////Authentication에 등록 실패
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }
                            }
                        });
            } else { //비밀번호 일치 안함
                startToast("비밀번호가 일치하지 않습니다..");
            }
        } else { //정보 입력 전부 안됨
            startToast("빠짐없이 입력해 주세요!");
        }
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

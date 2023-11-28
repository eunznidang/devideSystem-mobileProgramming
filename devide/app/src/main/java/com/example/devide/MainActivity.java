package com.example.devide;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button registerBtn;
    String college, email, customerID, itemID, memo, time, sellerID;
    ImageView item_iv, state_iv;
    TextView itemName_tv, originalPrice_tv, unit_tv, unitPrice_tv, college_tv, sentence_tv;
    TextView time_tv, memo_tv, x, o;
    int report;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference pathReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        college = intent.getStringExtra("college");
        email = intent.getStringExtra("email");

        college_tv = (TextView) findViewById(R.id.college_tv);
        college_tv.setText(college);

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterItemActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser sID = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Deal")
                .whereEqualTo("sellerID", sID.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String customerID = document.getData().get("customerID").toString();
                                itemID = document.getData().get("itemID").toString();
                                memo = document.getData().get("memo").toString();
                                time = document.getData().get("time").toString();

                                View dealview = (View) getLayoutInflater().inflate(R.layout.deal, null);
                                LinearLayout container = findViewById(R.id.dealLayout);

                                TextView customerName_tv = (TextView) dealview.findViewById(R.id.customerName_tv);
                                TextView item_tv = (TextView) dealview.findViewById(R.id.item_tv);
                                state_iv = (ImageView) dealview.findViewById(R.id.state_iv);
                                sentence_tv = (TextView) dealview.findViewById(R.id.sentence_tv);
                                time_tv = (TextView) dealview.findViewById(R.id.time_tv);
                                memo_tv = (TextView) dealview.findViewById(R.id.memo_tv);
                                time_tv.setText(time);
                                memo_tv.setText(memo);
                                x = (TextView) dealview.findViewById(R.id.x);
                                o = (TextView) dealview.findViewById(R.id.o);

                                db.collection("User")
                                        .whereEqualTo("userID", customerID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        customerName_tv.setText(document.getData().get("userName").toString());
                                                    }
                                                }
                                            }
                                        });

                                db.collection("Item")
                                        .whereEqualTo("itemID", itemID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        item_tv.setText(document.getData().get("itemName").toString());
                                                    }
                                                }
                                            }
                                        });

                                if (document.getData().get("success").toString().equals("false")) {

                                    sentence_tv.setText(" 님의 거래 제안: ");
                                    state_iv.setVisibility(View.VISIBLE);
                                    x.setText("거절");
                                    o.setText("수락");
                                    x.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // 거래 취소
                                            DocumentReference userRef = db.collection("Deal").document(customerID);
                                            userRef.delete();
                                            startToast("거래가 취소되었어요.");
                                            if (college.equals("영남대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "영남대");
                                                startActivity(intent);
                                                finish();
                                            } else if (college.equals("경북대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "경북대");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });

                                    o.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // 거래 확인
                                            DocumentReference userRef = db.collection("Deal").document(customerID);
                                            userRef
                                                    .update("success", "true")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            if (college.equals("영남대")) {
                                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                intent.putExtra("college", "영남대");
                                                                startActivity(intent);
                                                                finish();
                                                            } else if (college.equals("경북대")) {
                                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                intent.putExtra("college", "경북대");
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                    });
                                }
                                else {
                                    sentence_tv.setText(" 님과의 거래 : ");
                                    x.setText("신고");
                                    o.setText("완료");

                                    // 신고
                                    x.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            db.collection("User")
                                                    .whereEqualTo("userID", customerID)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    report = Integer.parseInt(document.getData().get("report").toString());
                                                                    report++;

                                                                    DocumentReference userRef = db.collection("User").document(customerID);
                                                                    userRef
                                                                            .update("report", Integer.toString(report))
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    startToast("사용자를 신고했어요");
//
                                                                                    DocumentReference userRef = db.collection("Deal").document(customerID);
                                                                                    userRef.delete();

                                                                                    if (college.equals("영남대")) {
                                                                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                                        intent.putExtra("college", "영남대");
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    } else if (college.equals("경북대")) {
                                                                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                                        intent.putExtra("college", "경북대");
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    startToast(e.toString());
                                                                                    Log.e("MyTag", e.toString());
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        }
                                                    });


                                        }
                                    });

                                    //거래완료
                                    o.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // 거래 확인
                                            DocumentReference userRef = db.collection("Deal").document(customerID);
                                            userRef.delete();
                                            startToast("다음 거래를 진행해 보세요!");
                                            if (college.equals("영남대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "영남대");
                                                startActivity(intent);
                                                finish();
                                            } else if (college.equals("경북대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "경북대");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                                container.addView(dealview);
                                dealview.setClickable(true);
                            }
                        }
                    }
                });

        FirebaseUser customerID = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Deal")
                .whereEqualTo("customerID", customerID.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                sellerID = document.getData().get("sellerID").toString();
                                itemID = document.getData().get("itemID").toString();
                                memo = document.getData().get("memo").toString();
                                time = document.getData().get("time").toString();

                                View dealview = (View) getLayoutInflater().inflate(R.layout.deal, null);
                                LinearLayout container = findViewById(R.id.dealLayout);

                                TextView customerName_tv = (TextView) dealview.findViewById(R.id.customerName_tv);
                                TextView item_tv = (TextView) dealview.findViewById(R.id.item_tv);
                                state_iv = (ImageView) dealview.findViewById(R.id.state_iv);
                                sentence_tv = (TextView) dealview.findViewById(R.id.sentence_tv);
                                time_tv = (TextView) dealview.findViewById(R.id.time_tv);
                                memo_tv = (TextView) dealview.findViewById(R.id.memo_tv);
                                time_tv.setText(time);
                                memo_tv.setText(memo);
                                x = (TextView) dealview.findViewById(R.id.x);
                                o = (TextView) dealview.findViewById(R.id.o);

                                db.collection("User")
                                        .whereEqualTo("userID", sellerID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        customerName_tv.setText(document.getData().get("userName").toString());
                                                    }
                                                }
                                            }
                                        });

                                db.collection("Item")
                                        .whereEqualTo("itemID", itemID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        item_tv.setText(document.getData().get("itemName").toString());
                                                    }
                                                }
                                            }
                                        });

                                if (document.getData().get("success").toString().equals("true")) {

                                    sentence_tv.setText(" 님과의 거래 : ");
                                    x.setText("신고");
                                    o.setText("완료");

                                    // 신고
                                    x.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            db.collection("User")
                                                    .whereEqualTo("userID", sellerID)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    report = Integer.parseInt(document.getData().get("report").toString());
                                                                    report++;

                                                                    DocumentReference userRef = db.collection("User").document(sellerID);
                                                                    userRef
                                                                            .update("report", Integer.toString(report))
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    startToast("사용자를 신고했어요");
//
                                                                                    DocumentReference userRef = db.collection("Deal").document(customerID.getUid());
                                                                                    userRef.delete();

                                                                                    if (college.equals("영남대")) {
                                                                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                                        intent.putExtra("college", "영남대");
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    } else if (college.equals("경북대")) {
                                                                                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                                                        intent.putExtra("college", "경북대");
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    startToast(e.toString());
                                                                                    Log.e("MyTag", e.toString());
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        }
                                                    });


                                        }
                                    });

                                    //거래완료
                                    o.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // 거래 확인
                                            DocumentReference userRef = db.collection("Deal").document(customerID.getUid());
                                            userRef.delete();
                                            startToast("다음 거래를 진행해 보세요!");
                                            if (college.equals("영남대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "영남대");
                                                startActivity(intent);
                                                finish();
                                            } else if (college.equals("경북대")) {
                                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                intent.putExtra("college", "경북대");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    container.addView(dealview);
                                    dealview.setClickable(true);
                                }
                            }
                        }
                    }
                });

        db.collection("User")
                .whereEqualTo("college", college)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userID = document.getData().get("userID").toString();
                                if (!document.getData().get("report").toString().equals("3")) {
                                    db.collection("Item")
                                            .whereEqualTo("userID", userID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            String itemID = document.getData().get("itemID").toString();
                                                            View view = (View) getLayoutInflater().inflate(R.layout.item, null);
                                                            LinearLayout container = findViewById(R.id.itemLayout);
                                                            ImageView item_iv = (ImageView) view.findViewById(R.id.item_iv);
                                                            itemName_tv = (TextView) view.findViewById(R.id.itemName_tv);
                                                            originalPrice_tv = (TextView) view.findViewById(R.id.totalPrice_tv);
                                                            unit_tv = (TextView) view.findViewById(R.id.unit_tv);
                                                            unitPrice_tv = (TextView) view.findViewById(R.id.unitPrice_tv);

                                                            itemName_tv.setText(document.getData().get("itemName").toString());
                                                            originalPrice_tv.setText(document.getData().get("originalPrice").toString() + " 원");
                                                            originalPrice_tv.setPaintFlags(originalPrice_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                            unit_tv.setText(document.getData().get("unit").toString());
                                                            unitPrice_tv.setText(document.getData().get("unitPrice").toString());


                                                            storage = FirebaseStorage.getInstance();
                                                            storageReference = storage.getReference();
                                                            pathReference = storageReference.child(document.getData().get("photo1").toString());
                                                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    Glide.with(view).load(uri).circleCrop().into(item_iv);
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });

                                                            container.addView(view);
                                                            view.setClickable(true);
                                                            view.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                                                                    intent.putExtra("itemID", itemID);
                                                                    startActivity(intent);
                                                                }
                                                            });


                                                        }
                                                    }
                                                }
                                            });
                                }

                            }
                        }

                    }
                });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
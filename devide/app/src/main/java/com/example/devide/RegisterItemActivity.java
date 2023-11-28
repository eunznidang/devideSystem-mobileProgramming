package com.example.devide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegisterItemActivity extends AppCompatActivity {

    String userID, itemName, itemCount, originalPrice, unitPrice, unit, photo1, photo2, itemID;
    RadioGroup radio;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button button1, button2, registerBtn, register_btn;
    TextView mon, tue, wed, thu, fri, sat, sun, text1, text2;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("image");
    Uri uri;
    int intPrice;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    StorageReference fileRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeritem);

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemName = ((EditText) findViewById(R.id.itemName)).getText().toString(); //물건 이름
                itemCount = ((EditText) findViewById(R.id.itemCount)).getText().toString(); // 물건 개수
                originalPrice = ((EditText) findViewById(R.id.totalPrice)).getText().toString(); // 구매 가격

                ///생성자 만들고 db 추가하기

                try {
                    int num = Integer.parseInt(itemCount);
                    int totalP = Integer.parseInt(originalPrice);
                    unitPrice = Integer.toString(totalP / num);
                    itemID = userID + "_" + itemName;
                    Item item = new Item(userID, itemID, itemName, itemCount, originalPrice, unitPrice, unit, photo1, photo2);
                    db.collection("Item")
                            .add(item)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    db.collection("User")
                                            .whereEqualTo("userID", userID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getData().get("college").toString().equals("영남대")) {
                                                                Intent intent = new Intent(RegisterItemActivity.this, MainActivity.class);
                                                                intent.putExtra("college", "영남대");
                                                                startActivity(intent);
                                                                finish();
                                                            } else if (document.getData().get("college").toString().equals("경북대")) {
                                                                Intent intent = new Intent(RegisterItemActivity.this, MainActivity.class);
                                                                intent.putExtra("college", "경북대");
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }
                                                    }
                                                }
                                            });
                                    startToast("거래 요청을 기다려 주세요~");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    startToast("등록 실패");
                                }
                            });
                } catch (Exception e) {
                    startToast("모든 정보를 빠짐없이 작성해 주세요");
                }


            }
        });


        radio = (RadioGroup) findViewById(R.id.radio);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.gae) {
                    unit = "개";
                } else if (i == R.id.kg) {
                    unit = "kg";
                }
            }
        });

        //상품
        button1 = (Button) findViewById(R.id.button1);
        text1 = (TextView) findViewById(R.id.text1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 업로드
                try {
                    itemName = ((EditText) findViewById(R.id.itemName)).getText().toString();

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Toast.makeText(RegisterItemActivity.this, "상단 내용을 먼저 채워 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 영수증
        button2 = (Button) findViewById(R.id.button2);
        text2 = (TextView) findViewById(R.id.text2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 업로드
                try {
                    itemName = ((EditText) findViewById(R.id.itemName)).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } catch (Exception e) {
                    Toast.makeText(RegisterItemActivity.this, "상단 내용을 먼저 채워 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    // 파이어베이스 물건 이미지 업로드
    private void uploadToFirebase(Uri uri, int requestCode) {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        switch (requestCode) {
            case 1:

                fileRef = reference.child(user.getUid() + "_" + itemName + "_item");
                photo1 = user.getUid() + "_" + itemName + "_item";
                fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Model model = new Model(uri.toString());
                                String modelid = root.push().getKey();
                                root.child(modelid).setValue(model);

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterItemActivity.this, "업로드 실패..", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case 2:
                fileRef = reference.child(user.getUid() + "_" + itemName + "_receipt");
                photo2 = user.getUid() + "_" + itemName + "_receipt";

                fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Model model = new Model(uri.toString());
                                String modelid = root.push().getKey();
                                root.child(modelid).setValue(model);

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterItemActivity.this, "업로드 실패..", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();

                    // 이미지 링크
                    if (uri != null) {
                        uploadToFirebase(uri, requestCode);
                    } else {
                        Toast.makeText(RegisterItemActivity.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
                    }
                    button1.setVisibility(View.INVISIBLE);
                    text1.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();

                    // 이미지 링크
                    if (uri != null) {
                        uploadToFirebase(uri, requestCode);
                    } else {
                        Toast.makeText(RegisterItemActivity.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
                    }
                    button2.setVisibility(View.INVISIBLE);
                    text2.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
package com.example.devide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class ItemActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    String dealID, sellerID, customerID, itemID, success = "false";
    Uri photo1, photo2;
    TextView itemName_tv, unit_tv, unitPrice_tv,sellerName_tv;
    private Uri[] images;
    TextView mon[], tue[], wed[], thu[], fri[], sat[], sun[];
    Dialog requestDialog;
    String time, memo, college;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mon = new TextView[]{findViewById(R.id.mon_9),
                findViewById(R.id.mon_930),
                findViewById(R.id.mon_10),
                findViewById(R.id.mon_1030),
                findViewById(R.id.mon_11),
                findViewById(R.id.mon_1130),
                findViewById(R.id.mon_12),
                findViewById(R.id.mon_1230),
                findViewById(R.id.mon_13),
                findViewById(R.id.mon_1330),
                findViewById(R.id.mon_14),
                findViewById(R.id.mon_1430),
                findViewById(R.id.mon_15),
                findViewById(R.id.mon_1530),
                findViewById(R.id.mon_16),
                findViewById(R.id.mon_1630),
                findViewById(R.id.mon_17),
                findViewById(R.id.mon_1730)};

        tue = new TextView[]{findViewById(R.id.tue_9),
                findViewById(R.id.tue_930),
                findViewById(R.id.tue_10),
                findViewById(R.id.tue_1030),
                findViewById(R.id.tue_11),
                findViewById(R.id.tue_1130),
                findViewById(R.id.tue_12),
                findViewById(R.id.tue_1230),
                findViewById(R.id.tue_13),
                findViewById(R.id.tue_1330),
                findViewById(R.id.tue_14),
                findViewById(R.id.tue_1430),
                findViewById(R.id.tue_15),
                findViewById(R.id.tue_1530),
                findViewById(R.id.tue_16),
                findViewById(R.id.tue_1630),
                findViewById(R.id.tue_17),
                findViewById(R.id.tue_1730)};

        wed = new TextView[]{findViewById(R.id.wed_9),
                findViewById(R.id.wed_930),
                findViewById(R.id.wed_10),
                findViewById(R.id.wed_1030),
                findViewById(R.id.wed_11),
                findViewById(R.id.wed_1130),
                findViewById(R.id.wed_12),
                findViewById(R.id.wed_1230),
                findViewById(R.id.wed_13),
                findViewById(R.id.wed_1330),
                findViewById(R.id.wed_14),
                findViewById(R.id.wed_1430),
                findViewById(R.id.wed_15),
                findViewById(R.id.wed_1530),
                findViewById(R.id.wed_16),
                findViewById(R.id.wed_1630),
                findViewById(R.id.wed_17),
                findViewById(R.id.wed_1730)};

        thu = new TextView[]{findViewById(R.id.thu_9),
                findViewById(R.id.thu_930),
                findViewById(R.id.thu_10),
                findViewById(R.id.thu_1030),
                findViewById(R.id.thu_11),
                findViewById(R.id.thu_1130),
                findViewById(R.id.thu_12),
                findViewById(R.id.thu_1230),
                findViewById(R.id.thu_13),
                findViewById(R.id.thu_1330),
                findViewById(R.id.thu_14),
                findViewById(R.id.thu_1430),
                findViewById(R.id.thu_15),
                findViewById(R.id.thu_1530),
                findViewById(R.id.thu_16),
                findViewById(R.id.thu_1630),
                findViewById(R.id.thu_17),
                findViewById(R.id.thu_1730)};

        fri = new TextView[]{findViewById(R.id.fri_9),
                findViewById(R.id.fri_930),
                findViewById(R.id.fri_10),
                findViewById(R.id.fri_1030),
                findViewById(R.id.fri_11),
                findViewById(R.id.fri_1130),
                findViewById(R.id.fri_12),
                findViewById(R.id.fri_1230),
                findViewById(R.id.fri_13),
                findViewById(R.id.fri_1330),
                findViewById(R.id.fri_14),
                findViewById(R.id.fri_1430),
                findViewById(R.id.fri_15),
                findViewById(R.id.fri_1530),
                findViewById(R.id.fri_16),
                findViewById(R.id.fri_1630),
                findViewById(R.id.fri_17),
                findViewById(R.id.fri_1730)};

        sat = new TextView[]{findViewById(R.id.sat_9),
                findViewById(R.id.sat_930),
                findViewById(R.id.sat_10),
                findViewById(R.id.sat_1030),
                findViewById(R.id.sat_11),
                findViewById(R.id.sat_1130),
                findViewById(R.id.sat_12),
                findViewById(R.id.sat_1230),
                findViewById(R.id.sat_13),
                findViewById(R.id.sat_1330),
                findViewById(R.id.sat_14),
                findViewById(R.id.sat_1430),
                findViewById(R.id.sat_15),
                findViewById(R.id.sat_1530),
                findViewById(R.id.sat_16),
                findViewById(R.id.sat_1630),
                findViewById(R.id.sat_17),
                findViewById(R.id.sat_1730)};

        sun = new TextView[]{findViewById(R.id.sun_9),
                findViewById(R.id.sun_930),
                findViewById(R.id.sun_10),
                findViewById(R.id.sun_1030),
                findViewById(R.id.sun_11),
                findViewById(R.id.sun_1130),
                findViewById(R.id.sun_12),
                findViewById(R.id.sun_1230),
                findViewById(R.id.sun_13),
                findViewById(R.id.sun_1330),
                findViewById(R.id.sun_14),
                findViewById(R.id.sun_1430),
                findViewById(R.id.sun_15),
                findViewById(R.id.sun_1530),
                findViewById(R.id.sun_16),
                findViewById(R.id.sun_1630),
                findViewById(R.id.sun_17),
                findViewById(R.id.sun_1730)};


        Intent intent = getIntent();
        itemID = intent.getStringExtra("itemID");

        itemName_tv = (TextView) findViewById(R.id.itemName_tv);
        unit_tv = (TextView) findViewById(R.id.unit_tv);
        unitPrice_tv = (TextView) findViewById(R.id.unitPrice_tv);
        sellerName_tv = (TextView) findViewById(R.id.sellerName_tv);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);

        db.collection("Item")
                .whereEqualTo("itemID", itemID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // 이미지
                                FirebaseStorage.getInstance()
                                        .getReference()
                                        .child(document.getData().get("photo1").toString())
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                photo1 = uri;

                                                FirebaseStorage
                                                        .getInstance()
                                                        .getReference()
                                                        .child(document.getData().get("photo2").toString())
                                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                photo2 = uri;
                                                                images = new Uri[]{photo1, photo2};

                                                                sliderViewPager.setOffscreenPageLimit(1);
                                                                sliderViewPager.setAdapter(new ImageSliderAdapter(ItemActivity.this, images));

                                                                sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                                                    @Override
                                                                    public void onPageSelected(int position) {
                                                                        super.onPageSelected(position);
                                                                        setCurrentIndicator(position);
                                                                    }
                                                                });

                                                                setupIndicators(images.length);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });

                                itemName_tv.setText(document.getData().get("itemName").toString());
                                unit_tv.setText(document.getData().get("unit").toString());
                                unitPrice_tv.setText(document.getData().get("unitPrice").toString());

                                sellerID = document.getData().get("userID").toString();
                                db.collection("User")
                                        .whereEqualTo("userID", sellerID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        sellerName_tv.setText(document.getData().get("userName").toString());
                                                        college = document.getData().get("college").toString();

                                                        String weekPlan = document.getData().get("weekPlan").toString();
                                                        String[] phNo = weekPlan.split("/");
                                                        for (int i = 1; i < phNo.length; i += 3) {

                                                            if (phNo[i].equals("월")) {

                                                                for (int j = 0; j < mon.length; j++) {
                                                                    if (phNo[i + 1].equals(mon[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(mon[k].getText().toString())) {
                                                                            mon[k].setVisibility(mon[k].VISIBLE);
                                                                            k++;
                                                                        }

                                                                        mon[k].setVisibility(mon[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }

                                                            }
                                                            if (phNo[i].equals("화")) {

                                                                for (int j = 0; j < tue.length; j++) {
                                                                    if (phNo[i + 1].equals(tue[j].getText().toString())) {
                                                                        int k = j;

                                                                        while (!phNo[i + 2].equals(tue[k].getText().toString())) {
                                                                            tue[k].setVisibility(tue[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        tue[k].setVisibility(tue[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }

                                                            }
                                                            if (phNo[i].equals("수")) {

                                                                for (int j = 0; j < wed.length; j++) {
                                                                    if (phNo[i + 1].equals(wed[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(wed[k].getText().toString())) {
                                                                            wed[k].setVisibility(wed[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        wed[k].setVisibility(wed[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (phNo[i].equals("목")) {

                                                                for (int j = 0; j < thu.length; j++) {
                                                                    if (phNo[i + 1].equals(thu[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(thu[k].getText().toString())) {
                                                                            thu[k].setVisibility(thu[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        thu[k].setVisibility(thu[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (phNo[i].equals("금")) {

                                                                for (int j = 0; j < fri.length; j++) {
                                                                    if (phNo[i + 1].equals(fri[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(fri[k].getText().toString())) {
                                                                            fri[k].setVisibility(fri[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        fri[k].setVisibility(fri[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (phNo[i].equals("토")) {

                                                                for (int j = 0; j < sat.length; j++) {
                                                                    if (phNo[i + 1].equals(sat[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(sat[k].getText().toString())) {
                                                                            sat[k].setVisibility(sat[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        sat[k].setVisibility(sat[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (phNo[i].equals("일")) {

                                                                for (int j = 0; j < sun.length; j++) {
                                                                    if (phNo[i + 1].equals(sun[j].getText().toString())) {
                                                                        int k = j;
                                                                        while (!phNo[i + 2].equals(sun[k].getText().toString())) {
                                                                            sun[k].setVisibility(sun[k].VISIBLE);
                                                                            k++;
                                                                        }
                                                                        sun[k].setVisibility(sun[k].VISIBLE);
                                                                        break;
                                                                    }
                                                                }
                                                            }


                                                        }

                                                    }
                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
        requestDialog = new Dialog(ItemActivity.this);

        requestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestDialog.setCancelable(false);
        requestDialog.setContentView(R.layout.request_dialog);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPopup();


            }
        });


    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void requestPopup() {
        requestDialog.show();
        Button obtn = requestDialog.findViewById(R.id.o);
        obtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    EditText time_et = (EditText) requestDialog.findViewById(R.id.time_et);
                    EditText memo_et = (EditText) requestDialog.findViewById(R.id.memo_et);
                    time = time_et.getText().toString();
                    memo = memo_et.getText().toString();
                } catch (Exception e) {
                    time = "";
                    memo = "";
                }
                if (time.equals("") || memo.equals("")) {
                    startToast("빠짐없이 작성해 주세요.");
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    customerID = user.getUid();

                    dealID = customerID.toString() + "_" + itemID;

                    Deal deal = new Deal(dealID, itemID, sellerID, customerID, time, memo, success);
                    db.collection("Deal").document(customerID)
                            .set(deal)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (college.equals("영남대")) {
                                        Intent intent = new Intent(ItemActivity.this, MainActivity.class);
                                        intent.putExtra("college", "영남대");
                                        startActivity(intent);
                                        finish();
                                    } else if (college.equals("경북대")) {
                                        Intent intent = new Intent(ItemActivity.this, MainActivity.class);
                                        intent.putExtra("college", "경북대");
                                        startActivity(intent);
                                        finish();
                                    }
                                    startToast("거래가 성사되면 알려드릴게요!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    startToast("erro: add User document");
                                }
                            });
                }
            }
        });

        Button xbtn = requestDialog.findViewById(R.id.x);
        xbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDialog.dismiss();
            }
        });
    }
}
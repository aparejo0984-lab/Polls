package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polls.R;

public class RatePollActivity extends AppCompatActivity {

    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private TextView pollQuestion;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_poll);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        intent = getIntent();

        pollQuestion = findViewById(R.id.tvPollQuestion);
        pollQuestion.setText(intent.getStringExtra("question"));

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);

        ColorStateList colorClicked = ColorStateList.valueOf(getResources().getColor(R.color.purple_200));
        ColorStateList colorUnClicked = ColorStateList.valueOf(getResources().getColor(R.color.lavender));

        btnOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOption1.setBackgroundTintList(colorClicked);
                btnOption2.setBackgroundTintList(colorUnClicked);
                btnOption3.setBackgroundTintList(colorUnClicked);
                btnOption4.setBackgroundTintList(colorUnClicked);
            }
        });

        btnOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOption2.setBackgroundTintList(colorClicked);

                btnOption1.setBackgroundTintList(colorUnClicked);
                btnOption3.setBackgroundTintList(colorUnClicked);
                btnOption4.setBackgroundTintList(colorUnClicked);
            }
        });

        btnOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOption3.setBackgroundTintList(colorClicked);

                btnOption1.setBackgroundTintList(colorUnClicked);
                btnOption2.setBackgroundTintList(colorUnClicked);
                btnOption4.setBackgroundTintList(colorUnClicked);
            }
        });

        btnOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOption4.setBackgroundTintList(colorClicked);

                btnOption1.setBackgroundTintList(colorUnClicked);
                btnOption2.setBackgroundTintList(colorUnClicked);
                btnOption3.setBackgroundTintList(colorUnClicked);
            }
        });


    }
}
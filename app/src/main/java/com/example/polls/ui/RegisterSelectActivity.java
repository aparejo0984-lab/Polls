package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.polls.R;

public class RegisterSelectActivity extends AppCompatActivity {

    ImageView travel, science, finance, fashion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);

        Button btnFinish = findViewById(R.id.btnFinish);

        travel = findViewById(R.id.imageTravel);
        science = findViewById(R.id.imageScience);
        finance = findViewById(R.id.imageFinance);
        fashion = findViewById(R.id.imageFashion);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterSelectActivity.this, RegisterConfirmActivity.class));
            }
        });
    }
}
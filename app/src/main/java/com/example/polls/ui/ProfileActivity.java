package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.polls.R;

public class ProfileActivity extends AppCompatActivity {

    Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        EditText etUsername = findViewById(R.id.tietName);
        EditText etPassword = findViewById(R.id.tiePassword);

        Button btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                Toast.makeText(context, "Profile is successfully updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
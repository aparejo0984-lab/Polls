package com.example.polls.ui;

import static com.example.polls.utils.Utils.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polls.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView tvLoginUp = findViewById(R.id.tvLoginUp);
        EditText etEmail = findViewById(R.id.tietEmail);
        Button btnSend = findViewById(R.id.btnSend);

        tvLoginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();

                if (!validateInputs(email)) return;

                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

    }

    private boolean validateInputs(String email){

        if (email.isEmpty()){
            Toast.makeText(this, getString(R.string.email_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)){
            Toast.makeText(this, getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
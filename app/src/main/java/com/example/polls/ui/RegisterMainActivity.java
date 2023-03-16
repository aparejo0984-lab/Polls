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

public class RegisterMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        TextView tvLoginUp = findViewById(R.id.tvLoginUp);
        EditText etUsername = findViewById(R.id.tietName);
        EditText etEmail = findViewById(R.id.tietEmail);
        EditText etPassword = findViewById(R.id.tiePassword);

        Button btnRegister = findViewById(R.id.btnRegister);

        tvLoginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterMainActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!validateInputs(username,email,password)) return;

                startActivity(new Intent(RegisterMainActivity.this, RegisterPhotoActivity.class));
            }
        });
    }

    private boolean validateInputs(String username,String email,String password){

        if (username.isEmpty()){
            Toast.makeText(this, getString(R.string.name_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()){
            Toast.makeText(this, getString(R.string.email_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)){
            Toast.makeText(this, getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()){
            Toast.makeText(this, getString(R.string.password_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
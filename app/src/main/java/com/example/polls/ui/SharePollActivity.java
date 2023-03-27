package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.polls.R;

public class SharePollActivity extends AppCompatActivity {

    private Button shareFacebook, shareInstagram, shareWhatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_poll);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        shareFacebook = findViewById(R.id.btnFacebook);
        shareInstagram = findViewById(R.id.btnInstagram);
        shareWhatsapp = findViewById(R.id.btnWhatsapp);

        shareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.share_facebook_message), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SharePollActivity.this, MainActivity.class));
            }
        });

        shareInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.share_instagram_message), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SharePollActivity.this, MainActivity.class));
            }
        });

        shareWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.share_whatsapp_message), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SharePollActivity.this, MainActivity.class));
            }
        });
    }
}
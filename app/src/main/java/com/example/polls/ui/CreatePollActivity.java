package com.example.polls.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.polls.R;

public class CreatePollActivity extends AppCompatActivity {

    private AlertDialog dialogDeletePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageDelete = findViewById(R.id.imageDelete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteNoteDialog();
            }
        });
    }

    private void showDeleteNoteDialog() {
        if(dialogDeletePoll == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreatePollActivity.this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_delete,
                            (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer));

            builder.setView(view);
            dialogDeletePoll = builder.create();

            if(dialogDeletePoll.getWindow() != null) {
                dialogDeletePoll.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteCancel).setOnClickListener(v -> dialogDeletePoll.dismiss());
        }

        dialogDeletePoll.show();
    }
}
package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polls.R;
import com.example.polls.handler.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RatePollActivity extends AppCompatActivity {

    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private TextView pollQuestion;
    private Intent intent;
    private int pollId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_poll);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        intent = getIntent();

        pollQuestion = findViewById(R.id.tvPollQuestion);
        pollQuestion.setText(intent.getStringExtra("question"));

        pollId = intent.getIntExtra("pollID", 0);

        new ShowPollDetail().execute(pollId);

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

    private class ShowPollDetail extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_POLL + "/" + params[0];
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                Log.i("URL", str);
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));

                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONObject(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null)
            {
                try {
                    Log.e("App", "Success: " + response.getString("status") );
                    Log.e("App", "Data: " + response.getString("data") );

                    Boolean status=Boolean.valueOf(response.getString("status") );

                    if (status == true) {
                        JSONObject pollJson = new JSONObject(response.getString("data"));
                        JSONObject answerJson = new JSONObject(pollJson.getString("answers"));

                        Log.e("App", "Question: " + pollJson.getString("question") );
                        Log.e("App", "option_text1: " + answerJson.getString("option_text1") );
                        Log.e("App", "option_text2: " + answerJson.getString("option_text2") );
                        Log.e("App", "option_text3: " + answerJson.getString("option_text3") );
                        Log.e("App", "option_text4: " + answerJson.getString("option_text4") );

                        btnOption1.setText(answerJson.getString("option_text1"));
                        btnOption2.setText(answerJson.getString("option_text2"));

                        String option3 = answerJson.getString("option_text3");
                        String option4 = answerJson.getString("option_text4");

                        if (option3.equals("null")) {
                            btnOption3.setVisibility(View.GONE);
                        } else {
                            btnOption3.setText(option3);
                        }

                        if (option4.equals("null")) {
                            btnOption4.setVisibility(View.GONE);
                        } else {
                            btnOption4.setText(option4);
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), response.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }
}
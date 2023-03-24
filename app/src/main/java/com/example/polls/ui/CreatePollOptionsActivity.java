package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.polls.R;
import com.example.polls.handler.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreatePollOptionsActivity extends AppCompatActivity {

    EditText etOption1, etOption2, etOption3, etOption4;
    String option1, option2, option3, option4;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll_options);

        Intent intent = getIntent();
        int pollID = intent.getIntExtra("pollID", 0);

        etOption1 = findViewById(R.id.tietOption1);
        etOption2 = findViewById(R.id.tietOption2);
        etOption3 = findViewById(R.id.tietOption3);
        etOption4 = findViewById(R.id.tietOption4);

        Button btnNext = findViewById(R.id.btnFinish);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option1 = etOption1.getText().toString();
                option2 = etOption2.getText().toString();
                option3 = etOption3.getText().toString();
                option4 = etOption4.getText().toString();

                if (!validaInputs(option1,option2)) return;

                new CreatePollAnswer().execute(pollID,option1,option2,option3,option4);
            }
        });
    }

    private boolean validaInputs(String option1, String option2) {
        if (option1.isEmpty()){
            Toast.makeText(this, getString(R.string.option1_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (option2.isEmpty()){
            Toast.makeText(this, getString(R.string.option2_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class CreatePollAnswer extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_POLL_ANSWER;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("poll_id", params[0]);
                jsonParam.put("option_text1", params[1]);
                jsonParam.put("option_text2", params[2]);

                String option3 = (String) params[3];
                String option4 = (String) params[4];

                if (option3.trim().length() > 0) {
                    jsonParam.put("option_text3", option3);
                }

                if (option3.trim().length() > 0) {
                    jsonParam.put("option_text4", option4);
                }

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                if (100 <= conn.getResponseCode() && conn.getResponseCode() < 400) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

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
            progressBar.setVisibility(View.GONE);

            if(response != null)
            {
                try {
                    Log.e("App", "Success: " + response.getString("status") );
                    Log.e("App", "Message: " + response.getString("message") );
                    Log.e("App", "Data: " + response.getString("data") );

                    Boolean status=Boolean.valueOf(response.getString("status") );

                    if (status == true) {
                        Toast.makeText(getApplicationContext(), response.getString("message"),Toast.LENGTH_SHORT).show();

                        JSONObject pollJson = new JSONObject(response.getString("data"));

                        Log.e("App", "POLL ID: " + pollJson.getInt("poll_id")  );
                        Log.e("App", "Option Text1: " + pollJson.getString("option_text1") );
                        Log.e("App", "Option Text2: " + pollJson.getString("option_text2") );

                        if (pollJson.getString("option_text3").trim().length() > 0) {
                            Log.e("App", "Option Text3: " + pollJson.getString("option_text3") );
                        }

                        if (pollJson.getString("option_text4").trim().length() > 0) {
                            Log.e("App", "Option Text4: " + pollJson.getString("option_text4") );
                        }

                        Intent intent = new Intent(CreatePollOptionsActivity.this, MainActivity.class);
                        startActivity(intent);
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
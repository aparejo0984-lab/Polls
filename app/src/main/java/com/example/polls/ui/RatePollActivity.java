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
import com.example.polls.model.User;
import com.example.polls.service.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RatePollActivity extends AppCompatActivity {

    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private TextView pollQuestion, rateLabel;
    private Intent intent;
    private Boolean isView;
    private int pollId;
    private int pollAnswerId, givenAnswerId = 0;
    ImageView imageBack, imageRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_poll);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();


        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        intent = getIntent();
        isView = intent.getBooleanExtra("isView", false);
        pollId = intent.getIntExtra("pollID", 0);

        pollQuestion = findViewById(R.id.tvPollQuestion);
        pollQuestion.setText(intent.getStringExtra("question"));

        new ShowPollDetail().execute(pollId);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        imageRate = findViewById(R.id.imageRate);

        if (isView) {
            rateLabel = findViewById(R.id.tvRatePoll);
            rateLabel.setText("View Poll");
            imageRate.setVisibility(View.GONE);
            btnOption1.setClickable(false);
            btnOption2.setClickable(false);
            btnOption3.setClickable(false);
            btnOption4.setClickable(false);
        } else {
            ColorStateList colorClicked = ColorStateList.valueOf(getResources().getColor(R.color.purple_200));
            ColorStateList colorUnClicked = ColorStateList.valueOf(getResources().getColor(R.color.lavender));

            btnOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    givenAnswerId = 1;

                    btnOption1.setBackgroundTintList(colorClicked);
                    btnOption2.setBackgroundTintList(colorUnClicked);
                    btnOption3.setBackgroundTintList(colorUnClicked);
                    btnOption4.setBackgroundTintList(colorUnClicked);
                }
            });

            btnOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    givenAnswerId = 2;

                    btnOption2.setBackgroundTintList(colorClicked);

                    btnOption1.setBackgroundTintList(colorUnClicked);
                    btnOption3.setBackgroundTintList(colorUnClicked);
                    btnOption4.setBackgroundTintList(colorUnClicked);
                }
            });

            btnOption3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    givenAnswerId = 3;

                    btnOption3.setBackgroundTintList(colorClicked);

                    btnOption1.setBackgroundTintList(colorUnClicked);
                    btnOption2.setBackgroundTintList(colorUnClicked);
                    btnOption4.setBackgroundTintList(colorUnClicked);
                }
            });

            btnOption4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    givenAnswerId = 4;

                    btnOption4.setBackgroundTintList(colorClicked);

                    btnOption1.setBackgroundTintList(colorUnClicked);
                    btnOption2.setBackgroundTintList(colorUnClicked);
                    btnOption3.setBackgroundTintList(colorUnClicked);
                }
            });

            imageRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CreatePollRate().execute(user.getId(),pollId, pollAnswerId, givenAnswerId);
                }
            });
        }
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

                        pollAnswerId = answerJson.getInt("id");

                        String option1 = answerJson.getString("option_text1");
                        String option2 = answerJson.getString("option_text2");
                        String option3 = answerJson.getString("option_text3");
                        String option4 = answerJson.getString("option_text4");

                        if (answerJson.getString("option1_vote_percentage").equals("null") == false) {
                            option1 = option1 + " - " + answerJson.getString("option1_vote_percentage");
                        }

                        if (answerJson.getString("option2_vote_percentage").equals("null") == false) {
                            option2 = option2 + " - " + answerJson.getString("option2_vote_percentage");
                        }

                        btnOption1.setText(option1);
                        btnOption2.setText(option2);

                        if (option3.equals("null")) {
                            btnOption3.setVisibility(View.GONE);
                        } else {
                            if (answerJson.getString("option3_vote_percentage").equals("null") == false) {
                                option3 = option3 + " - " + answerJson.getString("option3_vote_percentage");
                            }

                            btnOption3.setText(option3);
                        }

                        if (option4.equals("null")) {
                            btnOption4.setVisibility(View.GONE);
                        } else {
                            if (answerJson.getString("option4_vote_percentage").equals("null") == false) {
                                option4 = option4 + " - " + answerJson.getString("option4_vote_percentage");
                            }

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

    private class CreatePollRate extends AsyncTask<Object, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_POLL_VOTE;
            String method=  "POST";
            BufferedReader bufferedReader = null;

            try
            {
                Log.e("App", "API URL: " + str );
                Log.e("App", "API Method: " + method );

                URL url = new URL(str);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user_id", params[0]);
                jsonParam.put("poll_id", params[1]);
                jsonParam.put("poll_answer_id", params[2]);
                jsonParam.put("given_answer", params[3]);

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

                        Log.e("App", "POLL ID: " + pollJson.getInt("id") );

                        Intent intent = new Intent(RatePollActivity.this, MainActivity.class);
                        intent.putExtra("pollID", pollJson.getInt("id"));
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
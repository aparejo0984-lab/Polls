package com.example.polls.ui;

import static com.example.polls.utils.Utils.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private ProgressBar progressBar;

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

                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

                new UserPasswordReset().execute(email);
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

    private class UserPasswordReset extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_PASSWORD_RESET;
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
                jsonParam.put("email", (String) params[0]);

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

                    progressBar.setVisibility(View.GONE);

                    Boolean status=Boolean.valueOf(response.getString("status") );

                    if (status == true) {
                        Toast.makeText(getApplicationContext(), response.getString("message"),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
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
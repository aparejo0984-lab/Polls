package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        EditText etUsername = findViewById(R.id.tietName);
        EditText etPassword = findViewById(R.id.tiePassword);

        Button btnUpdate = findViewById(R.id.btnUpdate);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        etUsername.setText(user.getName());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!validaInputs(username)) return;

                new UserProfile().execute(user.getId(), username,password);
            }
        });
    }

    private boolean validaInputs(String username) {
        if (username.isEmpty()){
            Toast.makeText(this, getString(R.string.name_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class UserProfile extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_ALL_USER + "/" + params[0];
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                String name = (String) params[1];
                String password = (String) params[2];

                if (name.trim().length() > 0) {
                    jsonParam.put("name", name);
                }

                if (password.trim().length() > 0) {
                    jsonParam.put("password", password);
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

                        JSONObject userJson = new JSONObject(response.getString("data"));

                        Log.e("App", "ID: " + userJson.getInt("id") );
                        Log.e("App", "NAME: " + userJson.getString("name") );
                        Log.e("App", "Email: " + userJson.getString("email") );

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("email")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
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
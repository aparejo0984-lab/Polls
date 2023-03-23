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
import com.example.polls.model.User;
import com.example.polls.service.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterMainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    EditText etName,etEmail,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        TextView tvLoginUp = findViewById(R.id.tvLoginUp);
        etName= findViewById(R.id.tietName);
        etEmail = findViewById(R.id.tietEmail);
        etPassword = findViewById(R.id.tiePassword);

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

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!validateInputs(name,email,password)) return;

                new UserRegister().execute(name, email,password);
            }
        });
    }

    private boolean validateInputs(String name,String email,String password){

        if (name.isEmpty()){
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

    private class UserRegister extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_REGISTER;
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
                jsonParam.put("name", (String) params[0]);
                jsonParam.put("email", (String) params[1]);
                jsonParam.put("password", (String) params[2]);

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                BufferedReader br = null;
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
                    Log.e("App", "data: " + response.getString("data") );

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

                        startActivity(new Intent(RegisterMainActivity.this, RegisterPhotoActivity.class));
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
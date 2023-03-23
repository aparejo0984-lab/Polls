package com.example.polls.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.polls.R;
import com.example.polls.handler.APIClient;
import com.example.polls.model.User;
import com.example.polls.service.SharedPrefManager;
import com.example.polls.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreatePollActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private AlertDialog dialogDeletePoll;
    private int categoryId = 0;
    private int enableId = 1;
    private String questionTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageDelete = findViewById(R.id.imageDelete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteNoteDialog();
            }
        });

        EditText questionText = findViewById(R.id.inputNoteText);

        Switch switchEnable = (Switch) findViewById(R.id.switchEnable);
        switchEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableId = 1;
                } else {
                    enableId = 0;
                }
                Log.e("ENABLED", String.valueOf(enableId));
            }
        });

        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                Log.e("ITEM SELECTED", item);
                categoryId = Utils.getCategoryId(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                questionTxt = questionText.getText().toString();

                if (!validaInputs(questionTxt,categoryId)) return;

                new CreatePoll().execute(user.getId(),categoryId,enableId, questionTxt);
            }
        });
    }

    private boolean validaInputs(String question, int categoryId) {
        if (categoryId == 0){
            Toast.makeText(this, getString(R.string.select_a_category), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (question.isEmpty()){
            Toast.makeText(this, getString(R.string.question_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    private class CreatePoll extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_POLL;
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
                jsonParam.put("user_id", params[0]);
                jsonParam.put("category_id", params[1]);
                jsonParam.put("enable", params[2]);
                jsonParam.put("question", params[3]);

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

                        Log.e("App", "ID: " + pollJson.getInt("id") );
                        Log.e("App", "CATEGORY ID: " + pollJson.getInt("category_id")  );
                        Log.e("App", "Question: " + pollJson.getString("question") );

                        startActivity(new Intent(CreatePollActivity.this, CreatePollOptionsActivity.class));
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
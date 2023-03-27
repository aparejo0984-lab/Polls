package com.example.polls.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.polls.R;
import com.example.polls.handler.APIClient;
import com.example.polls.model.Poll;
import com.example.polls.service.PollRVAdapter;
import com.example.polls.service.PollRVListener;
import com.example.polls.service.SharedPrefManager;
import com.example.polls.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PollRVListener {

    private RecyclerView pollsRecyclerView;
    private PollRVAdapter pollRVAdapter;
    ArrayList<Poll> pollArrayList = new ArrayList<Poll>();
    private int pollClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddPollMain= findViewById(R.id.imageAddPollMain);
        imageAddPollMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreatePollActivity.class));
            }
        });

        pollsRecyclerView = findViewById(R.id.pollsRecyclerView);
        pollRVAdapter = new PollRVAdapter(pollArrayList, this);
        new ShowPollList().execute();

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pollRVAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pollArrayList.size() != 0) {
                    pollRVAdapter.searchPoll(s.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.action_logout:
                Toast.makeText(this, "Successfully logout", Toast.LENGTH_SHORT).show();

                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPollClicked(Poll poll, int position) {
        pollClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreatePollActivity.class);
        Log.i("Onclicked POLL ID: ", poll.getQuestion());
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("pollID", poll.getId());
        intent.putExtra("categoryID", poll.getCategoryId());
        intent.putExtra("question", poll.getQuestion());
        intent.putExtra("isEnable", poll.getEnableId());
        startActivity(intent);
    }

    @Override
    public void onShareClicked(Poll poll, int position) {
        Intent intent = new Intent(getApplicationContext(), SharePollActivity.class);
        startActivity(intent);
    }

    @Override
    public void onVoteClicked(Poll poll, int position) {
        Intent voteIntent = new Intent(getApplicationContext(), RatePollActivity.class);
        Log.e("App", "Poll Question: " + poll.getQuestion() );
        voteIntent.putExtra("pollID", poll.getId());
        voteIntent.putExtra("categoryID", poll.getCategoryId());
        voteIntent.putExtra("question", poll.getQuestion());
        voteIntent.putExtra("isEnable", poll.getEnableId());
        startActivity(voteIntent);
    }

    private class ShowPollList extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {

            String str= APIClient.API_POLL;
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
                        JSONArray jsonarray = new JSONArray(response.getString("data"));

                        if (jsonarray.length() == 0) return;

                        for(int item=0; item<jsonarray.length(); item++){
                            JSONObject obj = jsonarray.getJSONObject(item);

                            Integer id = Integer.valueOf(obj.getString("id"));
                            Integer userId = Integer.valueOf(obj.getString("user_id"));
                            Integer categoryId = Integer.valueOf(obj.getString("category_id"));
                            Integer enable = Integer.valueOf(obj.getString("enable"));
                            String question = obj.getString("question");
                            String createdAt = obj.getString("created_at");

                            JSONObject userJson = new JSONObject(obj.getString("user"));

                            Log.e("App", "ID: " + id );
                            Log.e("App", "User ID: " + userId);
                            Log.e("App", "Category ID: " + categoryId);
                            Log.e("App", "Question: " + question );
                            Log.e("App", "Username: " + userJson.getString("name") );
                            Log.e("App", "CreatedAt: " + createdAt.substring(0,10) );

                            String timeAgo = Utils.calculateHumanFriendlyTimeAgo(createdAt);
                            Log.e("App", "Time Ago: " + timeAgo );

                            pollArrayList.add(
                                    new Poll(id, userId, categoryId, enable, question, userJson.getString("name"), timeAgo)
                            );

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                            pollsRecyclerView.setLayoutManager(linearLayoutManager);
                            pollsRecyclerView.setAdapter(pollRVAdapter);
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
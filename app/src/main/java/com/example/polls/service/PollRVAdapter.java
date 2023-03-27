package com.example.polls.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polls.R;
import com.example.polls.model.Poll;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PollRVAdapter extends RecyclerView.Adapter<PollRVAdapter.ViewHolder>{

    private ArrayList<Poll> pollArrayList;
    private Timer timer;
    private ArrayList<Poll> pollsSource;
    private PollRVListener pollRVListener;

    // Constructor
    public PollRVAdapter(ArrayList<Poll> pollArrayList, PollRVListener pollRVListener) {
        this.pollArrayList = pollArrayList;
        this.pollRVListener = pollRVListener;
        pollsSource = pollArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_poll_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poll model = pollArrayList.get(position);
        holder.userNameTV.setText(model.getUsername());
        holder.questionTV.setText("" + model.getQuestion());
        holder.timeAgoTV.setText("" + model.getTimeAgo());
        holder.layoutPoll.setOnClickListener(v -> pollRVListener.onPollClicked(model,position));
    }

    @Override
    public int getItemCount() {
        return pollArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userNameTV;
        private final TextView questionTV;
        private final TextView timeAgoTV;
        CardView layoutPoll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.idTVUserName);
            questionTV = itemView.findViewById(R.id.idTVPollQuestion);
            timeAgoTV = itemView.findViewById(R.id.idTVTimeAgo);
            layoutPoll = itemView.findViewById(R.id.layoutPoll);
        }
    }

    public void searchPoll(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()) {
                    pollArrayList = pollsSource;
                } else {
                    ArrayList<Poll> temp = new ArrayList<>();
                    for(Poll pollItem : pollsSource) {
                        Log.i("Search string function", searchKeyword.toLowerCase());
                        if(pollItem.getQuestion().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                pollItem.getUsername().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            Log.i("Added poll item", pollItem.getQuestion());
                            temp.add(pollItem);
                        }
                    }
                    pollArrayList = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }
}

package com.example.polls.service;

import com.example.polls.model.Poll;

public interface PollRVListener {
        void onPollClicked(Poll poll, int position);
        void onShareClicked(Poll poll, int position);
        void onVoteClicked(Poll poll, int position);
    }


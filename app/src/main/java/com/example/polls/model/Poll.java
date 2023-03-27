package com.example.polls.model;

public class Poll {

    private int id,userId,categoryId, enableId;
    private String question, username, timeAgo;

    public Poll(int id, int userId, int categoryId, int enableId, String question, String username, String timeAgo) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.enableId = enableId;
        this.question = question;
        this.username = username;
        this.timeAgo = timeAgo;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getEnableId() { return enableId; }

    public void setEnableId(int enableId) { this.enableId = enableId; }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}

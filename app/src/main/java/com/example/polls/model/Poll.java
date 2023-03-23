package com.example.polls.model;

public class Poll {

    private int id,userId,categoryId;
    private String question;

    public Poll(int id, int userId, int categoryId, String question) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.question = question;
    }

    public int getId() {
        return id;
    }

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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

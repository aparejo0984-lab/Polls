package com.example.polls.handler;

public class APIClient {

    private static final String API_BASE = "http://10.0.2.2:8000/api/";
    //http://127.0.0.1:8000/api/

    public static final String API_REGISTER = API_BASE + "register";
    public static final String API_LOGIN = API_BASE + "login";
    public static final String API_PASSWORD_RESET = API_BASE + "password/reset";
    public static final String API_ALL_USER = API_BASE + "user";
    public static final String API_POLL = API_BASE + "poll";
}

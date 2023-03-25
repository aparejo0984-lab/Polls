package com.example.polls.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static int getCategoryId(String item) {
        switch(item) {
            case "Fashion":
                return 1;
            case "Finance":
                return 2;
            case "Science":
                return 3;
            case "Travel":
                return 4;
            default:
                return 0;
        }
    }

    public static String calculateHumanFriendlyTimeAgo(String pastTime) {
        String timeAgo = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date past = format.parse(pastTime);
            Date now = new Date();

            if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) > 0 && TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 60) {
                timeAgo =  TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " mins ago";
            }  else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) < 24) {
                timeAgo =  Math.abs(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime())) + " hrs ago";
            } else {
                timeAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago";
            }

            System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime()) + " milliseconds ago");
            System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
            System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
            System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");
            System.out.println("Time ago result: " + timeAgo);


        } catch (Exception j) {
            j.printStackTrace();
        }
        return timeAgo;
    }
}

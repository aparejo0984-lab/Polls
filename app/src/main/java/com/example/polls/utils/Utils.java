package com.example.polls.utils;

import android.text.TextUtils;
import android.util.Patterns;

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
}

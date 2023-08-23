package com.pos.pointofsale.controller.registerformcontroller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerValidation {
    static boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    static boolean isValidPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

}

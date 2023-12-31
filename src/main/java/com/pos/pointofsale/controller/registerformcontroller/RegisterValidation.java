package com.pos.pointofsale.controller.registerformcontroller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {
    public static boolean isvalidEmail(String email){
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    public static boolean invalidPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public static boolean invalidMobileNumber(String mobileNum){
        Pattern pattern = Pattern.compile("^(?:\\+94|0)[1-9][0-9]{8}$");
        Matcher matcher = pattern.matcher(mobileNum);
        return !matcher.matches();
    }

}

package com.tellgear.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tellgear.util.Constants.*;

public class Utilities {


    public static String getDate(){
        DateFormat format = new SimpleDateFormat("dd-MM-yy hh:mm");
        return format.format(new Date());
    }

    //COLOR SCREEN
    public static String red(String text){
        return ANSI_RED+text+ANSI_RESET;
    }

    public static String blue(String text){
        return ANSI_BLUE+text+ANSI_RESET;
    }

    public static String green(String text){
        return ANSI_GREEN+text+ANSI_RESET;
    }

    public static String black(String text){
        return ANSI_BLACK+ANSI_WHITE_BACKGROUND+text+ANSI_RESET;
    }

    public static String cyan(String text){
        return ANSI_CYAN+text+ANSI_RESET;
    }

}

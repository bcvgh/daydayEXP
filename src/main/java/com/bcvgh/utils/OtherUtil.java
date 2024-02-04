package com.bcvgh.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherUtil {
    private static final Logger LOGGER = LogManager.getLogger(OtherUtil.class.getName());
    public static String getRandomString(int length) {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static <K,V> K getKey(Map<K,V> map ,V value){
        for (Map.Entry<K,V> entry : map.entrySet()){
            if (entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    public static Boolean checkUrl(String url){
        Pattern urlPat = Pattern.compile("^(https?://)",Pattern.DOTALL);
        Matcher urlMatch = urlPat.matcher(url);
        if (!urlMatch.find()){
            return false;
        }
        return true;
    }














































}

package com.bcvgh.utils;

import java.util.Random;

public class Utils {
    public static String getRandomString(int length) {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String getDnsCommand(String dnsUrl){
//        PocUtil.dnsUrl = Utils.getRandomString(4)+"."+dnsUrl;
        return "ping -nc 1 "+dnsUrl;
    }
}

package com.bcvgh.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import org.apache.commons.text.StringEscapeUtils;

public class Encode {
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String Base64Encode(String text) {
        try {
            byte[] textByte = text.getBytes("UTF-8");
            String encodedText = encoder.encodeToString(textByte);
            return encodedText;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
        }
        return "error";
    }

    public static String Base64Decode(String encodedText) {
        try {
            String text = new String(decoder.decode(encodedText), "UTF-8");
            //System.out.println(text);
            return text;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
        }
        return "error";
    }

    public static String HtmlEncode(String encodedTest) {
        String encodedHtml = StringEscapeUtils.escapeHtml4(encodedTest);
        return encodedHtml;
    }

    public static String HtmlDeocde(String encodedText){
        String decodedHtml = StringEscapeUtils.unescapeHtml4(encodedText);
        return decodedHtml;
    }

    public static String UrlEncode(String original) throws UnsupportedEncodingException {
        StringBuilder encoded = new StringBuilder();

        for (char c : original.toCharArray()) {
            encoded.append('%');
            encoded.append(String.format("%02X", (int) c));
        }

        return encoded.toString();
    }

    public static String UrlDecode(String encoded) throws UnsupportedEncodingException {
        return URLDecoder.decode(encoded, "UTF-8");
    }
}



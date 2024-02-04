package com.bcvgh.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class EncodeUtil {
    private static final Logger LOGGER = LogManager.getLogger(EncodeUtil.class.getName());
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();


    public static <T> T Base64Encode(T text) {
        T encodedText = null;
        if (text instanceof byte[]){
            encodedText = (T) Base64.getEncoder().encode((byte[]) text);
        }else if (text instanceof String){
            byte[] textByte = new byte[0];
            try {
                textByte = ((String) text).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
            encodedText = (T) encoder.encodeToString(textByte);
        }
        return encodedText;
    }

    public static <T> T Base64Decode(T encodedText) {
        T Text = null;
        if (encodedText instanceof byte[]){
            Text = (T) Base64.getDecoder().decode((byte[]) encodedText);
        }else if (encodedText instanceof String){
            try {
                Text = (T) new String(decoder.decode((String) encodedText), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return Text;
    }


    public static  String HtmlEncode(String text) {
        String encodedHtml = StringEscapeUtils.escapeHtml4(text);
        return encodedHtml;
    }

    public static String HtmlDecode(String encodedText){
        String decodedHtml = StringEscapeUtils.unescapeHtml4(encodedText);
        return decodedHtml;
    }

    public static String UrlAllEncode(String text){
        String encodedText = null;
        StringBuilder encoded = new StringBuilder();

        for (char c : text.toCharArray()) {
            encoded.append('%');
            encoded.append(String.format("%02X", (int) c));
        }
        encodedText = encoded.toString();

        return encodedText;
    }

    public static String UrlEncode(String text){
        String  encodedText = URLEncoder.encode(text);
        return encodedText;
    }

    public static String UrlDecode(String encodedText){
        String Text = null;
        try {
            Text = URLDecoder.decode(encodedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return Text;
    }

    public static String HexEncode(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    public static byte[] HexDecode(String encodedText) {
        try {
            return DatatypeConverter.parseHexBinary(encodedText);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public static byte[] GzipEncode(byte[] bytes){
        // 构造一个GZIP格式的字节数组，将字节数组存储在GZIP数据块中
        byte[] maliciousBytes = bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(baos);
            gzipOutputStream.write(maliciousBytes);
            gzipOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] gzipBytes = baos.toByteArray();
        return gzipBytes;
    }

}



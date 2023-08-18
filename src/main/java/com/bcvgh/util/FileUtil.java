package com.bcvgh.util;

import java.io.*;

public class FileUtil {
    public static String FileRead(String fileName){
        BufferedReader in = null;
        String strs = "";
        String str = "";
        try {
            in = new BufferedReader(new FileReader(fileName));
            while ((str = in.readLine()) != null) {
                strs = strs+str;
            }
            return strs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] FileList(String filePath){
        File file = new File(filePath);
        return file.list();
    }
}

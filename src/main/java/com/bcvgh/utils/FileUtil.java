package com.bcvgh.utils;

import java.io.*;

public class  FileUtil {
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
            PromptUtil.Alert("警告","请正确选择需要检测的漏洞名称");
            return null;
        } catch (IOException e) {
            PromptUtil.Alert("警告","文件读取出错啦!");
            return null;
        }
    }

    public static String[] FileList(String filePath){
        File file = new File(filePath);
        return file.list();
    }
}

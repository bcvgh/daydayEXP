package com.bcvgh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.payloads.VulPocTemplateImp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonUtil {
    public static VulPocTemplateImp JsonToObject(String poc){
        return JSON.parseObject(poc, VulPocTemplateImp.class);
    }

    public static String ObjectToJson(JSONObject poc){
        return  JSON.toJSONString(poc);
    }

    public static JSONObject StringToJson(String poc){
        return JSONObject.parseObject(poc);
    }

//    public static String FileRead(String fileName){
//        BufferedReader in = null;
//        String strs = "";
//        String str = "";
//        try {
//            in = new BufferedReader(new FileReader(fileName));
//            while ((str = in.readLine()) != null) {
//                strs = strs+str;
//            }
//            return strs;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

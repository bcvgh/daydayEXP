package com.bcvgh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class ApiUtil {

    public static Map header = new HashMap<>(JSONObject.parseObject(" {\n" +
            "      \"content-type\":\"text/plain\",\n" +
            "      \"User-Agent\":\"Mozilla/5.0 (Linux;\"\n" +
            "    }"));

    public static Boolean ceyeDnslog(String dnslog ,String apiToken){
       try{
           Thread.sleep(5000);
           String apiUrl = String.format("http://api.ceye.io/v1/records?token=%s&type=dns&filter=%s", apiToken, dnslog);
           Response res = HttpTools.get(apiUrl,(HashMap<String, ?>) ApiUtil.header,"UTF-8");
           JSONArray data = JSONObject.parseObject(res.getText()).getJSONArray("data");
           for(int i=0;i<data.size();i++){
               if (dnslog.equals(data.getJSONObject(i).getString("name")));
               return true;
           }
       }catch (Exception e){
           PromptUtil.Alert("警告","当前api超时或不可用!");
       }
        return false;
    }


    public static boolean getDnslog(String dnslog){
        String Tags = FileUtil.FileRead(PocUtil.PocPath+"config.json");
//        String Tags = FileUtil.FileRead("D:\\comsoft\\st\\my\\daydayEXP\\daydayExp\\poc\\config.json");
        JSONObject apiJson = JSON.parseObject(Tags).getJSONObject("dnsapi");
        for (String dnsType : apiJson.keySet()){
            if (dnsType.equals("ceye")){
                String token = apiJson.getString("ceye");
                return ceyeDnslog(dnslog, token);
            }
//            if (dnsType.equals("dnslog")){
//                String token = apiJson.getString("dnslog");
//                return ceyeDnslog(dnslog, token);
//            }
        }
        return false;
    }
}

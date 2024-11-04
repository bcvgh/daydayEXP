package com.bcvgh.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static  String Path = System.getProperty("user.dir")+File.separator;
    public static  String PocPath = System.getProperty("user.dir")+ File.separator + "poc" + File.separator;
    public static  String LogPath =  System.getProperty("user.dir")+ File.separator +"log" + File.separator;
    public static  String ConfigPath = System.getProperty("user.dir")+ File.separator + "poc" + File.separator + "config.json";

    public static Integer ThreadNum = 5;
    public static final Map Header = new HashMap<>(JSONObject.parseObject(" {\n" +
            "      \"content-type\":\"text/plain\",\n" +
            "      \"User-Agent\":\"Mozilla/5.0 (Linux;\"\n" +
            "    }"));

    public static final String StringHeader = "content-type : text/plain\n User-Agent : Mozilla/5.0 (Linux;\n" ;

    public static final String ConfigContent = "{\n" +
            "  \"tagname\": {\n" +
            "    \"anheng\": \"安恒\",\n" +
            "    \"hikvision\": \"海康威视\",\n" +
            "  },\n" +
            "  \"dnsapi\": {\n" +
            "    \"ceye\": \"xxxxxxxxxxxxxxxxxxxxxxxx\"\n" +
            "  }\n" +
            "}";
}

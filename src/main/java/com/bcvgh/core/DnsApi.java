package com.bcvgh.core;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import org.apache.commons.text.StringSubstitutor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DnsApi {
    private static final Logger LOGGER = LogManager.getLogger(DnsApi.class.getName());
    private String dnsUrl;
//    private String prefix;
//    private String name;
    private String type;
    private String api;
    private String token;
    private JSONObject apiJson;

    public DnsApi(String dnsUrl, String configPath) {
        this.dnsUrl = dnsUrl;
        //支持其他ndslog平台，可能需要prefix和name
//        this.prefix = dnsUrl.split(".")[0];
//        this.name = dnsUrl.split(".")[1];
        //
        this.apiJson = getDnsApi(configPath);
        try{
            this.type = apiJson.getString("type");
            this.api = apiJson.getString("api");
            this.token = apiJson.getString("token");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    private JSONObject getDnsApi(String configPath){
        String content = FileUtil.FileRead(configPath,"");
        JSONObject apiData = null;
        try {
            apiData = JSON.parseObject(content).getJSONObject("dnslog");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return apiData;
    }

    //替换api中的参数，并获得dnslog平台结果并匹配
    public Boolean getDnsLog(){
        Map<String, String> valueMap = new HashMap<>();
        //支持其他ndslog平台，可能需要prefix和name
//        valueMap.put("{{prefix}}", this.prefix);
//        valueMap.put("{{name}}", this.name);
        //
        valueMap.put("token", this.token);
        StringSubstitutor substitutor = new StringSubstitutor(valueMap, "{{", "}}");
        String apiUrl = substitutor.replace(this.api);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        Response res = HttpTools.get(apiUrl,(HashMap<String, ?>) Constant.Header,"UTF-8");
        try {
            if (res.getCode()==200 && res.getText().toLowerCase(Locale.ROOT).contains(this.dnsUrl.toLowerCase(Locale.ROOT))){
                return true;
            }
            else if (res.getCode()!=200){
                LOGGER.warn("(api无响应)");
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return false;
    }
}

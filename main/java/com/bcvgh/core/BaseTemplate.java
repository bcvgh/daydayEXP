package com.bcvgh.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.PocUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseTemplate {
    public String Url;
    public String name;
    public String tag;
    public String type;
    public HashMap<String,Object> header;
    public JSONObject Poc;


    public BaseTemplate(String url, JSONObject poc) {
        this.Url = url;
        this.name = poc.getString("name");
        this.tag = poc.getString("tag");
        this.type = poc.getString("type");
    }

    public JSONObject replacePlaceholder(JSONObject poc, String placeholder, String input){
        try {
            String pocString = JSON.toJSONString(poc);
            pocString = pocString.replace(placeholder,input);
            poc = JSON.parseObject(pocString);
        }catch (Exception e){

        }
        return poc;
    }

    public JSONObject PlaceholderHandling(JSONObject EXP, String input){
        String MatchContent = new String();
        String type = new String();
        String encodeType = new String();
        String out = new String();
        Integer num = 0;
        String pocString = JSON.toJSONString(EXP);
        try{
            Pattern ExpPattern =Pattern.compile("\\{([a-zA-Z0-9]*):([a-zA-Z0-9]*):(\\d+)\\}" ,Pattern.DOTALL);
//            Pattern ExpPattern2 =Pattern.compile("\\{([a-zA-Z]*)\\}" ,Pattern.DOTALL);
            Matcher matcher = ExpPattern.matcher(pocString);
//            Matcher matcher2 = ExpPattern2.matcher(pocString);
            if (matcher.find()){
                MatchContent = matcher.group();
                type = matcher.group(1);
                encodeType = matcher.group(2);
                num = Integer.parseInt(matcher.group(3));
                out = PocUtil.multipleEncode(encodeType, input , num);
                pocString = pocString.replace(MatchContent,out);
            }
//            else if (matcher2.find()){
//                MatchContent = matcher2.group();
//                pocString = pocString.replace(MatchContent,input);
//            }
        }
        catch (Exception e){

        }
        return JSONObject.parseObject(pocString);
    }

}

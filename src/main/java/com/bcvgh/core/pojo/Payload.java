package com.bcvgh.core.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bcvgh.core.placeholder.*;
import com.bcvgh.utils.Constant;
import com.bcvgh.utils.FileUtil;

import java.io.File;
import java.net.URLEncoder;

public class Payload {
    public String name;
    public String tag;
    public String type;
    public Poc poc;
    public JSONObject exp;
    public String StringPayload;
//    public Input input;

    public Payload(JSONObject payload, Input input) {
        if (payload.getJSONObject("exp")!=null){
            for (String step: payload.getJSONObject("exp").keySet()){
                if (payload.getJSONObject("exp").getJSONObject(step).getString("expGet").contains("{{command}}")){
                    if (input.getCommand()!=null) input.setCommand(URLEncoder.encode(input.getCommand()));
                }
            }
        }
//        this.input = input;
        payload = PlaceHolder.dealPlaceHolder(payload,input);
        this.StringPayload = JSON.toJSONString(payload);
        this.name = payload.getString("name");
        this.tag = payload.getString("tag");
        this.type = payload.getString("type");
        this.poc = new Poc(payload.getJSONObject("poc"));
        this.exp = payload.getJSONObject("exp");
    }

    public static Payload getPayload(String tag,String name,Input input){
        String PocS = FileUtil.FileRead(Constant.PocPath+ File.separator+tag+File.separator+name+".json","");
        JSONObject PocJson = JSON.parseObject(PocS, Feature.OrderedField);
        Payload payload = new Payload(PocJson,input);
        return payload;
    }


}

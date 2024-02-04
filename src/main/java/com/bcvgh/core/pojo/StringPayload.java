package com.bcvgh.core.pojo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bcvgh.utils.Constant;
import com.bcvgh.utils.PocUtil;

import java.util.HashMap;

import static com.bcvgh.utils.PocUtil.PojoHeader_toJson;

public class StringPayload {
    private String name;
    private String tag;
    private String type;
    private HashMap<String, StringExp> exp;
    private StringPoc poc;

    public StringPayload(String name, String tag, String type, HashMap<String, StringExp> exp, StringPoc poc) {
        this.name = name;
        this.tag = tag;
        this.type = type;
        this.exp = exp;
        this.poc = poc;
    }

    public StringPayload() {

    }

    @Override
    public String toString() {
        return this.ObjectToString(this);
    }

    private String ObjectToString(StringPayload stringPayload) {
        JSONObject PocAll_JSON = new JSONObject(true);
        PocAll_JSON.put("name", stringPayload.name);
        PocAll_JSON.put("tag", stringPayload.tag);
        PocAll_JSON.put("type", stringPayload.type);
        JSONObject poc_JSON = new JSONObject(true);
        poc_JSON.put("pocGet", stringPayload.poc.getPoc_get());
        if (!stringPayload.poc.getPoc_post().isEmpty()) {
            poc_JSON.put("pocPost", stringPayload.poc.getPoc_post());
        }
        if (!stringPayload.poc.getPoc_header().isEmpty()) {
            poc_JSON.put("header", PojoHeader_toJson(stringPayload.poc.getPoc_header()));
        } else {
            poc_JSON.put("header", new JSONObject(Constant.Header));
        }
        poc_JSON.put("status_code", stringPayload.poc.getstatus_code());
        poc_JSON.put("pattern", stringPayload.poc.getPoc_pattern());
        PocAll_JSON.put("poc", poc_JSON);
        if (!stringPayload.exp.isEmpty()) {
            JSONObject AllExp = new JSONObject(true);
            for (String step : stringPayload.exp.keySet()) {
                JSONObject stepExp = new JSONObject(true);
                stepExp.put("expGet", stringPayload.exp.get(step).getExp_get());
                if (!stringPayload.exp.get(step).getExp_post().isEmpty()) {
                    stepExp.put("expPost", stringPayload.exp.get(step).getExp_post());
                }
                if (!stringPayload.exp.get(step).getExp_header().isEmpty()) {
                    stepExp.put("header", PojoHeader_toJson(stringPayload.exp.get(step).getExp_header()));
                } else {
                    stepExp.put("header", new JSONObject(Constant.Header));
                }
                stepExp.put("status_code", stringPayload.exp.get(step).getExp_status_code());
                stepExp.put("pattern", stringPayload.exp.get(step).getExp_pattern());
                AllExp.put(step, stepExp);
            }
            PocAll_JSON.put("exp", AllExp);
        }
        return JSONObject.toJSONString(PocAll_JSON, SerializerFeature.PrettyFormat);
    }


}

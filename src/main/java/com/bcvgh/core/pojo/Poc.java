package com.bcvgh.core.pojo;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Poc {
    private String pocGet;
    private Object pocPost;
    private HashMap<String,Object> header;
    private String status_code;
    private String patternText;

    public Poc(JSONObject poc) {
        this.pocGet = poc.getString("pocGet");
        this.pocPost = poc.get("pocPost");
        this.header = new HashMap(poc.getJSONObject("header"));
        this.status_code = poc.getString("status_code");
        this.patternText = poc.getString("pattern");
    }

    public String getPocGet() {
        return pocGet;
    }

    public void setPocGet(String pocGet) {
        this.pocGet = pocGet;
    }

    public Object getPocPost() {
        return pocPost;
    }

    public void setPocPost(String pocPost) {
        this.pocPost = pocPost;
    }

    public HashMap<String,Object> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String,Object> header) {
        this.header = header;
    }

    public String getstatus_code() {
        return status_code;
    }

    public void setstatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getPatternText() {
        return patternText;
    }

    public void setPatternText(String patternText) {
        this.patternText = patternText;
    }
}

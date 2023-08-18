package com.bcvgh.payloads;

import java.util.HashMap;

public class FanweiVul {
    private String name = "fanwei1";
    private String tag ="fanwei";
    private String pocGet = "/index?id=1";
    private String pocPost = "id=1";
    private HashMap<String ,String> header = (HashMap<String, String>) new HashMap<>().put("asd" ,"asd");
    private String pattern = ".*";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPocGet() {
        return pocGet;
    }

    public void setPocGet(String pocGet) {
        this.pocGet = pocGet;
    }

    public String getPocPost() {
        return pocPost;
    }

    public void setPocPost(String pocPost) {
        this.pocPost = pocPost;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}

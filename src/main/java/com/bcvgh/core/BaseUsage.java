package com.bcvgh.core;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseUsage {
    public String url;
    public String tag;
    public String name;
    public String PocS;
//    public ArrayList<String> prompt = new ArrayList<>();
    public BaseUsage(String url , String tag , String name){
        this.url = url;
        this.tag = tag;
        this.name = name;

    }

    public BaseUsage(String url , String tag){
        this.url = url;
        this.tag = tag;
    }
}

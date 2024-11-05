package com.bcvgh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PocUtil {
    public static HashMap<String, ArrayList<String>> tag_vul = new HashMap<>();
    public static HashMap<String,String> TagCn = new HashMap<>();


    public static HashMap<String, ArrayList<String>> getTagVul() throws Exception{
        String[] tags = FileUtil.DirList(Constant.PocPath);
        HashMap<String, ArrayList<String>> tag_vul = new HashMap<String, ArrayList<String>>();
        if (tags!=null){
            for (String tag : tags){
                if (tag.equals("config.json") || tag.equals(".git") || tag.equals("README.md") || tag.equals(".DS_Store")) continue;
                String[] NamesArray = FileUtil.DirList(Constant.PocPath+ File.separator+tag);
                ArrayList<String> Names = (ArrayList<String>) Arrays.asList(NamesArray).stream()
                        .map(s -> s.replaceAll("\\.json$", ""))
                        .collect(Collectors.toList());;
                tag_vul.put(tag,Names);
            }
        }
        return tag_vul;
    }

    public static HashMap<String,String> getTagCn() throws Exception{
        HashMap<String,String> TagCn = new HashMap<>();
        String ConfigS = FileUtil.FileRead(Constant.ConfigPath,new String());
        JSONObject tagNameJson = JSONObject.parseObject(ConfigS);
        tagNameJson = tagNameJson.getJSONObject("tagname");
        if (PocUtil.tag_vul!=null){
            for (String tag : PocUtil.tag_vul.keySet()){
                if (tagNameJson.getString(tag)!=null){
                    TagCn.put(tag,tagNameJson.getString(tag));
                }else {
                    TagCn.put(tag,tag);
                }
            }
        }
        return TagCn;
    }

    public static JSONObject getPocJSon(String tag,String name){
        String PocS = FileUtil.FileRead(Constant.PocPath+ File.separator+tag+File.separator+name+".json","");
        JSONObject PocJson = JSON.parseObject(PocS, Feature.OrderedField);
        return PocJson;
    }

    public static JSONObject PojoHeader_toJson(String header){
        JSONObject poc_headerJson = new JSONObject();
        String[] headerKeyValue = header.split("\n");
        for(String KeyValue : headerKeyValue){
            String Key = KeyValue.split(":")[0];
            String Value = KeyValue.split(":")[1];
            poc_headerJson.put(Key,Value);
        }
        return poc_headerJson;
    }





}

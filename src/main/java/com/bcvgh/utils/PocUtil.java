package com.bcvgh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.pojo.PocAll;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PocUtil {
    public static ArrayList<String> types = new ArrayList<>();
    public static String defaultHeader =
            "Content-Length: 29\n" +
            "Accept-Encoding: */*\n" +
            "User-Agent: Mozilla/5.0\n" +
            "Connection: close\n";
    public static String PocPath =System.getProperty("user.dir")+ File.separator + "poc" + File.separator;
//    public static String PocPath ="D:\\comsoft\\st\\my\\daydayEXP\\daydayExp\\poc\\";
    public static   HashMap<String , ArrayList<HashMap<String,String>>> pocParse = new HashMap<>();
    public static String name ="";
    public static String type ="";
    public static HashMap<String, ArrayList<String>> tag_name = new HashMap<String, ArrayList<String>>();
    public static String tag ="";
    public static ArrayList<String> tags;
    public static String url ="";
    public static String dnsUrl="";
    public static String DnsCommand ="";

    public static HashMap<String , ArrayList<HashMap<String,String>>> PocParse(String pocPath){
        String[] dirNames = FileUtil.FileList(pocPath);
        for (String dirName : dirNames) {
            ArrayList<HashMap<String,String>> vulList = new ArrayList<>();
            String[] fileNames = FileUtil.FileList(pocPath + dirName);
            for (String fileName : fileNames) {
                String poc = FileUtil.FileRead(pocPath + dirName + File.separator + fileName);
                JSONObject pocJson = JSON.parseObject(poc);
                HashMap<String,String>  a = new HashMap<>();
                a.put("name", pocJson.getString("name"));
                a.put("type", pocJson.getString("type"));
                a.put("tag", pocJson.getString("tag"));
                vulList.add(a);
            }
            pocParse.put(dirName,vulList);
        }
        return pocParse;
    }

    public static String[] GetTagVulFiles(String tag){
        String TagPath = PocUtil.PocPath+"json"+File.separator+tag+File.separator;
        String[] VulFiles = FileUtil.FileList(TagPath);
        return VulFiles;
    }

    public static HashMap<String, ArrayList<String>> GetTagName(){
//        for(ArrayList<HashMap<String, String>> a : pocParse)
        Iterator iterator = pocParse.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry =(Map.Entry) iterator.next();
            String tag = (String) entry.getKey();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<HashMap<String, String>> values = (ArrayList<HashMap<String, String>>) entry.getValue();
            for (HashMap<String,String> value: values){
                String name = value.get("name");
                names.add(name);
            }
            tag_name.put(tag,names);
        }
        return tag_name;
    }

    public static String GetTagCN(String tag){
        String Tags = FileUtil.FileRead(PocUtil.PocPath+"config.json");
//        String Tags = FileUtil.FileRead("./poc/config.json");
        JSONObject tagsJson = JSON.parseObject(Tags).getJSONObject("tagname");
        for(String i:tagsJson.keySet()){
            if (i.equals(tag)){
                return tagsJson.getString(i);
            }
        }
        return tag;
    }

    public static String GetTagEn(String tagCN){
        String Tags = FileUtil.FileRead(PocUtil.PocPath+"config.json");
//        String Tags = FileUtil.FileRead("./poc/config.json");
        JSONObject tagsJson = JSON.parseObject(Tags).getJSONObject("tagname");
        for(String i:tagsJson.keySet()){
            if (tagsJson.getString(i).equals(tagCN)){
                return i;
            }
        }
        return null;
    }

    public static ArrayList<String> GetTagCNS(){
        String Tags = FileUtil.FileRead(PocUtil.PocPath+"config.json");
        JSONObject tagsJson = JSON.parseObject(Tags).getJSONObject("tagname");
        ArrayList<String> tagsCNS = new ArrayList<>();
        for (String tag : tagsJson.keySet()){
            tagsCNS.add(tagsJson.getString(tag));
        }
        return tagsCNS;
    }

    public static ArrayList<String> getTags(){
        String Tags = FileUtil.FileRead(PocUtil.PocPath+"config.json");
        JSONObject tagsJson = JSON.parseObject(Tags).getJSONObject("tagname");
        ArrayList<String> tags = new ArrayList<>(tagsJson.keySet());
        return tags;

    }

    public static JSONObject GetPocs(String tag,String name){
        String pocs = FileUtil.FileRead(PocUtil.PocPath+"json"+File.separator+tag+File.separator+name+".json");
        JSONObject Pocs = JSON.parseObject(pocs);
        return Pocs;
    }

//    public static JSONObject replacePlaceholder(JSONObject poc, String placeholder, String input){
//        try {
//            String pocString = JSON.toJSONString(poc);
//            pocString = pocString.replace(placeholder,input);
//            poc = JSON.parseObject(pocString);
//        }catch (Exception e){
//
//        }
//        return poc;
//    }

    public static String Encode(String methodName,String text) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cls = Class.forName("com.bcvgh.utils.Encode");
        Method m = cls.getMethod(methodName, String.class);
        String out = (String) m.invoke(null,text);
        return out;
    }

    public static String multipleEncode(String methodName, String text, Integer num) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String out = text;
        for (Integer i =1 ; i<=num; i++){
            out = Encode(methodName, out);
        }
        return out;
    }

    public static JSONObject Object_toJSon(PocAll pocAll){
        JSONObject PocAll_JSON = new JSONObject(true);
        PocAll_JSON.put("num",2);
        PocAll_JSON.put("name",pocAll.getVulName());
        PocAll_JSON.put("tag",pocAll.getTag());
        PocAll_JSON.put("type",pocAll.getType());
        JSONObject poc_JSON = new JSONObject(true);
        poc_JSON.put("pocGet",pocAll.getPoc().getPoc_get());
        if (!pocAll.getPoc().getPco_post().isEmpty()){
            poc_JSON.put("pocPost",pocAll.getPoc().getPco_post());
        }
        if (!pocAll.getPoc().getPoc_header().isEmpty()){
            poc_JSON.put("header", PojoHeader_toJson(pocAll.getPoc().getPoc_header()));
        }else {
            poc_JSON.put("header",PojoHeader_toJson(PocUtil.defaultHeader));
        }
        PocAll_JSON.put("poc",poc_JSON);
        poc_JSON.put("Pattern",pocAll.getPoc().getPoc_Pattern());
        if (!pocAll.getExp().isEmpty()){
            JSONObject AllExp = new JSONObject(true);
            for (String step : pocAll.getExp().keySet()){
                JSONObject stepExp = new JSONObject(true);
                stepExp.put("expGet",pocAll.getExp().get(step).getExp_get());
                if (!pocAll.getExp().get(step).getExp_post().isEmpty()){
                    stepExp.put("expPost",pocAll.getExp().get(step).getExp_post());
                }
                if (!pocAll.getExp().get(step).getExp_header().isEmpty()){
                    stepExp.put("header", PojoHeader_toJson(pocAll.getExp().get(step).getExp_header()));
                }else {
                    stepExp.put("header", PojoHeader_toJson(PocUtil.defaultHeader));
                }
                stepExp.put("Pattern",pocAll.getExp().get(step).getExp_Pattern());
                AllExp.put(step,stepExp);
            }
            PocAll_JSON.put("exp",AllExp);
        }
        return PocAll_JSON;

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



//    private static void traverseAndExtract(JSONObject jsonObject, String parentKey) {
//        for (String key : jsonObject.keySet()) {
//            Object value = jsonObject.get(key);
//
//            if (value instanceof String) {
//                // 如果字段值是字符串，则提取并打印
//                String fullKey = (parentKey.isEmpty()) ? key : parentKey + "." + key;
//                System.out.println(fullKey + ": " + value);
//            } else if (value instanceof JSONObject) {
//                // 如果字段值是 JSON 对象，则递归处理
//                String newParentKey = (parentKey.isEmpty()) ? key : parentKey + "." + key;
//                traverseAndExtract((JSONObject) value, newParentKey);
//            }
//            // 可以添加其他数据类型的处理逻辑（例如数组等）
//        }
//    }
}

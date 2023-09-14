package com.bcvgh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PocUtil {
//    public static String PocPath ="./poc/json/";
    public static String PocPath ="D:\\comsoft\\st\\my\\daydayEXP\\daydayExp\\poc\\json\\";
//    public static String PocPath ="src/main/java/com/bcvgh/poc/json/";
    public static   HashMap<String , ArrayList<HashMap<String,String>>> pocParse = new HashMap<>();
    public static String name ="";
    public static String type ="";
    public static HashMap<String, ArrayList<String>> tag_name = new HashMap<String, ArrayList<String>>();
    public static String tag ="";
    public static ArrayList<String> tags;
    public static String url ="";

    public static HashMap<String , ArrayList<HashMap<String,String>>> PocParse(String pocPath){
        String[] dirNames = FileUtil.FileList(pocPath);
        for (String dirName : dirNames) {
            ArrayList<HashMap<String,String>> vulList = new ArrayList<>();
            String[] fileNames = FileUtil.FileList(pocPath + dirName);
            for (String fileName : fileNames) {
                String poc = FileUtil.FileRead(pocPath + dirName + "/" + fileName);
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
        String Tags = FileUtil.FileRead("D:\\comsoft\\st\\my\\daydayEXP\\daydayExp\\poc\\poctag.json");
//        String Tags = FileUtil.FileRead("./poc/poctag.json");
        JSONObject tagsJson = JSON.parseObject(Tags);
        String tagCn = new String();
        for(String i:tagsJson.keySet()){
            if (i.equals(tag)){
                return tagsJson.getString(i);
            }
        }
        return tag;
    }

    public static JSONObject GetPocs(String tag,String name){
        String pocs = FileUtil.FileRead(PocUtil.PocPath+tag+"/"+name+".json");
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

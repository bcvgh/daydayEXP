package com.bcvgh.core.placeholder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bcvgh.core.Encode;
import com.bcvgh.core.pojo.Input;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolder {
    private static final Logger LOGGER = LogManager.getLogger(PlaceHolder.class.getName());
    Map<String,String> stringMap = new HashMap<>();
    Map<String,byte[]> byteMap = new HashMap<>();
    public Object result;

    public PlaceHolder(Input input ,String content) {
        addValues(StringPlaceholder.values(), input, stringMap);
        addValues(bytePlaceHolder.values(), input, byteMap);
        Object tempString = this.StringPlaceHolder(content);
        this.result = tempString==null?this.StringToBytePlaceHolder(content):tempString;
//        this.result = tempBytes==null?content:tempBytes;
    }

    public static JSONObject dealPlaceHolder(JSONObject payload,Input input){
        String pocString = JSON.toJSONString(payload, SerializerFeature.SortField);
        PlaceHolder placeHolder = new PlaceHolder(input,pocString);
        if (placeHolder.result!=null){
            return JSON.parseObject((String) placeHolder.result);
        }
        return JSON.parseObject(JSON.toJSONString(payload, SerializerFeature.SortField));
    }

    public String StringPlaceHolder(String content){
        StringSubstitutor sub = new StringSubstitutor(new CustomReplace(this.stringMap),"{{","}}",'\\');
        String replacedString = sub.replace(content);
        if (content.equals(replacedString) && Pattern.matches(".*\\{\\{.*\\}\\}.*",content) ){
            try {
                if (this.byteMap.containsKey(content.substring(2).split(":")[0])){
                    return null;
                }
                return  (String) PlaceHolder.EncodeMath(null,content.substring(2,content.length()-2));
            }catch (Exception e){
                LOGGER.info(e.getMessage());
                return null;
            }
        }
        return replacedString;
    }

    public byte[] StringToBytePlaceHolder(String content){
        content = content.substring(2,content.length()-2);
        for (String byteName : this.byteMap.keySet()){
            if (content.contains(byteName)){
                return (byte[]) PlaceHolder.EncodeMath(this.byteMap,content);
            }
        }
        try {
            return (byte[]) PlaceHolder.EncodeMath(null,content);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            return null;
        }
    }


    private <T> void addValues(Enum[] enums, Input input, Map<String, T> map) {
        for (Enum e : enums) {
            try {
                Field field = Input.class.getDeclaredField(e.name());
                field.setAccessible(true);
                Object value = field.get(input);
                if (value != null) {
                    map.put(e.name(), (T) value); // Assuming string representation here
                }
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                LOGGER.warn(ex.getMessage());
            }
        }
    }

    public static Object EncodeMath(Object placeHolder,String content){
        Pattern pattern = Pattern.compile("([^:}]+)(?::([^:}]+))?(?::(\\d+))?");
        //
//        Pattern pattern2 = Pattern.compile(".*(([^:}]+)(?::([^:}]+))?(?::(\\d+))?).*");
//        Matcher matcher2 = pattern2.matcher(content);
        //
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            String actualKey = matcher.group(1);
            String encodeType = matcher.group(2);
            String numStr = matcher.group(3);
        if (placeHolder instanceof Map){
            if (((Map)placeHolder).containsKey(actualKey)) {
                if (encodeType != null) {
                    if (numStr != null) {
                        int num = Integer.parseInt(numStr);
                        return new Encode<>(encodeType, ((Map)placeHolder).get(actualKey), num).encoded;
                    } else {
                        return new Encode<>(encodeType, ((Map)placeHolder).get(actualKey)).encoded;
                    }
                } else {
                    return ((Map)placeHolder).get(actualKey);
                }
            }
        }else {
            if (encodeType != null) {
                if (numStr != null) {
                    int num = Integer.parseInt(numStr);
                    return new Encode<>(encodeType, actualKey, num).encoded;
                } else {
                    return new Encode<>(encodeType, actualKey).encoded;
                }
            } else {
                return placeHolder;
            }
        }
        }
        return null;
    }

}

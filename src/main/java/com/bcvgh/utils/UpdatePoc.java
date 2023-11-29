package com.bcvgh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdatePoc {

    public static HashMap<String,Object> headers = new HashMap<>(PocUtil.PojoHeader_toJson(PocUtil.defaultHeader));


    public static List<String> getDirs(String pocURL ,String type){
        ArrayList<String> dirs = new ArrayList<>();
        String dirsUrl = "";
        String regex ="";
        Response response = null;
        try {
            if (type.equals("github")){
                dirsUrl = pocURL+"/tree/main/";
                response = HttpTools.get(dirsUrl,UpdatePoc.headers,"UTF-8");
                regex = "/tree/main/[0-9a-zA-Z]*\">([0-9a-zA-Z]*)<";
            }else {
                dirsUrl = pocURL+"/json/";
                response = HttpTools.get(dirsUrl,UpdatePoc.headers,"UTF-8");
                regex = "<a href=\"(.*)/\">";
            }
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            while (matcher.find()){
                dirs.add(matcher.group(1));
            }
            if (!dirs.isEmpty()){
                return dirs;
            }else {
                if (type.equals("github")){
                    Platform.runLater(()->PromptUtil.Alert("警告","连接出错啦，请确认github仓库地址是否正确！"));
                }
                else {
                    Platform.runLater(()->PromptUtil.Alert("警告","连接出错啦，请确认仓库地址是否正确！"));
                }
                return null;
            }
        }catch (Exception e){
            Platform.runLater(()->PromptUtil.Alert("警告","连接出错啦，请确认仓库地址是否正确！"));
        }
        return null;
    }

    public static List<String> getPocs(String pocUrl, String dirname){
        List<String> pocs = new ArrayList<>();
        String PocsUrl = pocUrl +"/json"+"/"+dirname+"/";
        try {
            Response response = HttpTools.get(PocsUrl,UpdatePoc.headers,"UTF-8");
            String regex = "<a href=\"(.*)\">";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            while (matcher.find()){
                pocs.add(matcher.group(1));
            }
        }catch (Exception e){
            Platform.runLater(()->PromptUtil.Alert("警告","出错啦，获取poc失败！"));
            return null;
        }
        return pocs;
    }

    public static JSONArray getPocsgit(String pocURL, String dirname){
        String PocsUrl = pocURL+"/tree/main/"+dirname;
        try {
            Response response = HttpTools.get(PocsUrl,UpdatePoc.headers,"UTF-8");
            String regex = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            if (matcher.find()){
                JSONObject json = JSON.parseObject(matcher.group(1));
                JSONArray jsonArray = json.getJSONObject("payload").getJSONObject("tree").getJSONArray("items");
                return jsonArray;
            }
            else {
                Platform.runLater(()->PromptUtil.Alert("警告","连接出错啦，请确认github仓库地址是否正确！"));
                return null;
            }
        }catch (Exception e){
            Platform.runLater(()->PromptUtil.Alert("警告","出错啦，仓库poc获取失败！"));
            return null;
        }

    }

    public static String getPocContent(String pocURL,String dir,String dirname){
        String pocUrl = pocURL + "/json" +"/"+dir+"/"+dirname;
        Response response1 = HttpTools.get(pocUrl,UpdatePoc.headers, "UTF-8");
        String content = response1.getText();
        return content;
    }

    public static String getPocContentgit(String pocURL,String dir,String dirname) {
        String pocUrl = "";
        try {
            pocUrl = pocURL + "blob/main/" + dir + "/" + URLEncoder.encode(dirname, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            Platform.runLater(()->PromptUtil.Alert("警告","出错啦！"));
        }
        Response response1 = HttpTools.get(pocUrl,UpdatePoc.headers, "UTF-8");
        String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
        try {
            Pattern pattern1 = Pattern.compile(regex1);
            Matcher matcher1 = pattern1.matcher(response1.getText());
            if (matcher1.find()) {
                String pocs = "";
                JSONObject jsonObject = JSONObject.parseObject(matcher1.group(1));
                JSONArray jsonArray1 = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
                for (int s = 0; s < jsonArray1.size(); s++) {
                    String poc = jsonArray1.getString(s);
                    pocs = pocs + poc;
                }
                return pocs;
            }
        }catch (Exception e){
            Platform.runLater(()->PromptUtil.Alert("警告","出错啦！仓库内容获取失败！"));
        }
        return null;
    }

    public static Boolean getPocConfig(String pocURL){
        String path = "";
        String content ="";
        if (pocURL.contains("github")){
            path = "/blob/main/config.json";
        }else {
            path = "/config.json";
        }
        Response response = HttpTools.get(pocURL+path,UpdatePoc.headers,"UTF-8");
        try {
            if (response.getCode()==200){
                if (pocURL.contains("github")){
                    String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
                    Pattern pattern1 = Pattern.compile(regex1);
                    Matcher matcher1 = pattern1.matcher(response.getText());
                    if (matcher1.find()) {
                        String pocs = "";
                        JSONObject jsonObject = JSONObject.parseObject(matcher1.group(1));
                        JSONArray jsonArray1 = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
                        for (int s = 0; s < jsonArray1.size(); s++) {
                            String poc = jsonArray1.getString(s);
                            content = content + poc;
                        }
                    }
                }
                else {
                    content = response.getText();
                }
            }
        }catch (Exception e){
            Platform.runLater(()->PromptUtil.Alert("警告","出错啦！配置文件获取失败！"));
        }
        if (!content.isEmpty()){
            FileUtil.FileWrite("config.json",content,null);
            return true;
        }
        else {
            return false;
        }
    }




}

package com.bcvgh.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RemoteUpdatePoc {

    public String url;
    public String type;

    public RemoteUpdatePoc(String url) {
        this.url = url;
        this.type = verifyUrl(this.url);
    }

    private String verifyUrl(String url) {
        String type = "";
        if (url.contains("github")) {
            type = "github";
        } else {
            type = "customize";
        }
        Response response = HttpTools.get(url, (HashMap<String, ?>) Constant.Header, "UTF-8");
        if (response.getCode() != 200) {
            return null;
        }
        return type;
    }

    public Boolean getPocConfig() {
        String path = "";
        String content = "";
        if (this.type.equals("github")) {
            path = "/blob/main/config.json";
            Response response = HttpTools.get(this.url + path, (HashMap<String, ?>) Constant.Header, "UTF-8");
            String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
            Pattern pattern = Pattern.compile(regex1);
            Matcher matcher = pattern.matcher(response.getText());
            if (matcher.find()) {
                JSONArray jsonArray1 = JSONObject.parseObject(matcher.group(1)).getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
                for (int s = 0; s < jsonArray1.size(); s++) {
                    String poc = jsonArray1.getString(s);
                    content = content + poc;
                }
            }
        } else if (this.type.equals("customize")) {
            path = "/config.json";
            Response response = HttpTools.get(this.url + path, (HashMap<String, ?>) Constant.Header, "UTF-8");
            content = response.getText();
        }
        if (!content.isEmpty()) {
            FileUtil.FileWrite(Constant.ConfigPath, content);
            return true;
        } else {
            return false;
        }
    }

    public Boolean MkNewDir(){
        List<String> localDirs = Arrays.asList(FileUtil.DirList(Constant.PocPath)).stream().filter(s-> !s.equals("config.json")).collect(Collectors.toList());
        List<String> remoteDirs = new ArrayList<>();
        if (this.type.equals("github")) {
            remoteDirs = this.getDirs(this.url, "github");
        } else {
            remoteDirs = this.getDirs(this.url, "customize");
        }
        try {
            remoteDirs.removeAll(localDirs);
            for (String Dir : remoteDirs) {
                FileUtil.Mkdir(Constant.PocPath, Dir);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> getDirs(String pocURL, String type) {
        ArrayList<String> dirs = new ArrayList<>();
        String regex = "";
        if (type.equals("github")) {
            pocURL = pocURL + "/tree/main/";
            regex = "<script type=\"application/json\" data-target=\"react-partial.embeddedData\">(\\{\"props\":\\{.+\\}\\})</script>";
        } else if (type.equals("customize")) {
            pocURL = pocURL + "/";
            regex = "<a href=\"(.*)/\">";
        }
        Response response = HttpTools.get(pocURL, (HashMap<String, ?>) Constant.Header, "UTF-8");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response.getText());

        if (type.equals("github")){
            if (matcher.find()) {
                JSONArray jsonArray1 = JSONObject.parseObject(matcher.group(1)).getJSONObject("props").getJSONObject("initialPayload").getJSONObject("tree").getJSONArray("items");
                for (int s = 0; s < jsonArray1.size(); s++) {
                    String name = jsonArray1.getJSONObject(s).getString("name");
                    dirs.add(name);
                }
            }
        }
        if (type.equals("customize")){
            while (matcher.find()){
                dirs.add(matcher.group(1));
            }
        }

        return dirs.stream().filter(s-> !s.equals("config.json") && !s.contains("README.md")).collect(Collectors.toList());
    }


    public String getContent(String dirname, String name) {
        String content = "";
        String pocUrl = "";
        if (this.type.equals("customize")) {
            pocUrl = this.url + "/" + dirname + "/" + name;
            Response response = HttpTools.get(pocUrl, (HashMap<String, ?>) Constant.Header, "UTF-8");
            content = response.getText();
        } else if (this.type.equals("github")) {
            try {
                pocUrl = url + "/blob/main/"+ dirname + "/" + URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
            }
            Response response = HttpTools.get(pocUrl, (HashMap<String, ?>) Constant.Header, "UTF-8");
            String regex = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            if (matcher.find()) {
                JSONObject jsonObject = JSONObject.parseObject(matcher.group(1));
                JSONArray jsonArray = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
                for (int s = 0; s < jsonArray.size(); s++) {
                    String poc = jsonArray.getString(s);
                    content = content + poc;
                }
            }
        }
        return content;
    }


    public List<String> getPocs(String dirname){
        String pocUrl = "";
        List<String> pocs = new ArrayList<>();
        if (this.type.equals("customize")) {
            pocUrl = this.url + "/" + dirname + "/";
            Response response = HttpTools.get(pocUrl, (HashMap<String, ?>) Constant.Header, "UTF-8");
            String regex = "<a href=\"(.*)\">";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            while (matcher.find()) {
                pocs.add(matcher.group(1));
            }
            return pocs;
        }else if (this.type.equals("github")) {
            pocUrl = this.url + "/tree/main/" + dirname;
            Response response = HttpTools.get(pocUrl, (HashMap<String, ?>) Constant.Header, "UTF-8");
            String regex = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.getText());
            if (matcher.find()) {
                JSONObject json = JSON.parseObject(matcher.group(1));
                JSONArray jsonArray = json.getJSONObject("payload").getJSONObject("tree").getJSONArray("items");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = String.valueOf(jsonObject.get("name"));
                    pocs.add(name);
                }
            }
            return pocs;
        }
        return null;
    }

}
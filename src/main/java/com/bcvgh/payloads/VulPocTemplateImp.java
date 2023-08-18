package com.bcvgh.payloads;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.util.HttpTools;
import com.bcvgh.util.Response;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VulPocTemplateImp implements VulTemplate{
    private String tag;
    private String name;
    private boolean isVul = false;
    private String Url;
    private String PocGet;
    private String PocPost;
    private String PocPattern;
//    private String PocUrl;
    private HashMap<String, ?> header;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VulPocTemplateImp(String url, JSONObject poc) {
        this.name = poc.getString("name");
        this.Url = url;
        this.PocGet = poc.getJSONObject("detail").getString("pocGet");
        this.PocPattern = poc.getJSONObject("detail").getString("pattern");
        /** 添加请求头 **/
        JSONObject a = poc.getJSONObject("detail").getJSONObject("header");
        this.header = new HashMap<>(a);
//        this.header.put("cookie",poc.getJSONObject("detail").getJSONObject("header").getString("cookie"));
//        this.header.put("User-Agent",poc.getJSONObject("detail").getJSONObject("header").getString("User-Agent"));
//        this.header.put("content-type",poc.getJSONObject("detail").getJSONObject("header").getString("contentType"));
//        this.header.put("Content-Length",poc.getJSONObject("detail").getJSONObject("header").getString("Content-Length"));
//        this.header = poc.getJSONObject("detail").getJSONObject("header");
        if (poc.getJSONObject("detail").getString("pocPost") != null){
        this.PocPost = poc.getJSONObject("detail").getString("pocPost");}
    }

    @Override
    public Boolean PatternMatch(String resText){
        String pattern = this.PocPattern;
//        Pattern PocPattern = Pattern.compile(pattern);
//        Matcher matcher = PocPattern.matcher(resText);
        Boolean matcher = resText.contains(pattern);
        return matcher;
    }


    @Override
    public String checkVul() {
        if (this.PocPost != null){
            Response res = HttpTools.post(this.Url.concat(this.PocGet),this.PocPost,this.header,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                return "";
            }
            if (this.PatternMatch(res.getText())){
                this.isVul =true;
                return this.name;
            }
        }
        else if (this.PocGet != null){
            Response res = HttpTools.get(this.Url.concat(this.PocGet), this.header ,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                return "";
            }
            if (this.PatternMatch(res.getText())){
                this.isVul = true;
                return this.name;
            }
        }
       return "";

    }

    public void exploitVul(String target){

    }


}

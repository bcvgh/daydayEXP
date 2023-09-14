package com.bcvgh.core.exp;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.HttpTools;
import com.bcvgh.utils.Response;
import java.net.URLEncoder;
import java.util.HashMap;

public class ExecTemplateImp extends ExpTemplateImp {
    private String command;

    public ExecTemplateImp(String url, JSONObject poc, HashMap<String, String> Extra){
        super(url, poc);
        this.command = URLEncoder.encode(Extra.get("command"));
//        this.command = Extra.get("command");
        this.Exp = this.replacePlaceholder(this.Exp,"{command}",this.command);
        this.Exp = this.PlaceholderHandling(this.Exp,this.command);
    }


    @Override
    public HashMap<String, String> exploitVul() {
        Integer i = 1;
        String value;
        this.result.put("name",this.name);
        for (Integer n = 1; n <= this.Exp.keySet().size(); n++) {
            value = "step" + Integer.toString(n);
            JSONObject stepContent = this.Exp.getJSONObject(value);
            this.ExpPost = stepContent.getString("expPost");
            this.ExpGet = stepContent.getString("expGet");
            try {
                if (this.ExpGet.contains("{command}")){
                    this.command = URLEncoder.encode(this.command);
                }
            }catch (Exception e){}
            this.pattern = stepContent.getString("Pattern");
            this.header = new HashMap<>(stepContent.getJSONObject("header"));
//            for (String key : this.header.keySet()) {
//                if (((String) this.header.get(key)).contains("{url}")){
//                    this.header.replace(key,this.Url);
//                }
//                if (((String) this.header.get(key)).contains("{command}")){
//                    this.header.replace(key,this.command);
//                }
//            }
//            if (ExpGet.contains("{command}")) ExpGet = ExpGet.replace("{command}",this.command);
//            if (ExpPost.contains("{command}")) ExpPost = ExpPost.replace("{command}",this.command);
            if (ExpPost !=null){
                Response res = HttpTools.post(this.Url.concat(ExpGet),ExpPost,this.header,"UTF-8");
                if (res.getText()==null || res.getCode()!=200){
                    this.isExploited = false;
                    this.result.put("prompt","error");
                    return this.result;
                }
                String resText = this.resMatch(res.getText(),this.pattern);
                if (resText.equals("error")){
                    this.isExploited = false;
                }
                if (!resText.equals("error")){
                    this.isExploited = true;
                    this.result.put("result",resText);
                }
            }
            else if (ExpGet !=null){
                Response res = HttpTools.get(this.Url.concat(ExpGet),this.header,"UTF-8");
                if (res.getText()==null){
                    this.isExploited = false;
                    this.result.put("prompt","error");
                    return this.result;
                }
                String resText = this.resMatch(res.getText(),this.pattern);
                if (resText.equals("error")){
                    this.isExploited = false;
                }
                if (!resText.equals("error")){
                    this.isExploited = true;
                    this.result.put("result",resText);
                }
            }
            i = i+1;
        }
        if (this.isExploited && this.result.get("result").equals("")){
            this.result.put("prompt","无返回结果，请确认该Poc是否无回显，若为无回显Poc请输入反弹shell命令");
        }
        else if (this.isExploited){

        }
        else {
            this.result.put("prompt","利用失败，请检查poc可用性");
        }
        return this.result;
    }
}
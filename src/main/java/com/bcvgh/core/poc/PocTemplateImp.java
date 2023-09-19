package com.bcvgh.core.poc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.BaseTemplate;
import com.bcvgh.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PocTemplateImp extends BaseTemplate implements PocTemplate{


    private ArrayList<HashMap<String,String>> Result = new ArrayList<>();
    private String PocPost;
    private String PocGet;
    private String Pattern;
    private boolean HasDns;

    public PocTemplateImp(String url, JSONObject poc) {
        super(url, poc);
        String randomString = Utils.getRandomString(4);
        this.Poc = poc.getJSONObject("poc");
        this.HasDns = JSON.toJSONString(this.Poc).contains("{dnslog}") ? true : false;
        this.Poc = this.replacePlaceholder(this.Poc,"{url}",this.Url);
        this.Poc = this.replacePlaceholder(this.Poc,"{dnslog}",PocUtil.DnsCommand);
        this.Poc = this.replacePlaceholder(this.Poc,"{random}",randomString);
        this.header = new HashMap<>(this.Poc.getJSONObject("header"));
        this.PocPost = this.Poc.getString("pocPost");
        this.PocGet = this.Poc.getString("pocGet");
        this.Pattern = this.Poc.getString("Pattern");


    }




    @Override
    public ArrayList<HashMap<String, String>> checkVul() {
        HashMap<String,String> result = new HashMap<>();
        result.put("name",this.name);
        if (PocPost != null){
            Response res = HttpTools.post(this.Url.concat(PocGet),PocPost,this.header,"UTF-8");
            if (res.getText()==null || res.getText().contains("burp")){ /**之后会换成异常捕获**/
                result.put("res","false");
//                Result.add(result);
//                return this.Result;
            }
            else if (!this.resMatch(res.getText(),Pattern).equals("error") ){
                result.put("res",res.getText());
//                Result.add(result);
//                return this.Result;
            }
            else {
                result.put("res","false");
            }
        }
        else if (PocGet != null){
            Response res = HttpTools.get(this.Url.concat(PocGet), this.header ,"UTF-8");
            if (res.getText()==null || res.getText().contains("burp")){ /**之后会换成异常捕获**/
                result.put("res","false");
//                Result.add(result);
//                return this.Result;
            }
            else if (!this.resMatch(res.getText(),Pattern).equals("error")){
//                this.isVul =true;
                result.put("res",res.getText());
//                Result.add(result);
//                return this.Result;
            }
            else {
                result.put("res","false");
            }
        }

        if (!PocUtil.dnsUrl.isEmpty() && this.HasDns){
//            if (ApiUtil.ceyeDnslog(PocUtil.dnsUrl,"be94361a3b2d8f6ec7dfe0a3275ab79e")){
//                result.put("res","dnslog");
//            }
            if (ApiUtil.getDnslog(PocUtil.dnsUrl)){
                result.put("res","dnslog");
            }
            else {
                result.put("res","false");
            }
        }
        this.Result.add(result);
       return this.Result;
    }


//    public String exploitVul(){
//        Integer i =1;
//        String ExpPost = "";
//        String ExpGet = "";
//        String Pattern = "";
//        for (String value:this.Exp.keySet()){
////            if (this.type.equals("cmd") && !TaskController.Cmd.isEmpty()){
////                Content.offer(TaskController.Cmd);
////            }
//            value = "step"+Integer.toString(i);
//            ExpPost = this.Exp.getJSONObject(value).getString("expPost");
//            ExpGet = this.Exp.getJSONObject(value).getString("expGet");
//            Pattern = this.Exp.getJSONObject(value).getString("Pattern");
//            this.header = new HashMap<>(this.Exp.getJSONObject(value).getJSONObject("header"));
//            String resText;
//            try {
//                if (ExpGet.contains("{0}")){
//                    if (this.type.equals("upload")){
//                        ExpGet = ExpGet.replace("{0}",Content.poll());
//                    }
////                ExpGet = String.format(ExpGet,Content.poll());
//                    if (this.type.equals("cmd")){
//                        String Command = TaskController.Cmd.isEmpty() ? "whoami" : TaskController.Cmd;
//                        ExpGet = ExpGet.replace("{0}",Command);
//                    }
//                }
//                if (ExpPost.contains("{0}")){
//                    if (this.type.equals("cmd")){
//                        String Command = TaskController.Cmd.isEmpty() ? "whoami" : TaskController.Cmd;
//                        ExpPost = ExpPost.replace("{0}",Command);
//                    }
//                    if (this.type.equals("upload")){
//                        ExpPost = ExpPost.replace("{0}",Content.poll());
//                    }
////                ExpPost = String.format(ExpPost,Command);
//                }
//            }catch (Exception e){
//
//            }
//
//            if (ExpPost != null){
//                Response res = HttpTools.post(this.Url.concat(ExpGet),ExpPost,this.header,"UTF-8");
//                if (res.getText()==null){
//                    return "";
//                }
//                resText = this.ExpMatch(res.getText(),Pattern);
//                if (resText.equals(null)){ /**之后会换成异常捕获**/
//                    this.isExploited = false;
//                }
//                if (!resText.equals(null)){
//                    this.isExploited = true;
//                    if (this.type.equals("upload") || this.type.equals("cmd")){
//                        Content.offer(resText);
//                    }
//                }
//            }
//            else if (ExpGet != null){
//                Response res = HttpTools.get(this.Url.concat(ExpGet), this.header ,"UTF-8");
//                if (res.getText()==null) {
//                    return "";
//                }
//                if (this.ExpMatch(res.getText(),Pattern).equals(null)){ /**之后会换成异常捕获**/
//                    this.isExploited = false;
//                }
//                if (!this.ExpMatch(res.getText(),this.Poc.getString("Pattern")).equals(null)){
//                    this.isExploited = true;
//                }
//            }
//            i = i + 1;
//        }
//        if (this.isExploited) {
//            if (this.type.equals("upload")) {
//                System.out.println(this.Url);
//                return "webshell已上传，请自行检测可用性！\n" + this.Url.concat(ExpGet);
//            }
//            if (this.type.equals("cmd")) {
//                if (!Content.isEmpty()){
//                    return Content.poll();
//                }
//            }
//            return this.name;
//        }
//        return "";
//    }

}

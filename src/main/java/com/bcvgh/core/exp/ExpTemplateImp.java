package com.bcvgh.core.exp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.BaseTemplate;
import com.bcvgh.utils.PocUtil;
import com.bcvgh.utils.Response;
import com.bcvgh.utils.Utils;

import java.util.*;

public class ExpTemplateImp extends BaseTemplate implements ExpTemplate {
    public Boolean isExploited;
    public JSONObject Exp;
    public HashMap<String,Object> header;
    public Queue<String> Content = new ArrayDeque<>();
    public HashMap<String,String> result = new HashMap<String, String>();
    /**new**/
    public String ExpPost = new String();
    public String ExpGet = new String();
    public String pattern = new String();
    /**new**/
    public ExpTemplateImp(String url, JSONObject poc) {
        super(url, poc);
        String randomString = Utils.getRandomString(4);
        this.Exp = poc.getJSONObject("exp");
        this.Exp = this.replacePlaceholder(this.Exp,"{url}",this.Url);
        this.Exp = this.replacePlaceholder(this.Exp,"{random}",randomString);
    }

//    public String ExpMatch(String resText,String pattern){
//        Pattern ExpPattern =Pattern.compile(pattern ,Pattern.DOTALL);
//        Matcher matcher = ExpPattern.matcher(resText);
//        String MatchContent ="";
//        if (matcher.find()){
//            MatchContent = matcher.group(1);
//        }
//        return MatchContent;
//    }

//    public JSONObject replacePlaceholder(JSONObject poc, String placeholder, String input){
//        try {
//            String pocString = JSON.toJSONString(poc);
//            pocString = pocString.replace(placeholder,input);
//            poc = JSON.parseObject(pocString);
//        }catch (Exception e){
//
//        }
//        return poc;
//    }
//
//    public JSONObject PlaceholderHandling(JSONObject EXP, String input){
//        String MatchContent = new String();
//        String type = new String();
//        String encodeType = new String();
//        Integer num = 0;
//        String pocString = JSON.toJSONString(Exp);
//        try{
//            Pattern ExpPattern =Pattern.compile("\\{([a-zA-Z0-9]*):([a-zA-Z0-9]*):(\\d+)\\}" ,Pattern.DOTALL);
//            Matcher matcher = ExpPattern.matcher(pocString);
//            if (matcher.find()){
//                MatchContent = matcher.group();
//                type = matcher.group(1);
//                encodeType = matcher.group(2);
//                num = Integer.parseInt(matcher.group(3));
//                String out = PocUtil.multipleEncode(encodeType, input , num);
//                pocString = pocString.replace(MatchContent,out);
//            }
//        }
//        catch (Exception e){
//
//        }
//        return JSONObject.parseObject(pocString);
//    }
    @Override
    public HashMap<String,String> exploitVul(){
        return this.result;
    }



    @Override
    public void initStep(String value){
        JSONObject stepContent = this.Exp.getJSONObject(value);
        this.ExpPost = stepContent.getString("expPost");
        this.ExpGet = stepContent.getString("expGet");
        this.pattern = stepContent.getString("Pattern");
        this.header = new HashMap<>(stepContent.getJSONObject("header"));
    }

    @Override
    public Boolean ExpRequest(Response res,String type) {
        if (res.getText()==null || res.getCode()==404){
            this.isExploited = false;
            if (this.result.keySet().contains("result")) this.result.remove("result");
//                    this.result.put("prompt","error");
            this.result.put("prompt","利用失败，请检查poc可用性");
            return true;
        }
        String resText = this.resMatch(res.getText(),this.pattern);
        if (resText.equals("error")){
            this.isExploited = false;
            if (this.result.keySet().contains("result")) this.result.remove("result");
        }
        if (!resText.equals("error")){
            this.isExploited = true;
            this.result.put("result",resText);
            if (type.equals("upload") && !this.resMatch(resText,"(.*\\.[a-zA-Z]{1,4})").equals(null)){
                this.result.put("shellPath",resText);
            };
        }
        return false;
    }

//    public HashMap<String, String> exploitVul(){
//        Integer i =1;
//        String ExpPost = "";
//        String ExpGet = "";
//        String Pattern = "";
//        String value;
//        System.out.println(this.Exp.keySet());
//        for (Integer n=1;n<=this.Exp.keySet().size();i++){
//            value= "step"+Integer.toString(n);
//            JSONObject stepContent = this.Exp.getJSONObject(value);
//            ExpPost = stepContent.getString("expPost");
//            ExpGet = stepContent.getString("expGet");
//            Pattern = stepContent.getString("Pattern");
//            this.header = new HashMap<>(stepContent.getJSONObject("header"));
//            for(String key:this.header.keySet()){
//                String a = (String) this.header.get(key);
//                if (a.contains("{0}"))
//            }
//        }
//        for (String value:this.Exp.keySet()){
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

package com.bcvgh.core.exp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.BaseTemplate;
import com.bcvgh.utils.PocUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Random ran = new Random();
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        this.Exp = poc.getJSONObject("exp");
        this.Exp = this.replacePlaceholder(this.Exp,"{url}",this.Url);
        this.Exp = this.replacePlaceholder(this.Exp,"{random}",String.valueOf(randomNumber));
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

    public HashMap<String,String> exploitVul(){
        return this.result;
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

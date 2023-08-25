package com.bcvgh.payloads;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.util.HttpTools;
import com.bcvgh.util.JsonUtil;
import com.bcvgh.util.Response;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VulTemplateImp implements VulTemplate{
//    private JSONObject jsonPoc;
    private String Url;
    private Boolean isVul;
    private Boolean isExploited;
    private JSONObject Poc;
    private JSONObject Exp;
    private String name;
    private String tag;
    private String type;
    private HashMap<String,?> header;
    private Queue<String> Content = new ArrayDeque<>();

    public VulTemplateImp(String url, JSONObject poc) {
        this.Url = url;
        this.name = poc.getString("name");
        this.tag = poc.getString("tag");
        this.type = poc.getString("type");
        this.Poc = poc.getJSONObject("poc");
        this.Exp = poc.getJSONObject("exp");
        this.header = new HashMap<>(this.Poc.getJSONObject("header"));
//        this.jsonPoc = poc;

    }

    private Boolean PocMatch(String resText,String pattern){
//        String pattern = this.PocPattern;
//        Pattern PocPattern = Pattern.compile(pattern);
//        Matcher matcher = PocPattern.matcher(resText);
        Boolean matcher = resText.contains(pattern);
        return matcher;
    }

    private String ExpMatch(String resText,String pattern){
        Pattern ExpPattern =Pattern.compile(pattern ,Pattern.DOTALL);
        Matcher matcher = ExpPattern.matcher(resText);
        String MatchContent ="";
        if (matcher.find()){
            MatchContent = matcher.group();
        }
        return MatchContent;
    }




    @Override
    public String checkVul() {
        String PocPost = this.Poc.getString("pocPost");
        if (this.type.equals("upload")){

        }
        String PocGet = this.Poc.getString("pocGet");
        String Pattern = this.Poc.getString("Pattern");
        if (PocPost != null){
            Response res = HttpTools.post(this.Url.concat(PocGet),PocPost,this.header,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                return "";
            }
            if (this.PocMatch(res.getText(),this.Poc.getString("Pattern"))){
                this.isVul =true;
                return this.name;
            }
        }
        else if (PocGet != null){
            Response res = HttpTools.get(this.Url.concat(PocGet), this.header ,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                return "";
            }
            if (this.PocMatch(res.getText(),Pattern)){
                this.isVul = true;
                return this.name;
            }
        }
       return "";

    }

    public String exploitVul(){
        Integer i =1;
        String ExpPost = "";
        String ExpGet = "";
        String Pattern = "";
        for (String value:this.Exp.keySet()){
            value = "step"+Integer.toString(i);
            ExpPost = this.Exp.getJSONObject(value).getString("expPost");
            ExpGet = this.Exp.getJSONObject(value).getString("expGet");
            Pattern = this.Exp.getJSONObject(value).getString("Pattern");
            String resText;
            if (ExpGet.contains("%s")&& !Content.isEmpty()){
                ExpGet = String.format(ExpGet,Content.poll());
            }
            if (ExpPost != null){
                Response res = HttpTools.post(this.Url.concat(ExpGet),ExpPost,this.header,"UTF-8");
                if (res.getText()==null){
                    return "";
                }
                resText = this.ExpMatch(res.getText(),Pattern);
                if (resText.equals(null)){ /**之后会换成异常捕获**/
                    this.isExploited = false;
                }
                if (!resText.equals(null)){
                    this.isExploited = true;
                    Content.offer(resText);
                }
            }
            else if (ExpGet != null){
                Response res = HttpTools.get(this.Url.concat(ExpGet), this.header ,"UTF-8");
                if (res.getText()==null) {
                    return "";
                }
                if (this.ExpMatch(res.getText(),Pattern).equals(null)){ /**之后会换成异常捕获**/
                    this.isExploited = false;
                }
                if (!this.ExpMatch(res.getText(),this.Poc.getString("Pattern")).equals(null)){
                    this.isExploited = true;
                }
            }
            i = i + 1;
        }
        if (this.isExploited) {
            if (this.type.equals("upload")) {
                System.out.println(this.Url);
                return "webshell已上传，请自行检测可用性！\n" + this.Url.concat(ExpGet);
            }
            if (this.type.equals("cmd")) {
                if (!Content.isEmpty()){
                    return Content.poll();
                }
                return "";
            }
            return this.name;
        }
        return "";
    }

}

package com.bcvgh.core.poc;

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

    public PocTemplateImp(String url, JSONObject poc) {
        super(url, poc);
        this.Poc = poc.getJSONObject("poc");
        /**placeholder**/
        this.Poc = this.replacePlaceholder(this.Poc,"{url}",this.Url);
        /**placeholder**/
        this.header = new HashMap<>(this.Poc.getJSONObject("header"));
        this.PocPost = this.Poc.getString("pocPost");
        this.PocGet = this.Poc.getString("pocGet");
        this.Pattern = this.Poc.getString("Pattern");

    }

    private Boolean PocMatch(String resText,String pattern){
        Boolean matcher = resText.contains(pattern);
        return matcher;
    }


    @Override
    public ArrayList<HashMap<String, String>> checkVul() {
        HashMap<String,String> result = new HashMap<>();
        result.put("name",this.name);
        if (PocPost != null){
            Response res = HttpTools.post(this.Url.concat(PocGet),PocPost,this.header,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                result.put("res","false");
                Result.add(result);
                return this.Result;
            }
            if (this.PocMatch(res.getText(),this.Poc.getString("Pattern"))){
                result.put("res",res.getText());
                Result.add(result);
                return this.Result;
            }
        }
        else if (PocGet != null){
            Response res = HttpTools.get(this.Url.concat(PocGet), this.header ,"UTF-8");
            if (res.getText()==null){ /**之后会换成异常捕获**/
                result.put("res","false");
                Result.add(result);
                return this.Result;
            }
            if (this.PocMatch(res.getText(),Pattern)){
//                this.isVul =true;
                result.put("res",res.getText());
                Result.add(result);
                return this.Result;
            }
        }
        result.put("res","false");
        Result.add(result);
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

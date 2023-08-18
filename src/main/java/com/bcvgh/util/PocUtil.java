package com.bcvgh.util;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.payloads.VulPocTemplateImp;
import javafx.application.Platform;

public class PocUtil {
    private String PocS;
    private VulPocTemplateImp Poc;

//    public PocUtil(String url,String VulName){
//        this.PocS = FileUtil.FileRead("src/main/java/com/bcvgh/poc/json/"+Poc.getTag()+"/"+VulName+".json");
//        JSONObject PocContent = JsonUtil.StringToJson(PocS);
//        this.Poc = new VulPocTemplateImp(url, PocContent);
//    }

    public  PocUtil(String url,String tag ,String name){
        this.PocS = FileUtil.FileRead("./poc/json/"+tag+"/"+name+".json");
        JSONObject PocContent = JsonUtil.StringToJson(PocS);
        this.Poc = new VulPocTemplateImp(url, PocContent);
    }

    public String POC(){
        return this.Poc.checkVul();
    }
}


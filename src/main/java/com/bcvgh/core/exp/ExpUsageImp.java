package com.bcvgh.core.exp;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.BaseUsage;
import com.bcvgh.utils.PocUtil;

import java.util.HashMap;

public class ExpUsageImp extends BaseUsage {
    private ExpTemplateImp expTemplateImp;
    public HashMap<String,String> result = new HashMap<>();

    public ExpUsageImp(String url , String tag , String name ,HashMap<String,String> Extra){
        super(url, tag ,name);
        this.ExpUsage(this.url, this.tag, this.name ,Extra);
    }

    private void ExpUsage(String url, String tag, String name ,HashMap<String,String> Extra){
        JSONObject PocContent = PocUtil.GetPocs(tag,name);
        switch (PocUtil.type){
            case "upload":
                this.expTemplateImp = new UploadTemplateImp(url,PocContent, Extra);
                this.result = this.expTemplateImp.exploitVul();
                break;
            case "sql":
                break;
            case "deserialize":
                break;
            case "exec":
                this.expTemplateImp = new ExecTemplateImp(url ,PocContent , Extra);
                this.result = this.expTemplateImp.exploitVul();
                break;
        }
//        HashMap<String, String> result = this.expTemplateImp.exploitVul();

    }
}

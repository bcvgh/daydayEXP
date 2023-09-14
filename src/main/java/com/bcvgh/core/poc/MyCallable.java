// -*- coding: utf-8 -*-
package com.bcvgh.core.poc;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;


class MyCallable implements Callable<Object> {
    private String url;
    private JSONObject PocContent;

    MyCallable(String url ,JSONObject pocContent) {
        this.url = url;
        this.PocContent = pocContent;
    }

    @Override
    public Object call() throws Exception {
//        PocUtil poc = new PocUtil(this.url, this.tag ,this.name);
        PocTemplateImp vulTemplate = new PocTemplateImp(this.url, this.PocContent);
        ArrayList<HashMap<String,String>> output = vulTemplate.checkVul();
        return output;
    }
}
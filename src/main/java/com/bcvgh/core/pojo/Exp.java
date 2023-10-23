package com.bcvgh.core.pojo;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.PocUtil;

public class Exp {
    private String exp_get;
    private String exp_post;
    private String exp_Pattern;
    private String exp_header;

    public String getExp_get() {
        return exp_get;
    }

    public void setExp_get(String exp_get) {
        this.exp_get = exp_get;
    }

    public String getExp_header() {
        return exp_header;
    }

    public void setExp_header(String exp_header) {
        this.exp_header = exp_header;
    }

    public String getExp_post() {
        return exp_post;
    }

    public void setExp_post(String exp_post) {
        this.exp_post = exp_post;
    }

    public String getExp_Pattern() {
        return exp_Pattern;
    }

    public void setExp_Pattern(String exp_Pattern) {
        this.exp_Pattern = exp_Pattern;
    }
}

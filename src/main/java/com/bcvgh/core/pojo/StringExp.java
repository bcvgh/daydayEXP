package com.bcvgh.core.pojo;

import java.util.HashMap;
import java.util.regex.Pattern;

public class StringExp {
    private String exp_get;
    private String exp_post;
    private String exp_pattern;
    private String exp_header;
    private String exp_status_code;

    public String getExp_get() {
        return exp_get;
    }

    public void setExp_get(String exp_get) {
        this.exp_get = exp_get;
    }

    public String getExp_post() {
        return exp_post;
    }

    public void setExp_post(String exp_post) {
        this.exp_post = exp_post;
    }

    public String getExp_pattern() {
        return exp_pattern;
    }

    public void setExp_pattern(String exp_pattern) {
        this.exp_pattern = exp_pattern;
    }

    public String getExp_header() {
        return exp_header;
    }

    public void setExp_header(String exp_header) {
        this.exp_header = exp_header;
    }

    public String getExp_status_code() {
        return exp_status_code;
    }

    public void setExp_status_code(String exp_status_code) {
        this.exp_status_code = exp_status_code;
    }
}

package com.bcvgh.core.pojo;

public class StringPoc {
    private String poc_get;
    private String poc_post;
    private String poc_pattern;
    private String poc_header;
    private String status_code;

    public StringPoc(String poc_get, String poc_post, String poc_pattern, String poc_header, String status_code) {
        this.poc_get = poc_get;
        this.poc_post = poc_post;
        this.poc_pattern = poc_pattern;
        this.poc_header = poc_header;
        this.status_code = status_code;
    }

    public String getPoc_get() {
        return poc_get;
    }

    public void setPoc_get(String poc_get) {
        this.poc_get = poc_get;
    }

    public String getPoc_post() {
        return poc_post;
    }

    public void setPoc_post(String poc_post) {
        this.poc_post = poc_post;
    }

    public String getPoc_pattern() {
        return poc_pattern;
    }

    public void setPoc_pattern(String poc_pattern) {
        this.poc_pattern = poc_pattern;
    }

    public String getPoc_header() {
        return poc_header;
    }

    public void setPoc_header(String poc_header) {
        this.poc_header = poc_header;
    }

    public String getstatus_code() {
        return status_code;
    }

    public void setstatus_code(String status_code) {
        this.status_code = status_code;
    }
}

package com.bcvgh.core.pojo;

public class Poc{
    private String poc_get;
    private String pco_post;
    private String poc_Pattern;
    private String poc_header;

    public Poc(String poc_get, String pco_post, String poc_Pattern, String poc_header) {
        this.poc_get = poc_get;
        this.pco_post = pco_post;
        this.poc_Pattern = poc_Pattern;
        this.poc_header = poc_header;
    }

    public String getPoc_get() {
        return poc_get;
    }

    public void setPoc_get(String poc_get) {
        this.poc_get = poc_get;
    }

    public String getPco_post() {
        return pco_post;
    }

    public void setPco_post(String pco_post) {
        this.pco_post = pco_post;
    }

    public String getPoc_Pattern() {
        return poc_Pattern;
    }

    public void setPoc_Pattern(String poc_Pattern) {
        this.poc_Pattern = poc_Pattern;
    }

    public String getPoc_header() {
        return poc_header;
    }

    public void setPoc_header(String poc_header) {
        this.poc_header = poc_header;
    }
}

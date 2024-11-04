package com.bcvgh.core.pojo;

import java.util.ArrayList;
import java.util.HashMap;

public class PocAll {
    private Integer id;
    private String VulName;
    private String tag;
    private String type;
    private HashMap<String,Exp> exp;
    private Poc poc;

    public PocAll(Integer id, String vulName, String tag, String type, HashMap<String,Exp> exp, Poc poc) {
        this.id = id;
        this.VulName = vulName;
        this.tag = tag;
        this.type = type;
        this.exp = exp;
        this.poc = poc;
    }

    public PocAll(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVulName() {
        return VulName;
    }

    public void setVulName(String vulName) {
        VulName = vulName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String,Exp> getExp() {
        return exp;
    }

    public void setExp(HashMap<String,Exp> exp) {
        this.exp = exp;
    }

    public Poc getPoc() {
        return poc;
    }

    public void setPoc(Poc poc) {
        this.poc = poc;
    }
}

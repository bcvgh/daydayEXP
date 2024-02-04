package com.bcvgh.core.pojo;

public class Input {
    private String dnslog;
    private String command;
    private String shellpath;
    private String webshell;
    private String url;
    private String random;
    private byte[] serialization;

    public Input(String dnslog, String command, String shellpath, String webshell, String url, String random,  byte[] serialization) {
        this.dnslog = dnslog;
        this.command = command;
        this.shellpath = shellpath;
        this.webshell = webshell;
        this.url = url;
        this.random = random;
        this.serialization = serialization;
    }

    public String getDnslog() {
        return dnslog;
    }

    public void setDnslog(String dnslog) {
        this.dnslog = dnslog;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getShellpath() {
        return shellpath;
    }

    public void setShellpath(String shellpath) {
        this.shellpath = shellpath;
    }

    public String getWebshell() {
        return webshell;
    }

    public void setWebshell(String webshell) {
        this.webshell = webshell;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public byte[] getSerialization() {
        return serialization;
    }

    public void setSerialization(byte[] serialization) {
        this.serialization = serialization;
    }
}

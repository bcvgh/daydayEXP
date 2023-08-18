package com.bcvgh.util;

import java.util.concurrent.Callable;

public class PocThread implements Callable<String> {
    //    private int taskId;
    private String url;
    private String name;
    private String tag;

    public PocThread(String url, String name ,String tag) {
        this.url = url;
        this.name = name;
        this.tag = tag;
//        this.taskId = taskId;
    }


    @Override
    public String call() throws Exception{
        System.out.println("开始扫描"+name);
        PocUtil poc = new PocUtil(url, name ,tag);
        try {
            Thread.sleep(2000); // 模拟耗时操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return poc.POC();
//        System.out.println("Task " + taskId + " completed");
    }
}

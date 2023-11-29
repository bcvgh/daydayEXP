package com.bcvgh.core.poc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.BaseUsage;
import com.bcvgh.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class PocUsageImp extends BaseUsage {
    private PocTemplateImp pocTemplate;
    public ArrayList<String> prompt = new ArrayList<>();

    public PocUsageImp(String url , String tag , String name){
        super(url, tag ,name);
        this.PocUsage(this.url, this.tag, this.name);
    }

    public PocUsageImp(String url , String tag){
        super(url, tag);
        this.PocUsage(this.url, this.tag);
    }


    private void PocUsage(String url, String tag, String name){
        this.PocS = FileUtil.FileRead(PocUtil.PocPath+"json"+File.separator+tag+File.separator+name+".json");
        JSONObject PocContent = JSON.parseObject(PocS);
        this.pocTemplate = new PocTemplateImp(url, PocContent);
        ArrayList<HashMap<String,String>> result = this.pocTemplate.checkVul();
        this.PromptAdd(result);

    }

    private void PocUsage(String url, String tag){
        ArrayList<String> names = PocUtil.tag_name.get(tag);
        List<Future<Object>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(5);  /**创建线程池，多线程处理并返回结果**/
        for (String name : names) {
            this.PocS = FileUtil.FileRead(PocUtil.PocPath+"json"+File.separator + tag + File.separator + name + ".json");
            JSONObject PocContent = JSON.parseObject(PocS);
            Callable c = new MyCallable(url, PocContent);
            Future<Object> future = executor.submit(c);
            futures.add(future);
        }
//        ArrayList<String> vul_prompt = new ArrayList<>();
        for (Future<Object> future : futures) {
            try {
                ArrayList<HashMap<String,String>> result = (ArrayList<HashMap<String,String>>) future.get();
                this.PromptAdd(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
//        this.Result.put("prompt", vul_prompt);
        executor.shutdown();
    }


    private void PromptAdd(ArrayList<HashMap<String, String>> result){
        for (HashMap<String,String> n: result){
            if (!n.get("res").equals("false")){

                this.prompt.add("[+] 目标存在"+n.get("name")+"漏洞!请使用对应的漏洞利用模块！\n");
            }
            else {
                this.prompt.add("[-] 目标不存在"+n.get("name")+"漏洞...\n");
            }
        }
    }
//    public Poc(String url, String tag , String name){
//        this.PocS = FileUtil.FileRead("src/main/java/com/bcvgh/poc/json/"+tag+File.separator+name+".json");
//        JSONObject PocContent = JsonUtil.StringToJson(PocS);
//        this.Poc = new VulTemplateImp(url, PocContent);
//    }

//    public Poc(String url, String tag){
//        ArrayList<String> names =PocUtil.tag_name.get(tag);
//        List<Future<Object>> futures = new ArrayList<>();
//        ExecutorService executor = Executors.newFixedThreadPool(5);  /**创建线程池，多线程处理并返回结果**/
//        for (String name:names){
//            Callable c = new MyCallable(url,tag,name);
//            Future<Object> future = executor.submit(c);
//            futures.add(future);
//        }
//        for (Future<Object> future : futures) {
//            try {
//                Poc result = (Poc) future.get();
//                if (!result.POC().equals("")){
//                    Platform.runLater(() -> VulOut.appendText("目标存在"+result.POC()+"漏洞!\n"));
//                }
//                else {
//                    Platform.runLater(() -> VulOut.appendText("目标不存在"+result.POC()+"漏洞!\n"));
//                }
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        executor.shutdown();
//    }

//    public String POC(){
//        return this.Poc.checkVul();
//    }

//    public String EXP(){
//        return this.Poc.exploitVul();
//    }
}


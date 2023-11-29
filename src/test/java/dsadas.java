import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.*;
import org.junit.Test;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class dsadas {

    @Test
    public void teete() throws UnsupportedEncodingException {
                System.getProperties().setProperty("https.proxyHost", "127.0.0.1");
        System.getProperties().setProperty("https.proxyPort", "8080");
        System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
        System.getProperties().setProperty("http.proxyPort", "8080");
        String Header =
                        "Accept-Encoding: */*\n" +
                        "User-Agent: PostmanRuntime/7.35.0\n" +
                        "Connection: close\n";
        HashMap<String, String> headerMap = new HashMap<>();
        String[] lines = Header.split("\n");

        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                headerMap.put(parts[0], parts[1]);
            }
        }
        String  name = "用友NCcloud uapjs上传命令执行.json";
        String[] names = name.split("\\.");
        System.out.println(names[0]);
        String url ="https://github.com/bcvgh/daydayExp-pocs/blob/main/yongyou/"+URLEncoder.encode(names[0])+".json" ;
        url = url.replaceAll("\\+","%20");
        System.out.println(url);
        Response response = HttpTools.get(url,headerMap,"UTF-8");
        System.out.println(response.getText());
    }

    @Test
    public void add() throws ExecutionException, InterruptedException {
        System.out.println("----程序开始运行----");
        Date date1 = new Date();

        // 线程池数量
        int taskSize = 5;
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future<Object>> list = new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future<Object> f = executorService.submit(c);
            System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        executorService.shutdown();

        // 获取所有并发任务的运行结果
        for (Future<Object> f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }

        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【" + (date2.getTime() - date1.getTime()) + "毫秒】");
    }

//    private void getdirs(String path){
//        String pocPath = PocUtil.PocPath+"json"+ File.separator;
//        Response response = HttpTools.get(pocPath,headerMap,"Utf-8");
//        String regex = "/tree/main/[0-9a-zA-Z]*\">([0-9a-zA-Z]*)<";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(response.getText());
//        List<String> dirs = Arrays.asList(FileUtil.FileList(pocPath));
//        while (matcher.find()){
////            System.out.println(matcher.group(1));
//            if (!dirs.contains(matcher.group(1))) FileUtil.Mkdir(matcher.group(1));
//            gefiles(matcher.group(1));
//        }
//
//        String[] files =  FileUtil.FileList(pocPath);
//    }

    public void getFiles(String dirName,HashMap header) throws UnsupportedEncodingException {
        String Path = "https://github.com/bcvgh/daydayExp-pocs/";
        String pocPath = Path+"tree/main/"+dirName+"/";
        Response response = HttpTools.get(pocPath,header,"UTF-8");
        String regex = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response.getText());
        while (matcher.find()){
            JSONObject json = JSON.parseObject(matcher.group(1));
            JSONArray jsonArray = json.getJSONObject("payload").getJSONObject("tree").getJSONArray("items");
            for (int i = 0 ;i<jsonArray.size();i++){
                JSONObject a = jsonArray.getJSONObject(i);
                String name = String.valueOf(a.get("name"));
                Response response1 = HttpTools.get(Path+"blob/main/"+dirName+"/"+URLEncoder.encode(name,"UTF-8").replaceAll("\\+","%20"),header,"UTF-8");
                String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
                Pattern pattern1 = Pattern.compile(regex1);
                Matcher matcher1 = pattern1.matcher(response1.getText());
                if (matcher1.find()){
                    String pocs= "";
                    JSONObject jsonObject = JSONObject.parseObject(matcher1.group(1));
                    JSONArray  jsonArray1 = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
                    for (int s =0;s<jsonArray1.size();s++){
                        String poc = jsonArray1.getString(s);
                        pocs = pocs+poc;
                    }
                    System.out.println(pocs);
                }
            }

        }
    }

    @Test
    public void it() throws UnsupportedEncodingException {
        //    static {
        System.getProperties().setProperty("socksProxyHost", "127.0.0.1");
        System.getProperties().setProperty("socksProxyPort", "1080");

//    }
        String Header =
                "Content-Length: 29\n" +
                        "Accept-Encoding: */*\n" +
                        "User-Agent: PostmanRuntime/7.35.0\n" +
                        "Connection: close\n";
        HashMap<String, String> headerMap = new HashMap<>();
        String[] lines = Header.split("\n");

        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                headerMap.put(parts[0], parts[1]);
            }
        }
//        Pattern ExpPattern =Pattern.compile("jsp");
//        Matcher matcher = ExpPattern.matcher("{\"data\":{\"id\":59,\"path\":\"VIDEO/230824120904159934.jsp\"},\"errMsg\":\"success!\",\"success\":true}");
//        String MatchContent = matcher.group();
//        System.out.println(MatchContent);

        String path = "https://github.com/bcvgh/daydayExp-pocs";
        getFiles("yongyou",headerMap);
//        Response response = HttpTools.get(path,headerMap,"Utf-8");
//        String regex = "/tree/main/[0-9a-zA-Z]*\">([0-9a-zA-Z]*)<";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(response.getText());
//        while (matcher.find()){
//            System.out.println(matcher.group(1));
        }
//        String input = "{\"data\":{\"id\":59,\"path\":\"VIDEO/230824120904159934.jsp\"},\"errMsg\":\"success!\",\"success\":true}";
//
//        // Regular expression to match JSON data containing ".jsp" path
//        String regex = "[0-9a-zA-Z]*\\.jsp";
//
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(input);
//
//        if (matcher.find()) {
////            String jspPath = matcher.group(1);
//            System.out.println("Extracted JSP Path: " + matcher.group());
//        } else {
//            System.out.println("No match found.");
//        }
//        HashMap<String , ArrayList<String>> vulName = new HashMap<>();
//        String[] dirNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json");
//        for (String dirName : dirNames){
//            ArrayList<String> filenames = new ArrayList<>();
//            String[] fileNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json/"+dirName);
//            for (String fileName : fileNames){
//                String poc = FileUtil.FileRead("src/main/java/com/bcvgh/poc/json/"+dirName+"/"+fileName);
//                JSONObject pocJson = JsonUtil.StringToJson(poc);
//                filenames.add(pocJson.getString("name"));
//            }
//            vulName.put(dirName,filenames);
//        }


}

class MyCallable implements Callable<Object> {
    private String taskNum;

    MyCallable(String taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public Object call() throws Exception {
        System.out.println(">>>" + taskNum + "任务启动");
        Date dateTmp1 = new Date();
        Thread.sleep(1000);
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        System.out.println(">>>" + taskNum + "任务终止");
        return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
    }
}



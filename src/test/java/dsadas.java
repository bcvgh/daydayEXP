import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class dsadas {
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

    @Test
    public void it(){
        /**遍历每个jsonpoc文件的name,tag**/
//        Pattern ExpPattern =Pattern.compile("jsp");
//        Matcher matcher = ExpPattern.matcher("{\"data\":{\"id\":59,\"path\":\"VIDEO/230824120904159934.jsp\"},\"errMsg\":\"success!\",\"success\":true}");
//        String MatchContent = matcher.group();
//        System.out.println(MatchContent);
        String input = "{\"data\":{\"id\":59,\"path\":\"VIDEO/230824120904159934.jsp\"},\"errMsg\":\"success!\",\"success\":true}";

        // Regular expression to match JSON data containing ".jsp" path
        String regex = "[0-9a-zA-Z]*\\.jsp";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
//            String jspPath = matcher.group(1);
            System.out.println("Extracted JSP Path: " + matcher.group());
        } else {
            System.out.println("No match found.");
        }
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



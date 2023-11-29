import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    @Test
    public void aaa() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        String a = multipleEncode("UrlEncode","ping 828afe11.dnslog.store",3);
//        System.out.println(a);
//        ApiUtil.ceyeDnslog("abc.0nva74.ceye.io","be94361a3b2d8f6ec7dfe0a3275ab79e");
        String a= System.getProperty("user.dir");
        String[] aa =FileUtil.FileList(a);
        List<String> asa = Arrays.asList(aa);
        if (asa.contains("posac")) System.out.println(123);
//        String regex = "<a href=\"(.*)/\">";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(content);
//        while (matcher.find()){
//
//        }
        System.out.println(aa[0]);
    }

    @Test
    public void bbb() throws IOException {
        String pocUrl = "";
        String pocs = "";
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
        pocUrl = "https://github.com/bcvgh/daydayExp-pocs/blob/main/dahua/%E5%A4%A7%E5%8D%8E%E6%99%BA%E6%85%A7%E5%9B%AD%E5%8C%BA%E7%AE%A1%E7%90%86%E5%B9%B3%E5%8F%B0%E6%96%87%E4%BB%B6%E4%B8%8A%E4%BC%A0.json";
        Response response1 = HttpTools.get(pocUrl, headerMap, "UTF-8");
        String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(response1.getText());
        if (matcher1.find()) {
            JSONObject jsonObject = JSONObject.parseObject(matcher1.group(1));
            JSONArray jsonArray1 = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
            for (int s = 0; s < jsonArray1.size(); s++) {
                String poc = jsonArray1.getString(s);
                pocs = pocs + poc;
            }
//            pocs.replaceAll("\\r\\r\\n","\\r\\n");
        }
        FileUtil.FileWrite("1.json",pocs,"dahua");
    }

    public static String Encode(String methodName,String text) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class cls = Class.forName("com.bcvgh.utils.Encode");
        Method m = cls.getMethod(methodName, String.class);
        String out = (String) m.invoke(null,text);
        return out;
    }

    public static String multipleEncode(String methodName, String text, Integer num) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String out = text;
        for (Integer i =1 ; i<=num; i++){
            out = Encode(methodName, out);
        }
        return out;
    }

    @Test
    public void aa(){
        System.getProperties().setProperty("https.proxyHost", "127.0.0.1");
        System.getProperties().setProperty("https.proxyPort", "8080");
        String url ="https://github.com/bcvgh/daydayExp-pocs/tree/main/";
        Response res = HttpTools.get(url, new HashMap<>(PocUtil.PojoHeader_toJson(PocUtil.defaultHeader)) ,"UTF-8");
        if (res.getText()!=null){
//             String[] a = res.getText().split("<td id=\"LC1\" class=\"blob-code blob-code-inner js-file-line\">")[1].split("</td>");
            String pattern = "/bcvgh/daydayExp-pocs/tree/main/[a-z]*";
            Pattern pattern1 = Pattern.compile(pattern);
            Matcher matcher = pattern1.matcher(res.getText());
            System.out.println(matcher.group(0));
        }
    }


}

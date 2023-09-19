import com.bcvgh.utils.ApiUtil;
import com.bcvgh.utils.PocUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    @Test
    public void aaa() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        String a = multipleEncode("UrlEncode","ping 828afe11.dnslog.store",3);
//        System.out.println(a);
        ApiUtil.ceyeDnslog("abc.0nva74.ceye.io","be94361a3b2d8f6ec7dfe0a3275ab79e");
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


}

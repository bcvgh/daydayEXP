package com.bcvgh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;

public class InitUtil {
    public  void init() {
        this.initVul();
    }

    private  void initVul() {
        File file = new File("D:\\test\\exp\\src\\config.json");

        //            String content = FileUtils.readFileToString(file);
//            JSONObject abc = (JSONObject) JSON.parse(content);
//            System.out.println(abc.get("VulName"));
//            HashMap<String , Object> VulMap = new HashMap();
//            String VulName = "Vulname";
//            VulMap.put(VulName , abc.get("VulName"));
//            System.out.println("初始化poc");


//        hBox.getChildren().addAll(field,b1,b2,b3);

    }



}

package com.bcvgh.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Encode<T> {
    private static final Logger LOGGER = LogManager.getLogger(Encode.class.getName());
    public Object encoded;
    public Encode(String methodName, T text) {
        this.encoded = singleEncode(methodName,text);
    }

    public Encode(String methodName, T text, Integer num) {
        this.encoded = multipleEncode(methodName,text,num);
    }

    private  Object singleEncode(String methodName, T text){
        Class cls = null;
        try {
            cls = Class.forName("com.bcvgh.utils.EncodeUtil");
        } catch (ClassNotFoundException e) {
            LOGGER.warn(e.getMessage()+"(无编码方法!)");
        }
        Method m = null;
        try {
            m = cls.getMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
            try {
                m = cls.getMethod(methodName, byte[].class);
            } catch (NoSuchMethodException ex) {
                try {
                    m = cls.getMethod(methodName, Object.class);
                } catch (NoSuchMethodException exc) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        Object out = null;
        try {
            out =  m.invoke(null,text);
        } catch (IllegalAccessException e) {
            LOGGER.warn(e.getMessage());
//            e.printStackTrace();
        } catch (InvocationTargetException e) {
            LOGGER.warn(e.getMessage());
//            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
//            e.printStackTrace();
        }
        return out;
    }

    private  Object multipleEncode(String methodName, T text, Integer num){
        Object out = text;
        for (Integer i =1 ; i<=num; i++){
            out = singleEncode(methodName, (T) out);
        }
        return out;
    }
}

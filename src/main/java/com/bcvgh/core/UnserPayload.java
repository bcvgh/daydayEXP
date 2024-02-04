package com.bcvgh.core;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class UnserPayload {
//    private String bytecode;
    public Class<? extends ObjectPayload> gadgetClass;
    private UnSerInput unSerInput;
    private static final Logger LOGGER = LogManager.getLogger(UnserPayload.class.getName());

    public UnserPayload(UnSerInput unSerInput) {
        this.unSerInput = unSerInput;
        this.gadgetClass = ObjectPayload.getPayloadClass(unSerInput.getGadget(), unSerInput.getType());
    }



    public <T> Object GadgetPayloadGenerate() {
        try {
            String className = this.unSerInput.getGadget();
            Object object = ObjectPayload.getObjectPayload(className , this.unSerInput);
            return object;
        }catch (Exception e){
            LOGGER.warn(e.getMessage()+"构造链错误!");
            return null;
        }


//        switch (input.getType()){
//            case "exec":
////                input.setGadget(className+"Exec");
//                className = className+"Exec";
//            break;
//            case "bytecode":
////                input.setGadget(className+"Bytecode");
//                className = className+"Bytecode";
//            break;
//            case "bcel":
////                input.setGadget(className+"Bcel");
//                className = className+"Bcel";
//            break;
//            default:break;
//        }
//        this.gadgetClass = ObjectPayload.getPayloadClass(className,input.getType());


    }


//    public byte[] PayloadGenerate(String methods ,String tag ,byte[] payloadObject){
//        byte[] bytes = (byte[]) payloadObject;
//        Class clz = null;
//        try {
//            clz = Class.forName("com.bcvgh.core.unser.utils."+tag+"Util");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Method method = null;
//        try {
//            method = clz.getDeclaredMethod(methods,byte[].class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        try {
//            bytes = (byte[]) method.invoke(null,bytes);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return bytes;
//    }

//    public <T> T EncodePayloadGenerate(String encode){
//        byte[] bytes = (byte[]) this.payload;
//        Class clz = null;
//        try {
//            clz = Class.forName("com.bcvgh.core.unser.utils.EncodeUtil");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Method method = null;
//        try {
//            method = clz.getDeclaredMethod(other,Byte[].class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        try {
//            bytes =(byte[]) method.invoke(null,bytes);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        this.payload = bytes;
//        return (T) bytes;
//    }

    public static byte[] GenSerial(UnSerInput unSerInput){
        UnserPayload unserPayload = new UnserPayload(unSerInput);
        Object object = unserPayload.GadgetPayloadGenerate();
        byte[] serialBytes = ObjectPayload.getSerialBytes(object);
        return serialBytes;
    }


}

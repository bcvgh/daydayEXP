package com.bcvgh.core.unser.gadgets;

import com.bcvgh.core.Encode;
import com.bcvgh.core.unser.core.pojo.UnSerInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public interface ObjectPayload {

    <T> Object getObject (UnSerInput unSerInput) throws Exception;
    static final Logger LOGGER = LogManager.getLogger(ObjectPayload.class.getName());

    static   <T> byte[] getSerialBytes(final T input){
      try {
          ByteArrayOutputStream baous = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baous);
          oos.writeObject(input);
          byte[] bytes = baous.toByteArray();
          oos.close();
          return bytes;
      }catch (Exception e){
          LOGGER.error(e.getMessage());
          return null;
      }
    }

     static Class<? extends ObjectPayload> getPayloadClass ( final String className ,String payloadType ) {
        String type = payloadType==null ? "other" : payloadType;
        Class<? extends ObjectPayload> clazz = null;
        try {
            clazz = (Class<? extends ObjectPayload>) Class.forName(className);
        }
        catch ( Exception e1 ) {

        }
        if ( clazz == null ) {
            try {
                String targetClassName = null;
                Package pk = ObjectPayload.class.getPackage();
                if(pk != null){
                    targetClassName = pk.getName() + "." + type + "." + className;
                }else{
                    String payloadClassName = ObjectPayload.class.getName();
                    targetClassName = payloadClassName.substring(0,payloadClassName.lastIndexOf(".")) + "." + className;
                }
                return clazz = (Class<? extends ObjectPayload>) Class.forName(targetClassName);
            } catch ( Exception e2 ) {
                LOGGER.error(e2.getMessage());
                return null;

            }
        }
        if ( clazz != null && !ObjectPayload.class.isAssignableFrom(clazz) ) {
            clazz = null;
        }
        return clazz;
    }

    static <T> Object getObjectPayload(final String className, UnSerInput unSerInput){
        final Class<? extends ObjectPayload> payloadClass =  ObjectPayload.getPayloadClass(className , unSerInput.getType());
        ObjectPayload payload = null;
        try {
            payload = payloadClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
        //根据当前类对象是否为CustomPayload，判断是否动态生成恶意类
        if (payload instanceof CustomPayload){
            try {
                unSerInput.setBytecode(((CustomPayload) payload).getCustomBytecode(unSerInput));
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }

        //根据当前类对象是否为BcelPayload，加载bcel字节码
        if (payload instanceof BcelPayload){
            unSerInput.setBcel(BcelPayload.getBcelBytecode(unSerInput));
//            input = (T) BcelPayload.getBcelBytecode((byte[]) input);
        }

//        Object object = payload.getObject("/System/Applications/Calculator.app/Contents/MacOS/Calculator");
        Object object = null;
        try {
            object = payload.getObject(unSerInput);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return object;
    }
}

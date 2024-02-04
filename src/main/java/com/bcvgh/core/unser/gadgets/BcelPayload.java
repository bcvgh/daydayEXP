package com.bcvgh.core.unser.gadgets;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.IOException;

public interface BcelPayload extends ObjectPayload{
    static String getBcelBytecode(UnSerInput unSerInput){
        try {
            String bcelcode = "$$BCEL$$"+ Utility.encode(unSerInput.getBytecode(),true);
            return bcelcode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

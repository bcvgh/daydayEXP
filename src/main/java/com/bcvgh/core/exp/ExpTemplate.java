package com.bcvgh.core.exp;

import com.bcvgh.utils.Response;

import java.rmi.MarshalledObject;
import java.util.Map;

public interface ExpTemplate {

//    String ExpMatch(String resText,String pattern);
    Map exploitVul();
    Boolean ExpRequest(Response res,String type);
    void initStep(String value);
}

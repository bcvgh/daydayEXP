package com.bcvgh.core.placeholder;

import org.apache.commons.text.StrLookup;
import java.util.Map;

public class CustomReplace extends StrLookup<String> {
    private final Map<String, String> valuesMap;

    public CustomReplace(Map<String, String> valuesMap) {
        this.valuesMap = valuesMap;
    }

    @Override
    public String lookup(String key) {
        return (String) PlaceHolder.EncodeMath(this.valuesMap,key);
    }

}

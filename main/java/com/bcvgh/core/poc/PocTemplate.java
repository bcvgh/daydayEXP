package com.bcvgh.core.poc;

import javax.security.sasl.SaslServer;
import java.util.ArrayList;
import java.util.HashMap;

public interface PocTemplate {
    ArrayList<HashMap<String,String>> checkVul();
}
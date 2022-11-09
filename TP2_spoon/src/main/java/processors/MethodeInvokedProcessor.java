package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;

import java.util.ArrayList;
import java.util.List;

public class MethodeInvokedProcessor extends AbstractProcessor<CtInvocation> {
    private List<CtInvocation> methodsInvoked = new ArrayList<>();

    public List<CtInvocation> getMethodsInvoked() {
        return methodsInvoked;
    }

    @Override
    public void process(CtInvocation ctInvocation) {
        methodsInvoked.add(ctInvocation);
    }

}

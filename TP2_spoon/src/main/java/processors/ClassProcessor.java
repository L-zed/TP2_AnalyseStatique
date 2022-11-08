package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.util.ArrayList;
import java.util.List;


public class ClassProcessor extends AbstractProcessor<CtClass> {

    private List<String> classesNames = new ArrayList<>();

    public List<String> getClassesNames() {
        return classesNames;
    }

    @Override
    public void process(CtClass ctClass) {
        //System.out.println(ctClass.getQualifiedName());
        classesNames.add(ctClass.getQualifiedName());
    }
}

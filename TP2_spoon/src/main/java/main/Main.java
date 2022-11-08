package main;

import builder.GraphBuilder;
import graph.Edge;
import graph.Graph;
import parser.SpoonParser;
import processors.ClassProcessor;
import processors.MethodeInvokedProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        SpoonParser spoonParser = new SpoonParser("/Users/lylia/Downloads/project2");
        spoonParser.configure();

        ClassProcessor classProcessor = new ClassProcessor();
        spoonParser.addProcessor(classProcessor);

        MethodeInvokedProcessor methodeInvokedProcessor = new MethodeInvokedProcessor();
        spoonParser.addProcessor(methodeInvokedProcessor);

        spoonParser.run();


        GraphBuilder graphConstructor = new GraphBuilder("graph");
        graphConstructor.createGraph(methodeInvokedProcessor.getMethodsInvoked(), classProcessor.getClassesNames() );
        graphConstructor.generateGraph();
        //graphConstructor.display();

    }

}

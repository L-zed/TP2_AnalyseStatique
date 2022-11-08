package builder;

import graph.Edge;
import graph.Graph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

import java.io.*;
import java.util.List;

import static java.lang.String.format;

public class GraphBuilder {
    private Graph graph = new Graph();

    private final String dotFilePath;
    private final String pngFilePath;

    public Graph getGraph() {
        return this.graph;
    }

    public GraphBuilder(String fileName) {
        dotFilePath = "src/main/resources/" + fileName + ".dot";
        pngFilePath = "src/main/resources/" + fileName + ".png";
        graph = new Graph();
    }

    public void createGraph(List<CtInvocation> methods, List<String> classesInApplication){
        for (CtInvocation ctInvocation : methods) {
            String classInvokedMethodName = getClassOfInvokedMethod(ctInvocation);
            String classCallerMethodName = getCallerClass(ctInvocation);
            if(classesInApplication.contains(classInvokedMethodName) ) {
                graph.addNode(classInvokedMethodName);
                graph.addNode(classCallerMethodName);
                graph.addEdge(classInvokedMethodName, classCallerMethodName);
            }
        }
    }

    private String getCallerClass(CtInvocation ctInvocation) {
        CtElement ctElement = ctInvocation;
        while (! (ctElement instanceof CtClass)) {
            ctElement = ctElement.getParent();
        }
        CtClass ctClass = (CtClass) ctElement;
        return ctClass.getQualifiedName();
    }

    public String getClassOfInvokedMethod(CtInvocation ctInvocation){
        String className = "";
        if (ctInvocation.getTarget() != null){
            className = ctInvocation.getTarget().getType().getQualifiedName();
        }
        else {
            className = getCallerClass(ctInvocation);
        }
        return className;
    }

    public void generateGraph() throws IOException {
        FileWriter writer = new FileWriter(dotFilePath);
        writer.write("digraph \"graph\" {\n");

        writer.write("edge[dir=none]\n");
        for (Edge edge : graph.getEdges()) {
            try {
                writer.write(
                        "\""+edge.getNode1()+"\""
                                +"->"
                                +"\""+edge.getNode2()+"\""
                                + format(" [ label=\"%s\" ]", edge.getWeight())
                                +"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.write("}");
        writer.close();
        System.out.println("File generated");
        dotToPng();

    }

    public void display(){
        for (Edge edge : graph.getEdges()){
            System.out.println("(" + edge.getNode1() + ","+ edge.getNode2() + ", " + edge.getWeight() +")" );
        }
    }


    private void dotToPng() {
        try (InputStream dotFile = new FileInputStream(dotFilePath)) {

            MutableGraph mutableGraph = new Parser().read(dotFile);
            Graphviz.fromGraph(mutableGraph).width(10000).render(Format.PNG).toFile(new File(pngFilePath));
            System.out.println("Image generated");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

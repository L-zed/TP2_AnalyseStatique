package jdt.builders;

import graph.Edge;
import graph.Graph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import jdt.visitors.MethodDeclarationVisitor;
import jdt.visitors.MethodInvocationVisitor;
import jdt.visitors.TypeDeclarationVisitor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class GraphBuilder {

    private final String dotFilePath;
    private final String pngFilePath;
    private Graph graph;


    public GraphBuilder(String fileName) {
        dotFilePath = "src/main/resources/" + fileName + ".dot";
        pngFilePath = "src/main/resources/" + fileName + ".png";
        graph = new Graph();
    }

    public Graph getGraph() {
        return graph;
    }

    public List<String> classesNamesInApplication(ASTCreator astCreator, ArrayList<File> javaFiles) throws IOException {
        List<String> classesInApplication = new ArrayList<>();
        for (File javaFile : javaFiles) {
            String content = FileUtils.readFileToString(javaFile);
            CompilationUnit cu = astCreator.parse(content.toCharArray());

            TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
            cu.accept(typeDeclarationVisitor);
            classesInApplication.add(typeDeclarationVisitor.getTypeDeclaration().getName().toString());
        }
        return classesInApplication;
    }

    public void createGraph(ASTCreator astCreator, ArrayList<File> javaFiles) throws IOException {
        List<String> classesNamesInApplication = classesNamesInApplication(astCreator, javaFiles);

        TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();

        for (File javaFile : javaFiles) {
            String content = FileUtils.readFileToString(javaFile);
            CompilationUnit cu = astCreator.parse(content.toCharArray());
            cu.accept(typeDeclarationVisitor);
            TypeDeclaration typeDeclaration = typeDeclarationVisitor.getTypeDeclaration();

                MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
                typeDeclaration.accept(methodDeclarationVisitor);
                graph.addNode(typeDeclaration.getName().toString());

                for (MethodDeclaration method : methodDeclarationVisitor.getMethods()) {
                    MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
                    method.accept(methodInvocationVisitor);

                    for (MethodInvocation methodInvocation : methodInvocationVisitor.getMethods()) {
                        String classNameOfInvokeMethod =  getNameClassOfInvokeMethod(methodInvocation,typeDeclaration);
                        if (classesNamesInApplication.contains(classNameOfInvokeMethod)){
                            if (!typeDeclaration.getName().toString().equals(classNameOfInvokeMethod)){
                                graph.addEdge(typeDeclaration.getName().toString(),classNameOfInvokeMethod);
                            }

                        }
                    }
            }
        }
    }

    private String getNameClassOfInvokeMethod(MethodInvocation methodInvocation, TypeDeclaration typeDeclaration){
        String classInvokedMethodName = "";

        if (( methodInvocation.getExpression() != null) &&
                (methodInvocation.getExpression().resolveTypeBinding() != null)) {
            classInvokedMethodName+= methodInvocation.getExpression().resolveTypeBinding().getName();
        }
        else {
            classInvokedMethodName+= typeDeclaration.getName().getFullyQualifiedName();
        }
        return classInvokedMethodName;
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
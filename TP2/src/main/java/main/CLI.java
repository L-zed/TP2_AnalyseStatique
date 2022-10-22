package main;

import methods.ASTCreator;
import methods.Graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CLI {

    public static ArrayList<File> listJavaFilesFromFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesFromFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    public static void main(String[] args) throws IOException {

        System.out.println("                                                            ******************************" +
                "\n" +
                           "                                                            ****STATIC CODE ANALYSIS******"+
                "\n"+
                           "                                                            ******************************"
        );

        System.out.println("Welcome this application help you to analyze the source code of an application" +"\n");
        //Path application

        String projectSourcePath;
        System.out.println("Please enter your project source path");
        java.util.Scanner entree1 =   new java.util.Scanner(System.in);
        projectSourcePath = entree1.nextLine();

        // Scan jre path
        String jrePath;
        System.out.println("Please enter your project jre path");
        jrePath = entree1.nextLine();

        //Create AST
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesFromFolder(folder);
        ASTCreator ast = new ASTCreator(projectSourcePath, jrePath);

        Graph graph = new Graph("graph");
        graph.createCallGraph(ast, javaFiles);
    }
}

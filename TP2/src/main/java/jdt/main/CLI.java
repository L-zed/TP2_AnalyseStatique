package jdt.main;

import compositeCluster.ICluster;
import jdt.builders.HierarchyClusters;
import jdt.builders.ASTCreator;
import jdt.builders.GraphBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) throws IOException{

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

        //graph
        GraphBuilder graphConstructor = new GraphBuilder("graph");
        graphConstructor.createGraph(ast, javaFiles);
        graphConstructor.generateGraph();

        //dendrogram
        HierarchyClusters hierarchyClusters = new HierarchyClusters(graphConstructor.getGraph());

        System.out.println("\n" +
                "Enter 1 to get coupling metric between two classes  " + "\n" +
                "Enter 2 to get the dendrogram of your application" +"\n" +
                "Enter 3 to get modules of your application" + "\n" +
                "Enter 0 to quiet" + "\n");

        int entree;
        do{
            entree = entree1.nextInt();

            switch (entree){
                case 1 :
                    java.util.Scanner entree2 =   new java.util.Scanner(System.in);
                    System.out.println("enter the name of the first class");
                    String class1 = "";
                    class1 = entree2.nextLine();
                    java.util.Scanner entree3 =   new java.util.Scanner(System.in);
                    System.out.println("enter the name of the second class");
                    String class2 = "";
                    class2 = entree3.nextLine();

                    System.out.println(graphConstructor.getGraph().couplingClasses(class1,class2));
                    break;
                case 2 :
                    System.out.println(hierarchyClusters.clusteringHierarchy().getClusters().toString());
                    break;
                case 3 :
                    List<ICluster> modules = hierarchyClusters.getClustersGreaterThanCp(hierarchyClusters.clusteringHierarchy(),
                            -1f,
                            5);
                    System.out.println(modules);

            }
            if (entree != 0){
                System.out.println("enter the number on the question you want, or O to quiet");
            }
        }while (entree != 0);


        /*System.out.println(graphConstructor.getGraph().couplingClasses("AA","BB"));

        System.out.println(hierarchyClusters.clusteringHierarchy().getClusters().toString());
        List<ICluster> modules = hierarchyClusters.getClustersGreaterThanCp(hierarchyClusters.clusteringHierarchy(),
                -1f,
                5);
        System.out.println(modules);
        System.out.println(modules.size());*/


    }
}

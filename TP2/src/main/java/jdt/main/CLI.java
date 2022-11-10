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
                "Enter 2 to get the dendrogram as list" +"\n" +
                "Enter 3 to get modules as list" + "\n" +
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
                    System.out.println("enter CP");
                    java.util.Scanner entree4 =   new java.util.Scanner(System.in);
                    float cp = entree4.nextFloat();
                    List<ICluster> modules =
                            hierarchyClusters.getClustersGreaterThanCp(
                                    hierarchyClusters.clusteringHierarchy(),
                                    cp);
                    int max  = (hierarchyClusters.getGraph().getNodes().size() / 2);
                    System.out.println("Modules : ");
                    if (modules.size() < max){
                        System.out.println(modules);
                    }
                    else{
                        for (int i = 0; i< max; i++){
                            System.out.println("[" + modules.get(i) + "]");
                        }
                    }

            }
            if (entree != 0){
                System.out.println("enter the number on the question you want, or O to quiet");
            }
        }while (entree != 0);

    }
}

package main;

import builder.GraphBuilder;
import builder.HierarchyClusters;
import compositeCluster.ICluster;
import parser.SpoonParser;
import processors.ClassProcessor;
import processors.MethodeInvokedProcessor;

import java.io.IOException;
import java.util.List;

public class CLI {
    public static void main(String[] args) throws IOException {

        String projectPath;
        System.out.println("Please enter your project path");
        java.util.Scanner entree0 =   new java.util.Scanner(System.in);
        projectPath = entree0.nextLine();
        SpoonParser spoonParser = new SpoonParser(projectPath);
        spoonParser.configure();

        ClassProcessor classProcessor = new ClassProcessor();
        spoonParser.addProcessor(classProcessor);

        MethodeInvokedProcessor methodeInvokedProcessor = new MethodeInvokedProcessor();
        spoonParser.addProcessor(methodeInvokedProcessor);

        spoonParser.run();


        //GRaph
        GraphBuilder graphConstructor = new GraphBuilder("graph");
        graphConstructor.createGraph(methodeInvokedProcessor.getMethodsInvoked(), classProcessor.getClassesNames() );
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
            java.util.Scanner entree1 =   new java.util.Scanner(System.in);
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

                    System.out.println(graphConstructor.getGraph().couplingClasses(
                            "project.src."+class1,
                            "project.src."+class2));
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

    }

}

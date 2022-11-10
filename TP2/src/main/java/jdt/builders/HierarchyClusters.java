package jdt.builders;

import compositeCluster.Cluster;
import compositeCluster.Clusters;
import compositeCluster.ICluster;
import graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class HierarchyClusters {
    private Graph graph;

    public HierarchyClusters(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public Clusters graphToCluster(){
        Clusters clusters = new Clusters();
        for (String node : this.graph.getNodes()){
            Cluster cluster = new Cluster(node);
            clusters.getClusters().add(cluster);
        }
        return clusters;
    }

    // clustering hierarchy
    public ICluster clusteringHierarchy() {
        Clusters clusters = graphToCluster();
        while (clusters.getClusters().size() > 1){
            ICluster [] closestClusters = findClosestClusters(clusters);
            ICluster cluster3 = new Clusters();
            cluster3.getClusters().add(closestClusters[0]);
            cluster3.getClusters().add(closestClusters[1]);

            clusters.getClusters().remove(closestClusters[0]);
            clusters.getClusters().remove(closestClusters[1]);
            clusters.getClusters().add(cluster3);
        }
        return clusters.getClusters().get(0);
    }

    public float couplingClusters (ICluster cluster1, ICluster cluster2) {
        float result = 0;

        List<String> clustersNames1 = cluster1.getClustersNames();
        java.util.List<String> clustersNames2 = cluster2.getClustersNames();

        for (String clusterName1 : clustersNames1) {
            for (String clusterName2 : clustersNames2) {
                if (!clusterName1.equals(clusterName2)) {
                    float tmp = graph.couplingClasses(clusterName1, clusterName2);
                    result += tmp;
                }
            }
        }
        return result;
    }

    public ICluster[] findClosestClusters(Clusters clusters){
        ICluster[] closestClusters = new ICluster [2];
        float max = -1;
        for (ICluster clusterI : clusters.getClusters() ){
            for (ICluster clusterJ : clusters.getClusters() ){
                if (!clusterI.equals(clusterJ)){
                    float coupling = couplingClusters(clusterI, clusterJ);
                    if( max < coupling){
                        closestClusters[0] = clusterI;
                        closestClusters[1] = clusterJ;
                        max = coupling;
                    }
                }
            }
        }
        return closestClusters;
    }

    //get clusters > cp

    public List<ICluster> getClustersGreaterThanCp(ICluster dendrogram, float cp, int max) {
        List<ICluster> clusters = new ArrayList<>();
            if (dendrogram.getClusters().size() > 1) {
                float cl = couplingClusters(dendrogram.getClusters().get(0),
                        dendrogram.getClusters().get(1));

                if (cl > cp){
                    clusters.add(dendrogram);
                }
                if (clusters.size() < max){
                    clusters.addAll(getClustersGreaterThanCp(dendrogram.getClusters().get(0), cp, max));
                    clusters.addAll(getClustersGreaterThanCp(dendrogram.getClusters().get(1), cp, max));
                }
            }

        return clusters;
    }

}

package compositeCluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Clusters implements ICluster {
    List<ICluster> clusters;

    public Clusters(){
        this.clusters = new ArrayList<>();
    }

    public void addCluster(ICluster cluster){
        this.getClusters().add(cluster);
    }

    @Override
    public List<String> getClustersNames() {
        List<String> clustersComponents = new ArrayList<>();
        for (ICluster cluster :
                this.clusters) {
            clustersComponents.addAll(cluster.getClustersNames());
        }
        return clustersComponents;
    }

    @Override
    public List<ICluster> getClusters() {
        return this.clusters;
    }

    @Override
    public String toString() {
        StringBuilder clusterStrBuilder = new StringBuilder();
        clusterStrBuilder.append("( ");
        int clusterSize = this.clusters.size();
        for (int i = 0; i < clusterSize; i++) {
            clusterStrBuilder.append((i != 0 ? ", " : "") + this.clusters.get(i).toString());
        }
        clusterStrBuilder.append(" )");
        return  clusterStrBuilder.toString();
    }
}

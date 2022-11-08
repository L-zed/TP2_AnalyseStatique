package compositeCluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements ICluster{

    private String name;

    public Cluster() {
    }

    public Cluster(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getClustersNames() {
        List<String> name = new ArrayList<>();
        name.add(this.name);
        return  name;
    }

    @Override
    public List<ICluster> getClusters() {
        List<ICluster> clusters = new ArrayList<>();
        clusters.add(this);
        return clusters;
    }

    @Override
    public String toString() {
        return name;
    }
}

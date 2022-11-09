package compositeCluster;

import java.util.List;

public interface ICluster {
    List<String> getClustersNames();
    List<ICluster> getClusters();
}

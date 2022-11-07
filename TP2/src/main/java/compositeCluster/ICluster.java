package compositeCluster;

import java.util.List;
import java.util.Set;

public interface ICluster {
    List<String> getClustersNames();
    List<ICluster> getClusters();
}

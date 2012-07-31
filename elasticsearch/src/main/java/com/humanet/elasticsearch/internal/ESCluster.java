package com.humanet.elasticsearch.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;

public class ESCluster {

    public static final Log log = LogFactory.getLog(ESCluster.class);

    private Client client;

    public ESCluster(ESServer server) {
        client = server.getClient();
    }

    private ClusterHealthStatus getClusterHealthStatus() {
        return client.admin().cluster().prepareHealth()
            .execute()
            .actionGet()
            .getStatus();
    }

    public NodeInfo[] getClusterInfo() {
        NodesInfoResponse clusterInfo = client.admin().cluster()
            .nodesInfo(new NodesInfoRequest())
            .actionGet();

        return clusterInfo.getNodes();
    }

}

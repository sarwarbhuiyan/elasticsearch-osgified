package com.humanet.elasticsearch.internal;

import com.humanet.elasticsearch.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;

/**
 * User: Hugo Marcelino
 * Date: 29/6/11
 */
public class ESMonitor {

    public static final Log log = LogFactory.getLog(ESMonitor.class);

    private Client client;

    public ESMonitor(ESServer server) {
        client = server.getClient();
    }

    private ClusterHealthStatus getClusterHealthStatus() {
        return client.admin().cluster().prepareHealth()
                .execute()
                .actionGet()
                .getStatus();
    }

    public Status getClusterStatus() {
        ClusterHealthStatus status = getClusterHealthStatus();

        if (ClusterHealthStatus.RED.equals(status)) {
            log.info("ElasticSearch cluster status is " + status + ". Waiting for ElasticSearch recovery.");

            // Waits at most 30 seconds to make sure the cluster health is at least yellow.
            client.admin().cluster().prepareHealth()
                    .setWaitForYellowStatus()
                    .setTimeout("30s")
                    .execute()
                    .actionGet();
        }

        // Check the cluster health for a final time.
        status = getClusterHealthStatus();
        log.info("ES cluster status is " + status);

        // If we are still in red status, then we cannot proceed.
        if (ClusterHealthStatus.RED.equals(status)) {
            String errorMsg = "ElasticSearch cluster health status is RED. Server is not able to start.";
            log.error(errorMsg);
            return Status.failure(errorMsg);
        }

        return Status.success();
    }

    public NodeInfo[] getClusterInfo() {
        NodesInfoResponse clusterInfo = client.admin().cluster()
                .nodesInfo(new NodesInfoRequest())
                .actionGet();

        return clusterInfo.getNodes();
    }

}

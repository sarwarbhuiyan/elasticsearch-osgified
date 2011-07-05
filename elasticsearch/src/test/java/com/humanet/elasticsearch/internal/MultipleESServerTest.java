package com.humanet.elasticsearch.internal;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

/**
 * User: Hugo Marcelino
 * Date: 30/6/11
 */
public class MultipleESServerTest {

    private ESConfigurationBuilder s1Properties = new ESConfigurationBuilder()
            .withName("server1")
            .withHttp(false)
            .withTransportTcpPort(9300)
            .withClusterName("es-cluster");

    private ESConfigurationBuilder s2Properties = new ESConfigurationBuilder()
            .withName("server2")
            .withHttp(false)
            .withTransportTcpPort(9301)
            .withClusterName("es-cluster");

    @AfterMethod
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File("data"));
    }

    @Test
    public void check_cluster_connection_with_multicast() {
        ESServer server1 = new ESServer(s1Properties.build());
        ESServer server2 = new ESServer(s2Properties.build());

        server1.start();
        server2.start();

        ESMonitor esMonitor = new ESMonitor(server1);
        NodeInfo[] clusterInfo = esMonitor.getClusterInfo();

        assertEquals(clusterInfo.length, 2);

        assertThat(clusterInfo, containsServer("server1"));
        assertThat(clusterInfo, containsServer("server2"));

        server1.stop();
        server2.stop();
    }

    private Matcher<NodeInfo[]> containsServer(final String serverName) {
        return new TypeSafeMatcher<NodeInfo[]>() {
            @Override
            public boolean matchesSafely(NodeInfo[] nodeInfos) {
                for (NodeInfo nodeInfo : nodeInfos) {
                    if (nodeInfo.getNode().getName().equals(serverName)) return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("A server was found with name " + serverName);
            }
        };
    }

    @Test
    public void check_cluster_connection_with_unicast() {
        s1Properties.withclusterNode("127.0.0.1:9301");
        s2Properties.withclusterNode("127.0.0.1:9300");

        ESServer server1 = new ESServer(s1Properties.build());
        ESServer server2 = new ESServer(s2Properties.build());

        server1.start();
        server2.start();

        ESMonitor esMonitor = new ESMonitor(server1);
        NodeInfo[] clusterInfo = esMonitor.getClusterInfo();

        assertEquals(clusterInfo.length, 2);

        assertThat(clusterInfo, containsServer("server1"));
        assertThat(clusterInfo, containsServer("server2"));

        server1.stop();
        server2.stop();
    }

}

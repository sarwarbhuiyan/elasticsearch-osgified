package com.humanet.elasticsearch.internal;

import java.util.HashMap;
import java.util.Map;

public class ESConfigurationBuilder {

    public Map<String, String> properties = new HashMap<String, String>();

    public ESConfigurationBuilder withName(String name) {
        properties.put("name", name);
        return this;
    }

    public ESConfigurationBuilder withClusterName(String clusterName) {
        properties.put("cluster.name", clusterName);
        return this;
    }

    public ESConfigurationBuilder withHttp(boolean enableHttp) {
        properties.put("http.enabled", String.valueOf(enableHttp));
        return this;
    }

    public ESConfigurationBuilder withTransportTcpPort(int port) {
        properties.put("transport.tcp.port", String.valueOf(port));
        return this;
    }

    public ESConfigurationBuilder withclusterNode(String clusterNode) {
        properties.put("discovery.zen.ping.unicast.hosts", clusterNode);
        return this;
    }

    public Map<String, String> build() {
        return properties;
    }
}
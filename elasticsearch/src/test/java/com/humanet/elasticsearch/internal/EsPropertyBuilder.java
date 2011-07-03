package com.humanet.elasticsearch.internal;

import java.util.HashMap;
import java.util.Map;

public class ESPropertyBuilder {

    public Map<String, String> properties = new HashMap<String, String>();

    public ESPropertyBuilder withName(String name) {
        properties.put("name", name);
        return this;
    }

    public ESPropertyBuilder withClusterName(String clusterName) {
        properties.put("cluster.name", clusterName);
        return this;
    }

    public ESPropertyBuilder withHttp(boolean enableHttp) {
        properties.put("http.enabled", String.valueOf(enableHttp));
        return this;
    }

    public ESPropertyBuilder withTransportTcpPort(int port) {
        properties.put("transport.tcp.port", String.valueOf(port));
        return this;
    }

    public ESPropertyBuilder withclusterNode(String clusterNode) {
        properties.put("discovery.zen.ping.unicast.hosts", clusterNode);
        return this;
    }

    public Map<String, String> build() {
        return properties;
    }
}
package com.humanet.elasticsearch.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Hugo Marcelino
 * Date: 28/6/11
 * <p/>
 * This class was build based on https://gist.github.com/977580
 */
public class ESServer {

    public static final Log log = LogFactory.getLog(ESServer.class);

    private Map<String, String> configuration;
    private Node node;

    public ESServer() {
        //noinspection NullableProblems
        this(null);
    }

    public ESServer(Map<String, String> configuration) {
        this.configuration = configuration != null
                ? configuration
                : Collections.<String, String>emptyMap();

        setUp();
    }

    private void setUp() {
        final Settings settings = ImmutableSettings.settingsBuilder().put(configuration).build();

        node = NodeBuilder.nodeBuilder()
                .settings(settings)
                .loadConfigSettings(false)
                .build();

        if ("true".equalsIgnoreCase(settings.get("es.max.files"))) {
            final String workPath = node.settings().get("path.work");
            final int maxOpen = FileSystemUtils.maxOpenFiles(new File(workPath));
            log.info("The maximum number of open files for user " + System.getProperty("user.name") + " is " + maxOpen);
        }

        if (log.isInfoEnabled()) {
            log.info("Starting the Elastic Search server node with these settings:");
            final Map<String, String> map = node.settings().getAsMap();
            final List<String> keys = new ArrayList<String>(map.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                log.info(" " + key + " : " + getValue(map, key));
            }
        }
    }

    private static String getValue(Map<String, String> map, String key) {
        if (key.startsWith("cloud.aws.secret")) return "<HIDDEN>";
        return map.get(key);
    }

    public void start() {
        log.info("Starting FullTextSearch Server with ElasticSearch");
        node.start();

        //Small shortcut to see if everything is ok.
        ESMonitor esMonitor = new ESMonitor(this);
        if (esMonitor.getClusterStatus().equals(com.humanet.elasticsearch.internal.Status.nok)) {
            node = null;
            throw new RuntimeException("ES cluster health status is RED. Server is not able to start.");

        } else {
            log.info("FullTextSearch Server running");
        }
    }

    public void stop() {
        if (node != null) {
            log.info("Starting FullTextSearch Server with ElasticSearch");

            node.stop();
            node = null;

            log.info("FullTextSearch Server stopped");
        } else {
            log.info("The server appears to be already stopped. Are you sure you started the server ?");

        }
    }

    public boolean isRunning() {
        return node != null;
    }

    protected Client getClient() {
        return node.client();
    }
}
